import { getRememberMe, isAuthenticated, setAuthenticatedUser, setRememberMe, setToken } from '../../utils/auths';
import {
  clearPage,
  renderPageTitle,
  renderBreadcrumb,
  displayToast
} from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';

// Define LoginPage component
const LoginPage = () => {
  if (isAuthenticated()) {
    Navigate('/');
  } else {
    clearPage();
    renderPageTitle('Connexion');
    document.title = "Connexion";
    renderBreadcrumb({"Connexion": "/login"});
    renderLoginForm();
  }
};

// Function to render the login form
function renderLoginForm() {
  const main = document.querySelector('main');
  const form = document.createElement('form');
  form.className = 'p-5';
  const titleEmail = document.createElement('h6');
  titleEmail.textContent = 'Email';
  const username = document.createElement('input');
  username.type = 'text';
  username.id = 'username';
  username.className = 'form-control mb-3';
  const emailError = document.createElement('p');
  emailError.id = 'emailError';
  emailError.className = 'text-danger';
  emailError.hidden = true;
  const titlePassword = document.createElement('h6');
  titlePassword.textContent = 'Mot de passe';
  const passwordDiv = document.createElement('div');
  const passwordHtml = `
    <div class="input-group">
      <input id="password" type="password" class="form-control mb-3" name="password">
      <button class="btn btn-outline-secondary mb-3 greybutton" type="button" id="hidePassword">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye-slash" viewBox="0 0 16 16">
          <path
            d="M13.359 11.238C15.06 9.72 16 8 16 8s-3-5.5-8-5.5a7.028 7.028 0 0 0-2.79.588l.77.771A5.944 5.944 0 0 1 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.134 13.134 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755-.165.165-.337.328-.517.486z" />
          <path
            d="M11.297 9.176a3.5 3.5 0 0 0-4.474-4.474l.823.823a2.5 2.5 0 0 1 2.829 2.829zm-2.943 1.299.822.822a3.5 3.5 0 0 1-4.474-4.474l.823.823a2.5 2.5 0 0 0 2.829 2.829" />
          <path
            d="M3.35 5.47c-.18.16-.353.322-.518.487A13.134 13.134 0 0 0 1.172 8l.195.288c.335.48.83 1.12 1.465 1.755C4.121 11.332 5.881 12.5 8 12.5c.716 0 1.39-.133 2.02-.36l.77.772A7.029 7.029 0 0 1 8 13.5C3 13.5 0 8 0 8s.939-1.721 2.641-3.238l.708.709zm10.296 8.884-12-12 .708-.708 12 12-.708.708" />
        </svg>
      </button>
    </div>
  `;
  passwordDiv.appendChild(titlePassword);
  passwordDiv.innerHTML += passwordHtml;
  const passwordError = document.createElement('p');
  passwordError.id = 'passwordError';
  passwordError.className = 'text-danger';
  passwordError.hidden = true;
  const submit = document.createElement('input');
  submit.value = 'Se connecter';
  submit.type = 'submit';
  submit.className = 'btn btn-primary';

  const formCheckWrapper = document.createElement('div');
  formCheckWrapper.className = 'mb-3 form-check';

  const rememberme = document.createElement('input');
  rememberme.type = 'checkbox';
  rememberme.className = 'form-check-input';
  rememberme.id = 'rememberme';
  const remembered = getRememberMe();
  rememberme.checked = remembered;
  rememberme.addEventListener('click', onCheckboxClicked);

  const checkLabel = document.createElement('label');
  checkLabel.htmlFor = 'rememberme';
  checkLabel.className = 'form-check-label';
  checkLabel.textContent = 'Se souvenir de moi';

  const register = document.createElement('p');
  register.textContent = 'Pas encore de compte ? ';
  register.className = 'd-block text-center mt-3';
  const link = document.createElement('a');
  link.textContent = 'Inscrivez-vous';
  link.href = '/register';

  formCheckWrapper.appendChild(rememberme);
  formCheckWrapper.appendChild(checkLabel);

  form.appendChild(titleEmail);
  form.appendChild(username);
  form.appendChild(emailError);
  form.appendChild(passwordDiv);
  form.appendChild(passwordError);
  form.appendChild(formCheckWrapper);
  form.appendChild(submit);
  main.appendChild(form);
  const hidePassword = document.querySelector('#hidePassword');
  hidePassword.addEventListener('click', () => {
    const password = document.querySelector('#password');
    if (password.type === 'password') {
      password.type = 'text';
    } else {
      password.type = 'password';
    }
  });
  main.appendChild(register);
  register.appendChild(link);
  form.addEventListener('submit', onLogin);
}

// Function to handle checkbox click event
function onCheckboxClicked(e) {
  setRememberMe(e.target.checked);
}

// Async function to handle form submission
async function onLogin(e) {
  e.preventDefault();

  const email = document.querySelector('#username').value;
  const password = document.querySelector('#password').value;

  const emailError = document.querySelector('#emailError');
  const passwordError = document.querySelector('#passwordError');
  if(email.length === 0) {
    emailError.textContent = 'Email requis';
    emailError.hidden = false;
  } else {
    emailError.hidden = true;
  }
  if(password.length === 0) {
    passwordError.textContent = 'Mot de passe requis';
    passwordError.hidden = false;
  } else {
    passwordError.hidden = true;
  }
  if(passwordError.hidden && emailError.hidden) {
    const options = {
      method: 'POST',
      body: JSON.stringify({
        email,
        password,
      }),
      headers: {
        'Content-Type': 'application/json',
      },
    };

    // verify if email is valid
    const emailRegex = /^[a-zA-Z0-9._%+-]+\.[a-zA-Z0-9._%+-]+@(vinci\.be|student\.vinci\.be)$/;
    if (!emailRegex.test(email)) {
      // clear the last error message
      const form1 = document.querySelector('form');
      const lastError = form1.querySelector('#error');
      if (lastError) {
        form1.removeChild(lastError);
      }
      emailError.textContent = 'Email invalide';
      emailError.hidden = false;
      return;
    }

    const response = await fetch(`http://localhost:3000/auths/login`, options);

    if (response.status === 401) {
      // clear the last error message
      const form1 = document.querySelector('form');
      const lastError = form1.querySelector('#error');
      if (lastError) {
        form1.removeChild(lastError);
      }
      const erreur = document.createElement('p');
      erreur.id = 'error';
      erreur.textContent = 'Email ou mot de passe invalide';
      erreur.className = 'text-danger';
      const form = document.querySelector('form');
      form.appendChild(erreur);
      return;
    }

    if (!response.ok) displayToast(await response.type, 'error');

    // Get the authenticated user
    const authenticatedUser = await response.json();

    setToken(authenticatedUser.token);
    setAuthenticatedUser(authenticatedUser.user);

    // Navigate to the home page if the user is authenticated, otherwise navigate to the login page
    if (authenticatedUser) {
      Navbar();
      Navigate('/');
    } else {
      Navigate('/login');
    }
  }
}

export default LoginPage;