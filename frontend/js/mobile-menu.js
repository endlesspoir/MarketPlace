export function initMobileMenu() {
  const burgerBtn = document.getElementById("burger-btn");
  const mobileMenu = document.getElementById("mobile-menu");
  const mobileMenuBackdrop = document.getElementById("mobile-menu-backdrop");
  const mobileBp = window.matchMedia("(max-width: 1024px)");

  if (!burgerBtn || !mobileMenu || !mobileMenuBackdrop) return;
  if (burgerBtn.dataset.menuInit === "1") return;
  burgerBtn.dataset.menuInit = "1";

  let closeTimerId = null;

  const clearCloseTimer = () => {
    if (!closeTimerId) return;
    window.clearTimeout(closeTimerId);
    closeTimerId = null;
  };

  const setClosedState = () => {
    mobileMenu.hidden = true;
    mobileMenuBackdrop.hidden = true;
  };

  const openMenu = () => {
    if (!mobileBp.matches) return;
    clearCloseTimer();
    mobileMenu.hidden = false;
    mobileMenuBackdrop.hidden = false;
    requestAnimationFrame(() => {
      mobileMenu.classList.add("is-open");
      mobileMenuBackdrop.classList.add("is-open");
    });
    burgerBtn.setAttribute("aria-expanded", "true");
    document.body.classList.add("menu-open");
  };

  const closeMenu = () => {
    clearCloseTimer();
    mobileMenu.classList.remove("is-open");
    mobileMenuBackdrop.classList.remove("is-open");
    burgerBtn.setAttribute("aria-expanded", "false");
    document.body.classList.remove("menu-open");
    closeTimerId = window.setTimeout(setClosedState, 220);
  };

  burgerBtn.addEventListener("click", () => {
    const isOpen = burgerBtn.getAttribute("aria-expanded") === "true";
    if (isOpen) closeMenu();
    else openMenu();
  });

  mobileMenuBackdrop.addEventListener("click", closeMenu);
  mobileMenu.addEventListener("click", event => {
    if (event.target.closest("a")) closeMenu();
  });

  window.addEventListener("keydown", event => {
    if (event.key === "Escape") closeMenu();
  });

  const mediaHandler = event => {
    if (!event.matches) closeMenu();
  };
  if (typeof mobileBp.addEventListener === "function") {
    mobileBp.addEventListener("change", mediaHandler);
  } else if (typeof mobileBp.addListener === "function") {
    mobileBp.addListener(mediaHandler);
  }

  setClosedState();
}
