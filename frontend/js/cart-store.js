const CART_STORAGE_KEY = "pawsstore-cart-v1";
const DEFAULT_STATE = {
  items: [],
  promoCode: ""
};

function readState() {
  try {
    const raw = localStorage.getItem(CART_STORAGE_KEY);
    if (!raw) {
      return { ...DEFAULT_STATE };
    }

    const parsed = JSON.parse(raw);
    const items = Array.isArray(parsed.items)
      ? parsed.items
          .filter(item => item && item.id)
          .map(item => ({ id: String(item.id), qty: Math.max(1, Number(item.qty) || 1) }))
      : [];

    return {
      items,
      promoCode: typeof parsed.promoCode === "string" ? parsed.promoCode : ""
    };
  } catch (error) {
    return { ...DEFAULT_STATE };
  }
}

function writeState(state) {
  localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(state));
}

function emitCartUpdate() {
  window.dispatchEvent(new CustomEvent("cart:updated"));
}

function saveAndNotify(state) {
  writeState(state);
  emitCartUpdate();
  return state;
}

export function getCartState() {
  return readState();
}

export function getCartItemsCount() {
  return readState().items.reduce((sum, item) => sum + item.qty, 0);
}

export function isInCart(productId) {
  const id = String(productId);
  return readState().items.some(item => item.id === id);
}

export function addToCart(productId, qty = 1) {
  const id = String(productId);
  const state = readState();
  const found = state.items.find(item => item.id === id);

  if (found) {
    found.qty += Math.max(1, Number(qty) || 1);
  } else {
    state.items.push({ id, qty: Math.max(1, Number(qty) || 1) });
  }

  return saveAndNotify(state);
}

export function setCartItemQty(productId, qty) {
  const id = String(productId);
  const state = readState();
  const nextQty = Number(qty) || 0;

  if (nextQty <= 0) {
    state.items = state.items.filter(item => item.id !== id);
  } else {
    const found = state.items.find(item => item.id === id);
    if (found) {
      found.qty = Math.max(1, nextQty);
    } else {
      state.items.push({ id, qty: Math.max(1, nextQty) });
    }
  }

  return saveAndNotify(state);
}

export function removeFromCart(productId) {
  const id = String(productId);
  const state = readState();
  state.items = state.items.filter(item => item.id !== id);
  return saveAndNotify(state);
}

export function clearCart() {
  return saveAndNotify({ ...DEFAULT_STATE });
}

export function setPromoCode(code) {
  const state = readState();
  state.promoCode = String(code || "").trim().toUpperCase();
  return saveAndNotify(state);
}

export function clearPromoCode() {
  const state = readState();
  state.promoCode = "";
  return saveAndNotify(state);
}

export function initCartBadge() {
  const update = () => {
    const count = getCartItemsCount();
    document.querySelectorAll(".cart__count").forEach(node => {
      node.textContent = String(count);
      node.style.display = count > 0 ? "flex" : "none";
    });
  };

  update();
  window.addEventListener("cart:updated", update);
  window.addEventListener("storage", event => {
    if (event.key === CART_STORAGE_KEY) {
      update();
    }
  });
}

