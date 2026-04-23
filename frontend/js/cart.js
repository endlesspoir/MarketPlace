import {
  clearPromoCode,
  getCartItemsCount,
  getCartState,
  initCartBadge,
  removeFromCart,
  setCartItemQty,
  setPromoCode
} from "./cart-store.js";

const TAX_RATE = 0.08;
const DISCOUNT_CODE = "SAVE10";
const FREE_SHIPPING_THRESHOLD = 50;
let promoFeedback = null;

function money(value) {
  return `$${value.toFixed(2)}`;
}

function buildProductsMap(products) {
  return new Map(products.map(item => [String(item.id), item]));
}

function calculateTotals(items, productsMap, promoCode) {
  const subtotal = items.reduce((sum, item) => {
    const product = productsMap.get(item.id);
    if (!product) return sum;
    return sum + Number(product.price) * item.qty;
  }, 0);

  const tax = subtotal * TAX_RATE;
  const preDiscountTotal = subtotal + tax;
  const discount = promoCode === DISCOUNT_CODE ? preDiscountTotal * 0.1 : 0;
  const total = Math.max(0, preDiscountTotal - discount);

  return { subtotal, tax, discount, total };
}

function renderEmptyState(root) {
  root.innerHTML = `
    <section class="empty-state">
      <div>
        <div class="empty-icon">&#128717;</div>
        <h1>Your cart is empty</h1>
        <p>Discover amazing products for your furry friends!</p>
        <button class="back-btn" id="start-shopping" type="button">Start Shopping</button>
      </div>
    </section>
  `;

  document.getElementById("start-shopping").addEventListener("click", () => {
    window.location.href = "./index.html";
  });
}

function renderCart(root, products, state) {
  const productsMap = buildProductsMap(products);
  const activeItems = state.items.filter(item => productsMap.has(item.id));

  if (activeItems.length === 0) {
    renderEmptyState(root);
    return;
  }

  const totals = calculateTotals(activeItems, productsMap, state.promoCode);
  const itemsCount = getCartItemsCount();
  const itemsLabel = `${itemsCount} ${itemsCount === 1 ? "item" : "items"}`;
  const shippingLeft = Math.max(0, FREE_SHIPPING_THRESHOLD - totals.subtotal);
  const shippingStatusText = shippingLeft > 0 ? `${money(shippingLeft)} away` : "✓ Qualified!";
  const shippingStatusClass = shippingLeft > 0
    ? "cart-alert-status cart-alert-status--pending"
    : "cart-alert-status cart-alert-status--ok";
  const discountRow = totals.discount > 0
    ? `<div class="summary-line"><span>Discount</span><strong>-${money(totals.discount)}</strong></div>`
    : "";

  root.innerHTML = `
    <section class="cart-top">
      <div class="cart-top-inner">
        <div class="cart-header-row">
          <div>
            <h1 class="cart-title">Shopping Bag</h1>
            <p class="cart-subtitle">${itemsLabel} ready for checkout</p>
          </div>

          <ol class="checkout-steps" aria-label="Checkout steps">
            <li class="step is-active">
              <span class="step-dot">1</span>
              <span class="step-label">Cart</span>
            </li>
            <li class="step-divider" aria-hidden="true">›</li>
            <li class="step">
              <span class="step-dot">2</span>
              <span class="step-label">Checkout</span>
            </li>
            <li class="step-divider" aria-hidden="true">›</li>
            <li class="step">
              <span class="step-dot">3</span>
              <span class="step-label">Complete</span>
            </li>
          </ol>
        </div>

        <div class="cart-alert">
          <div class="cart-alert-left">
            <svg class="alert-icon" viewBox="0 0 24 24" aria-hidden="true">
              <path d="M9 3h9l3 3v9l-9 9-9-9V6l3-3h3z"></path>
              <circle cx="8.5" cy="8.5" r="1.25"></circle>
            </svg>
            <span>Free shipping on orders over <strong>${money(FREE_SHIPPING_THRESHOLD)}</strong></span>
          </div>
          <span class="${shippingStatusClass}">${shippingStatusText}</span>
        </div>
      </div>
    </section>

    <section class="cart-content">
      <div class="cart-content-inner">
        <div class="cart-grid">
          <div class="cart-list">
            ${activeItems
              .map(item => {
                const product = productsMap.get(item.id);
                const linePrice = Number(product.price) * item.qty;
                return `
                  <article class="cart-item" data-id="${item.id}">
                    <img class="cart-item-image" src="./${product.imagesUrl?.[0] || "img/1.png"}" alt="${product.name}">
                    <div class="cart-item-main">
                      <h2 class="cart-item-title">${product.name}</h2>
                      <p class="cart-item-sub">Toys & Scratchers</p>
                    </div>
                    <div class="qty-control">
                      <button type="button" class="qty-btn qty-minus" aria-label="Decrease quantity">-</button>
                      <span class="qty-value">${item.qty}</span>
                      <button type="button" class="qty-btn qty-plus" aria-label="Increase quantity">+</button>
                    </div>
                    <div class="cart-price-wrap">
                      <p class="cart-price">${money(linePrice)}</p>
                      <span class="cart-price-sub">${money(Number(product.price))} each</span>
                    </div>
                    <button type="button" class="remove-btn" aria-label="Remove item">
                      <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M4 7h16"></path>
                        <path d="m9 7 .6-2h4.8l.6 2"></path>
                        <path d="M7 7v12h10V7"></path>
                        <path d="M10 11v5M14 11v5"></path>
                      </svg>
                    </button>
                  </article>
                `;
              })
              .join("")}
          </div>

          <aside class="summary">
            <div class="summary-head">
              <h2>Order Summary</h2>
              <p>${itemsLabel} in your bag</p>
            </div>

            <div class="summary-body">
              <label for="promo-input" class="promo-label">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M9 3h9l3 3v9l-9 9-9-9V6l3-3h3z"></path>
                  <circle cx="8.5" cy="8.5" r="1.25"></circle>
                </svg>
                Promo Code
              </label>
              <div class="promo-row">
                <input id="promo-input" placeholder="Enter code" value="${state.promoCode || ""}">
                <button id="promo-apply" type="button">Apply</button>
              </div>
              <div class="promo-message" id="promo-message"></div>
              <div class="summary-divider"></div>

              <div class="summary-rows">
                <div class="summary-line"><span>Subtotal</span><strong>${money(totals.subtotal)}</strong></div>
                <div class="summary-line"><span>Tax (8%)</span><strong>${money(totals.tax)}</strong></div>
                ${discountRow}
                <div class="summary-line total"><span>Total</span><span class="total-value">${money(totals.total)}</span></div>
              </div>

              <div class="summary-meta">
                <div class="summary-meta-card summary-meta-card--delivery">
                  <span class="summary-meta-icon">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                      <circle cx="12" cy="12" r="9"></circle>
                      <path d="M12 7v5l3 2"></path>
                    </svg>
                  </span>
                  <div>
                    <strong>Delivery Time</strong>
                    <span>3-5 business days</span>
                  </div>
                </div>
                <div class="summary-meta-card summary-meta-card--shipping">
                  <span class="summary-meta-icon">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                      <path d="M12 21s6-5.4 6-10a6 6 0 1 0-12 0c0 4.6 6 10 6 10z"></path>
                      <circle cx="12" cy="11" r="2.5"></circle>
                    </svg>
                  </span>
                  <div>
                    <strong>Shipping To</strong>
                    <span>123 Main Street, NY 10001</span>
                  </div>
                </div>
              </div>

              <button class="checkout-btn" type="button">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <rect x="3" y="6" width="18" height="12" rx="2"></rect>
                  <path d="m3 9 9 6 9-6"></path>
                </svg>
                Proceed to Checkout
              </button>
              <button class="continue-btn" id="continue-shopping" type="button">← Continue Shopping</button>
            </div>
          </aside>
        </div>
      </div>
    </section>
  `;

  root.querySelector(".cart-list").addEventListener("click", event => {
    const itemNode = event.target.closest(".cart-item");
    if (!itemNode) return;

    const itemId = itemNode.dataset.id;
    const stateNow = getCartState();
    const currentItem = stateNow.items.find(entry => entry.id === itemId);
    if (!currentItem) return;

    if (event.target.closest(".qty-plus")) {
      setCartItemQty(itemId, currentItem.qty + 1);
      initPage(products);
      return;
    }

    if (event.target.closest(".qty-minus")) {
      setCartItemQty(itemId, currentItem.qty - 1);
      initPage(products);
      return;
    }

    if (event.target.closest(".remove-btn")) {
      removeFromCart(itemId);
      initPage(products);
    }
  });

  document.getElementById("continue-shopping").addEventListener("click", () => {
    window.location.href = "./index.html";
  });

  const promoMessage = document.getElementById("promo-message");
  if (promoFeedback) {
    promoMessage.className = `promo-message ${promoFeedback.type}`;
    promoMessage.textContent = promoFeedback.text;
  }

  document.getElementById("promo-apply").addEventListener("click", () => {
    const input = document.getElementById("promo-input");
    const code = input.value.trim().toUpperCase();

    if (!code) {
      promoFeedback = { type: "error", text: "Введите промокод" };
      clearPromoCode();
      initPage(products);
      return;
    }

    if (code === DISCOUNT_CODE) {
      setPromoCode(code);
      promoFeedback = { type: "success", text: "Промокод применен: -10%" };
      initPage(products);
      return;
    }

    promoFeedback = { type: "error", text: "Неверный промокод" };
    clearPromoCode();
    initPage(products);
  });
}

async function initPage(productsSource) {
  const root = document.getElementById("cart-page");

  if (productsSource) {
    renderCart(root, productsSource, getCartState());
    return;
  }

  const res = await fetch("./data/products.json");
  const data = await res.json();
  renderCart(root, data.content, getCartState());
}

async function initCartPage() {
  initCartBadge();
  try {
    await initPage();
  } catch (error) {
    document.getElementById("cart-page").innerHTML = "<p>Unable to load cart.</p>";
  }
}

initCartPage();
