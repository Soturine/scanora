const nav = document.querySelector(".nav");

const syncNavState = () => {
  if (!nav) return;
  nav.classList.toggle("is-scrolled", window.scrollY > 18);
};

syncNavState();
window.addEventListener("scroll", syncNavState, { passive: true });
