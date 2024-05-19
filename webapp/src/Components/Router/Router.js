import {
  verifyToken
} from '../../utils/auths';
import routes from './routes';
import Navigate from "./Navigate";
import Navbar from "../Navbar/Navbar";

// Define Router component
const Router = () => {
  onFrontendLoad();
  onNavBarClick();
  onHistoryChange();
};

// Function to handle navbar click events
function onNavBarClick() {
  const navbarWrapper = document.querySelector('#navbarWrapper');

  navbarWrapper.addEventListener('click', (e) => {
    e.preventDefault();
    const navBarItemClicked = e.target;
    const uri = navBarItemClicked?.dataset?.uri;
    if (uri) {
      const componentToRender = routes[uri];
      if (!componentToRender)
        Navigate('/error');
      else {
        componentToRender();
        window.history.pushState({}, '', uri);
      }
    }
  });
}

// Function to handle history change events
function onHistoryChange() {
  window.addEventListener('popstate', (event) => {
    const uri = window.location.pathname;
    const componentToRender = routes[uri];
    componentToRender(event.state);
  });
}

// Function to handle frontend load events
function onFrontendLoad() {
  window.addEventListener('load', async () => {
    const uri = window.location.pathname;
    const componentToRender = routes[uri];
    if (!componentToRender)
      return Navigate('/error');
    if (!await verifyToken()){
      Navbar();
      Navigate('/login');
    }
    Navbar();
    return componentToRender();
  });
}

export default Router;