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

  root.innerHTML = `
    <section class="cart-header">
      <h1 class="cart-title">Shopping Bag</h1>
      <p class="cart-subtitle">${getCartItemsCount()} item ready for checkout</p>
    </section>

    <div class="cart-alert">Free shipping on orders over <strong>$50</strong></div>

    <section class="cart-grid">
      <div class="cart-list">
        ${activeItems
          .map(item => {
            const product = productsMap.get(item.id);
            const linePrice = Number(product.price) * item.qty;
            return `
              <article class="cart-item" data-id="${item.id}">
                <img src="./${product.imagesUrl?.[0] || "img/1.png"}" alt="${product.name}">
                <div>
                  <h2 class="cart-item-title">${product.name}</h2>
                  <span class="cart-item-sub">Travel & Carriers</span>
                </div>
                <div class="item-actions">
                  <div class="qty-control">
                    <button type="button" class="qty-btn qty-minus">-</button>
                    <span class="qty-value">${item.qty}</span>
                    <button type="button" class="qty-btn qty-plus">+</button>
                  </div>
                </div>
                <div class="cart-price-wrap">
                  <p class="cart-price">${money(linePrice)}</p>
                  <span class="cart-price-sub">${money(Number(product.price))} each</span>
                  <button type="button" class="remove-btn">&#128465;</button>
                </div>
              </article>
            `;
          })
          .join("")}
      </div>

      <aside class="summary">
        <div class="summary-head">
          <h3>Order Summary</h3>
          <small>${getCartItemsCount()} items in your bag</small>
        </div>

        <div class="summary-body">
          <label for="promo-input"><strong>Promo Code</strong></label>
          <div class="promo-row">
            <input id="promo-input" placeholder="Enter code" value="${state.promoCode || ""}">
            <button id="promo-apply" type="button">Apply</button>
          </div>
          <div class="promo-message" id="promo-message"></div>

          <div class="summary-rows">
            <div class="summary-line"><span>Subtotal</span><strong>${money(totals.subtotal)}</strong></div>
            <div class="summary-line"><span>Tax (8%)</span><strong>${money(totals.tax)}</strong></div>
            <div class="summary-line"><span>Discount</span><strong>-${money(totals.discount)}</strong></div>
            <div class="summary-line total"><span>Total</span><span class="total-value">${money(totals.total)}</span></div>
          </div>

          <button class="checkout-btn" type="button">Proceed to Checkout</button>
          <button class="continue-btn" id="continue-shopping" type="button">Continue Shopping</button>
        </div>
      </aside>
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

