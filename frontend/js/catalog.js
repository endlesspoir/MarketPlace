let products = [];
let filteredProducts = [];

import { addToCart, initCartBadge } from "./cart-store.js";

let filters = {
  ratings: [],
  minPrice: 0,
  maxPrice: Infinity,
  sort: "az"
};

export async function initCatalog() {
  initCartBadge();
  const res = await fetch("./data/products.json");
  const data = await res.json();
  products = data.content;
  applyAll();
}

function getStarSVG(rating, index) {
  if (rating >= index) {
    return `<img src="./svg/full-star.svg" alt="★" width="20" height="20">`;
  } else if (rating >= index - 0.5) {
    return `<img src="./svg/half-star.svg" alt="½" width="20" height="20">`;
  } else {
    return `<img src="./svg/empty-star.svg" alt="☆" width="20" height="20">`;
  }
}

function getStarsHTML(rating) {
  let starsHTML = "";
  for (let i = 1; i <= 5; i++) {
    starsHTML += getStarSVG(rating, i);
  }
  return starsHTML;
}

function applyAll() {
  let result = [...products];

  if (filters.ratings.length > 0) {
    result = result.filter(p =>
      filters.ratings.some(r => p.averageRating >= r)
    );
  }

  result = result.filter(
    p => p.price >= filters.minPrice && p.price <= filters.maxPrice
  );

  switch (filters.sort) {
    case "az":
      result.sort((a, b) => a.name.localeCompare(b.name));
      break;
    case "za":
      result.sort((a, b) => b.name.localeCompare(a.name));
      break;
    case "price-asc":
      result.sort((a, b) => a.price - b.price);
      break;
    case "price-desc":
      result.sort((a, b) => b.price - a.price);
      break;
  }

  filteredProducts = result;

  renderProducts(filteredProducts);
  updateCount();
}

function showCartToast(message) {
  let container = document.querySelector(".cart-toast-container");
  if (!container) {
    container = document.createElement("div");
    container.className = "cart-toast-container";
    document.body.appendChild(container);
  }

  const toast = document.createElement("div");
  toast.className = "cart-toast";
  toast.textContent = message;
  container.appendChild(toast);

  requestAnimationFrame(() => {
    toast.classList.add("show");
  });

  setTimeout(() => {
    toast.classList.remove("show");
    setTimeout(() => {
      toast.remove();
    }, 220);
  }, 1800);
}

function renderProducts(list) {
  const catalog = document.querySelector(".catalog");
  catalog.innerHTML = "";

  list.forEach(p => {
    const item = document.createElement("div");
    item.className = "product_item";

    item.innerHTML = `
      <div class="product_item_image_container">
        <div class="product_price_badge">$${p.price}</div>

        <img src="${p.imagesUrl[0]}" alt="${p.name}">

        <div class="product_hover">
          <p class="product_title">${p.name}</p>

          <div class="product_rating_tab">
            <div class="product_rating_stars">
              ${getStarsHTML(p.averageRating)}
            </div>
            <div class="product_rating_number">
              (${p.averageRating})
            </div>
          </div>

          <div class="product_bottom">
            <span class="product_price_big">$${p.price}</span>
            <button class="cart_btn" data-id="${p.id}" type="button" aria-label="Add to cart">
              <img src="./svg/cart.svg" alt="">
            </button>
          </div>
        </div>
      </div>

      <div class="product_info">
        <p class="product_name">${p.name}</p>

        <div class="product_rating_tab">
          <div class="product_rating_stars">
            ${getStarsHTML(p.averageRating)}
          </div>
          <div class="product_rating_number">
            (${p.averageRating})
          </div>
        </div>
      </div>
    `;

    const openProduct = () => {
      window.location.href = `./product.html?id=${encodeURIComponent(p.id)}`;
    };

    const imageNode = item.querySelector(".product_item_image_container img");
    const nameNode = item.querySelector(".product_name");
    const hoverTitleNode = item.querySelector(".product_title");
    const addBtn = item.querySelector(".cart_btn");

    imageNode?.addEventListener("click", openProduct);
    nameNode?.addEventListener("click", openProduct);
    hoverTitleNode?.addEventListener("click", openProduct);

    addBtn?.addEventListener("click", event => {
      event.stopPropagation();
      addToCart(p.id, 1);
      showCartToast(`${p.name} added to cart`);
    });

    catalog.appendChild(item);
  });
}

window.applyRating = function(rating, isChecked) {
  if (isChecked) {
    if (!filters.ratings.includes(rating)) {
      filters.ratings.push(rating);
    }
  } else {
    filters.ratings = filters.ratings.filter(r => r !== rating);
  }

  applyAll();
};

window.applyPrice = function() {
  const minInput = document.getElementById("min");
  const maxInput = document.getElementById("max");
  const minRaw = Number(minInput.value);
  const maxRaw = Number(maxInput.value);
  const min = isNaN(minRaw) ? 0 : Math.max(0, minRaw);
  const max = isNaN(maxRaw) ? Infinity : Math.max(0, maxRaw);

  minInput.value = Number.isFinite(min) ? String(min) : "";
  maxInput.value = Number.isFinite(max) ? String(max) : "";

  if (min > max) {
    filters.minPrice = max;
    filters.maxPrice = min;
  } else {
    filters.minPrice = min;
    filters.maxPrice = max;
  }

  applyAll();
};

window.sortProducts = function(type) {
  filters.sort = type;
  applyAll();
};

function updateCount() {
  document.querySelector(".shown-items-count").textContent =
    `${filteredProducts.length} products`;
}
