const STORE_NAME = 'token';
const REMEMBER_ME = 'remembered';

let currentUser;

const getAuthenticatedUser = () => {
  if (currentUser !== undefined) return currentUser;

  return undefined;
};

const setAuthenticatedUser = (authenticatedUser) => {
  currentUser = authenticatedUser;
};

const isAuthenticated = () => currentUser !== undefined;

const clearAuthenticatedUser = () => {
  localStorage.clear();
  sessionStorage.clear();
  currentUser = undefined;
};

function getRememberMe() {
  const rememberedSerialized = localStorage.getItem(REMEMBER_ME);
  const remembered = JSON.parse(rememberedSerialized);
  return remembered;
}

function setRememberMe(remembered) {
  const rememberedSerialized = JSON.stringify(remembered);
  localStorage.setItem(REMEMBER_ME, rememberedSerialized);
}

function setToken(token) {
  const remembered = getRememberMe();
  if (remembered) localStorage.setItem(STORE_NAME, token);
  else sessionStorage.setItem(STORE_NAME, token);
}

function getToken() {
  const remembered = getRememberMe();
  if (remembered) return localStorage.getItem(STORE_NAME);
  return sessionStorage.getItem(STORE_NAME);
}

const verifyToken = async () => {
  const token = getToken();
  if(!isAuthenticated() && !token) return false;
  const response = await fetch('http://localhost:3000/auths/user', {
    method:'GET',
    headers: {
      'Authorization': token,
    },
  });
  if(response.status === 401) {
    clearAuthenticatedUser();
    return false;
  }
  const user = await response.json();
  setAuthenticatedUser(user);
  return true;
};

export {
  getAuthenticatedUser,
  setAuthenticatedUser,
  isAuthenticated,
  clearAuthenticatedUser,
  getRememberMe,
  setRememberMe,
  verifyToken,
  getToken,
  setToken,
};
