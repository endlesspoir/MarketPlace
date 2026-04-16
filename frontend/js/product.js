import { addToCart, initCartBadge, isInCart } from "./cart-store.js";

const KNOWN_BASE_KEYS = new Set(["id", "name", "description", "price", "imagesUrl", "averageRating", "keyHighlights"]);

function getStarSVG(rating, index) {
  if (rating >= index) return `<img src="./svg/full-star.svg" alt="*">`;
  if (rating >= index - 0.5) return `<img src="./svg/half-star.svg" alt="*">`;
  return `<img src="./svg/empty-star.svg" alt="*">`;
}

function getStarsHTML(rating) {
  let stars = "";
  for (let i = 1; i <= 5; i += 1) stars += getStarSVG(rating, i);
  return stars;
}

function toLabel(key) {
  return key.replace(/([a-z])([A-Z])/g, "$1 $2").replace(/[_-]+/g, " ").toUpperCase();
}

function getSpecs(product) {
  return Object.entries(product)
    .filter(([key, value]) => !KNOWN_BASE_KEYS.has(key) && value)
    .slice(0, 6)
    .map(([key, value]) => ({ label: toLabel(key), value: String(value) }));
}

function getHighlights(product) {
  if (Array.isArray(product.keyHighlights) && product.keyHighlights.length > 0) return product.keyHighlights;
  return getSpecs(product).slice(0, 3).map(spec => `${spec.label}: ${spec.value}`);
}

function getGalleryImages(imagesUrl) {
  const source = Array.isArray(imagesUrl) && imagesUrl.length > 0 ? imagesUrl : ["img/1.png"];
  const normalized = [...source];
  while (normalized.length < 3) {
    normalized.push(source[normalized.length % source.length]);
  }
  return normalized.slice(0, 5);
}

function renderBreadcrumbs(product) {
  const breadcrumbs = document.getElementById("breadcrumbs");
  breadcrumbs.innerHTML = `
    <a href="./index.html">Home</a>
    <span class="crumb-divider">></span>
    <a href="./index.html">Products</a>
    <span class="crumb-divider">></span>
    <span>${product.name}</span>
  `;
}

function renderProduct(product) {
  const shell = document.getElementById("product-shell");
  const highlights = getHighlights(product);
  const specs = getSpecs(product);
  const galleryImages = getGalleryImages(product.imagesUrl);
  const inCart = isInCart(product.id);

  shell.innerHTML = `
    <article class="product-card">
      <section class="product-media">
        <div class="media-main-wrap">
          <img id="main-image" class="main-image" src="./${galleryImages[0]}" alt="${product.name}">
          <button id="prev-image" class="media-nav prev" type="button" aria-label="Previous image">&#8249;</button>
          <button id="next-image" class="media-nav next" type="button" aria-label="Next image">&#8250;</button>
        </div>
        <div class="media-thumbs" id="media-thumbs">
          ${galleryImages
            .map((src, idx) => `<button class="thumb-btn ${idx === 0 ? "active" : ""}" data-index="${idx}" type="button"><img src="./${src}" alt="${product.name}"></button>`)
            .join("")}
        </div>
      </section>

      <section class="product-details">
        <span class="product-chip">Travel & Carriers</span>
        <h1 class="product-title">${product.name}</h1>
        <div class="rating-row">
          <div class="rating-stars">${getStarsHTML(product.averageRating || 0)}</div>
          <span>${product.averageRating || 0} out of 5 stars</span>
        </div>
        <p class="product-price">$${Number(product.price).toFixed(2)}</p>

        <div class="panel">
          <h3>Key Highlights</h3>
          <ul>${highlights.map(line => `<li>${line}</li>`).join("")}</ul>
        </div>

        <section class="description accordion" id="desc-accordion">
          <button class="accordion-trigger" id="accordion-trigger" type="button">
            <span>Extended Description</span>
            <span class="accordion-icon">+</span>
          </button>
          <div class="accordion-content" id="accordion-content">
            <p>${product.description || "No description available."}</p>
          </div>
        </section>

        <div class="qty-row">
          <span>Quantity:</span>
          <div class="qty-control">
            <button type="button" class="qty-btn" id="minus-btn">-</button>
            <span class="qty-value" id="qty-value">1</span>
            <button type="button" class="qty-btn" id="plus-btn">+</button>
          </div>
        </div>

        <button type="button" class="add-cart ${inCart ? "in-cart" : ""}" id="add-cart-btn">${inCart ? "In Cart" : "Add to Cart"}</button>

        <section class="specs">
          <h3>Technical Specifications</h3>
          <div class="specs-grid">
            ${specs.map(spec => `<div class="spec-item"><small>${spec.label}</small><strong>${spec.value}</strong></div>`).join("")}
          </div>
        </section>
      </section>
    </article>
  `;

  let quantity = 1;
  let currentIndex = 0;

  const mainImage = document.getElementById("main-image");
  const thumbsWrap = document.getElementById("media-thumbs");
  const qtyValue = document.getElementById("qty-value");
  const addBtn = document.getElementById("add-cart-btn");

  const setImage = nextIndex => {
    currentIndex = (nextIndex + galleryImages.length) % galleryImages.length;
    mainImage.src = `./${galleryImages[currentIndex]}`;
    thumbsWrap.querySelectorAll(".thumb-btn").forEach(btn => {
      btn.classList.toggle("active", Number(btn.dataset.index) === currentIndex);
    });
  };

  document.getElementById("prev-image").addEventListener("click", () => setImage(currentIndex - 1));
  document.getElementById("next-image").addEventListener("click", () => setImage(currentIndex + 1));
  thumbsWrap.addEventListener("click", event => {
    const btn = event.target.closest(".thumb-btn");
    if (!btn) return;
    setImage(Number(btn.dataset.index));
  });

  const updateQty = () => {
    qtyValue.textContent = String(quantity);
  };

  document.getElementById("minus-btn").addEventListener("click", () => {
    quantity = Math.max(1, quantity - 1);
    updateQty();
  });

  document.getElementById("plus-btn").addEventListener("click", () => {
    quantity += 1;
    updateQty();
  });

  addBtn.addEventListener("click", () => {
    addToCart(product.id, quantity);
    addBtn.classList.add("in-cart");
    addBtn.textContent = "In Cart";
  });

  const accordionTrigger = document.getElementById("accordion-trigger");
  const accordionContent = document.getElementById("accordion-content");
  accordionTrigger.addEventListener("click", () => {
    const opened = accordionContent.classList.toggle("open");
    accordionTrigger.querySelector(".accordion-icon").textContent = opened ? "-" : "+";
  });
}

function renderNotFound() {
  const shell = document.getElementById("product-shell");
  const breadcrumbs = document.getElementById("breadcrumbs");
  breadcrumbs.innerHTML = `<a href="./index.html">Home</a> <span class="crumb-divider">></span> <span>Product</span>`;
  shell.innerHTML = `<section class="product-not-found"><h1>Product not found</h1><p>Open <a href="./index.html">catalog</a> and choose another item.</p></section>`;
}

async function initProductPage() {
  initCartBadge();
  try {
    const res = await fetch("./data/products.json");
    const data = await res.json();
    const params = new URLSearchParams(window.location.search);
    const productId = params.get("id") || data.content?.[0]?.id;
    const product = data.content.find(item => item.id === productId);
    if (!product) return renderNotFound();

    renderBreadcrumbs(product);
    renderProduct(product);
  } catch (error) {
    renderNotFound();
  }
}

initProductPage();

