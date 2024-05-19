import Toast from 'bootstrap/js/dist/toast';
import Navigate from "../Components/Router/Navigate";

const clearPage = () => {
    const main = document.querySelector('main');
    main.innerHTML = '';
  };
  

  // Function to render the page title 
  const renderPageTitle = (title) => {
    if (!title) return;
    const main = document.querySelector('main');
    const pageTitle = document.createElement('h1');
    pageTitle.className = 'page-title';
    pageTitle.innerText = title;
    main.appendChild(pageTitle);
  };

const renderBreadcrumb = (path) => {
  const main = document.querySelector('main');
  const breadcrumb = document.createElement('div');
  const list = document.createElement('ol');
  list.className = 'breadcrumb';
  breadcrumb.style.overflow = 'auto';
  breadcrumb.style.margin = '10px';
  Object.entries(path).forEach(([name, url]) => {
    const item = document.createElement('li');
    item.style.color = 'blue'
    item.className = 'breadcrumb-item';
    item.textContent = name;
    item.style.cursor = 'pointer';
    item.addEventListener('click', () => {
      const links = document.querySelectorAll('.nav-link');
      links.forEach(l => l.classList.remove('active'));
      links.forEach(l => l.classList.remove('text-white'));
      Navigate(url);
    });
    list.appendChild(item);
  });
  list.lastChild.className = 'breadcrumb-item active';
  list.lastChild.style.color = 'gray'
  breadcrumb.appendChild(list);
  main.prepend(breadcrumb);
}

function displayToast(message, type = 'danger') {
  let toastContainer = document.querySelector('.toast-container');
  if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.className = 'toast-container position-absolute p-3 bottom-0 end-0';
    document.body.appendChild(toastContainer);
  }

  const toast = document.createElement('div');
  toast.className = `toast align-items-center text-white bg-${type} border-0`;
  toast.role = 'alert';
  toast.ariaLive = 'assertive';
  toast.ariaAtomic = 'true';

  const closeButton = document.createElement('button');
  closeButton.type = 'button';
  closeButton.className = 'btn-close';
  closeButton.setAttribute('data-bs-dismiss', 'toast');
  closeButton.setAttribute('aria-label', 'Close');
  closeButton.addEventListener('click', () => {
    if (toast.parentNode) {
      toast.parentNode.removeChild(toast);
    }
  });

  const toastBody = document.createElement('div');
  toastBody.className = 'toast-body d-flex justify-content-between align-items-center';
  toastBody.innerText = message;

  toastBody.appendChild(closeButton);
  toast.appendChild(toastBody);
  toastContainer.appendChild(toast);

  const toastInstance = new Toast(toast);

  toastInstance.show();

  toast.addEventListener('hidden.bs.toast', () => {
    if (toast.parentNode) {
      toast.parentNode.removeChild(toast);
    }
  });
}

export {
  clearPage,
  renderPageTitle,
  renderBreadcrumb,
  displayToast
};
  