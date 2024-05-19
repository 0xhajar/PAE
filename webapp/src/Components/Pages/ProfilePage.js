import {clearPage, renderBreadcrumb, displayToast} from "../../utils/render";
import {getAuthenticatedUser, isAuthenticated, getToken, setAuthenticatedUser} from "../../utils/auths";
import Navigate from "../Router/Navigate";
import defaultImage from "../../img/default-user-image.png";

const ProfilePage = async () => {
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    clearPage();
    document.title = "Profil";
    renderBreadcrumb({"Accueil": "/", "Profil": "/profile"})
    buildPage();
  }
}

function buildPage() {
  const main = document.querySelector('main');
  const mainDiv = document.createElement('div');
  mainDiv.style.display = 'flex';
  mainDiv.style.justifyContent = 'center';
  mainDiv.id = 'mainDiv';
  const leftDiv = document.createElement('div');
  leftDiv.id = 'leftDiv';
  const rightDiv = document.createElement('div');
  rightDiv.id = 'rightDiv';
  rightDiv.style.marginLeft = '20px';
  mainDiv.appendChild(leftDiv);
  mainDiv.appendChild(rightDiv);
  main.appendChild(mainDiv);
  renderPictureInfos()
  renderProfilPage();
}

function renderPictureInfos() {
  const div = document.querySelector('#leftDiv');
  div.innerHTML = '';

  const user = getAuthenticatedUser();

  const img = document.createElement('img');
  img.src = user.profilePicture
      ? `data:image/png;base64, ${user.profilePicture}` : `${defaultImage}`;
  img.alt = 'image de profil';
  img.id = 'profilePic';
  img.className = 'img-thumbnail';
  img.style.width = '240px';
  div.className = 'form-control bg-light rounded shadow';
  div.appendChild(img);
  div.style.width = '200px';

  const modifyButton = document.createElement('button');
  modifyButton.textContent = 'Modifier';
  modifyButton.addEventListener('click', displayImageInput);
  modifyButton.className = 'btn btn-success mt-2';
  modifyButton.id = 'modifyPic';
  modifyButton.style.marginRight = '10px';

  const removeButton = document.createElement('button');
  removeButton.textContent = 'Supprimer';
  removeButton.addEventListener('click', removeProfilePicture);
  removeButton.className = 'btn btn-danger mt-2';
  removeButton.id = 'removePic';
  removeButton.style.marginRight = '10px';

  img.addEventListener('mouseover', () => {
    img.style.cursor = 'pointer';
    img.style.opacity = '0.5';
    img.style.transition = 'opacity 0.5s';
  });
  img.addEventListener('mouseout', () => {
    img.style.opacity = '1';
  });
  img.addEventListener('click', () => {
    if (!document.querySelector('#profilePicInput')) {
      const options = document.createElement('div');
      options.id = 'options';
      options.className = 'btn-group';
      options.style.display = 'flex';
      options.appendChild(modifyButton);
      options.appendChild(removeButton);
      div.appendChild(options);
      div.style.width = '300px';
    }
  });
}

function displayImageInput() {
  const div = document.querySelector('#leftDiv');
  const options = document.querySelector('#options')
  div.removeChild(options);

  const imageInput = document.createElement('input');
  imageInput.type = 'file'
  imageInput.accept = '.png, .jpg, .jpeg';
  imageInput.id = 'profilePicInput';
  imageInput.className = 'form-control mt-2';

  const imageInputError = document.createElement('p');
  imageInputError.className = 'alert alert-danger';
  imageInputError.hidden = true;
  imageInputError.id = 'imageInputError';

  const img = document.querySelector('#profilePic');

  imageInput.addEventListener('input', (event) => {
    const {target} = event
    if (target.files && target.files[0]) {
      // TODO set max size in pixels
      const maxAllowedSize = 5 * 1024 * 1024;
      if (target.files[0].size > maxAllowedSize) {
        imageInputError.hidden = false;
        imageInputError.textContent = 'Image trop grande, max 5Mb !';
      } else {
        imageInputError.hidden = true;
        const reader = new FileReader();

        reader.onloadend = () => {
          img.src = reader.result;
        }

        if (target.files[0]) {
          reader.readAsDataURL(target.files[0]);
        } else {
          img.src = "";
        }
      }
    }
  });

  div.appendChild(imageInput);
  div.appendChild(imageInputError);

  const saveButton = document.createElement('button');
  saveButton.textContent = 'Enregistrer';
  saveButton.className = 'btn btn-success';
  saveButton.addEventListener('click', modifyProfilePicture);
  saveButton.id = 'savePic';
  saveButton.style.marginTop = '10px';
  document.querySelector('#leftDiv').appendChild(saveButton);

  const cancelButton = document.createElement('button');
  cancelButton.textContent = 'Annuler';
  cancelButton.className = 'btn btn-secondary ms-2';
  cancelButton.style.marginTop = '10px';
  document.querySelector('#leftDiv').appendChild(cancelButton);
  cancelButton.addEventListener('click', () => {
    div.removeChild(imageInput);
    div.removeChild(imageInputError);
    div.removeChild(saveButton);
    div.removeChild(cancelButton);
    div.style.width = '200px';
    const user = getAuthenticatedUser();
    img.src = user.profilePicture
        ? `data:image/png;base64, ${user.profilePicture}` : `${defaultImage}`;
    ;
  });
}

async function modifyProfilePicture() {
  console.log('modify pic')
  const file = document.querySelector('#profilePicInput').files[0];
  if (!file) {
    const error = document.querySelector('#imageInputError');
    error.hidden = false;
    error.textContent = 'Veuillez sélectionner une image';
    return;
  }
  const formData = new FormData();
  formData.append('file', file);
  const response = await fetch('http://localhost:3000/users/picture/modify', {
    method: 'POST',
    headers: {
      Authorization: getToken()
    },
    body: formData
  });
  if(response.status !== 200) {
    displayToast(await response.text(), 'danger')
  } else {
    const data = await response.json();
    setAuthenticatedUser(data);
    renderPictureInfos();
  }
}

async function removeProfilePicture() {
  console.log('remove pic')
  const response = await fetch('http://localhost:3000/users/picture/remove', {
    method: 'POST',
    headers: {
      Authorization: getToken()
    }
  });
  if(response.status !== 200) {
    displayToast(await response.text(), 'danger')
  } else {
    const data = await response.json();
    setAuthenticatedUser(data);
    renderPictureInfos();
  }
}

function renderProfilPage() {
  const rightDiv = document.querySelector('#rightDiv');
  const form = document.createElement('form');
  const authenticatedUser = getAuthenticatedUser();
  form.className = 'form-control bg-light rounded shadow';
  const fields = ['lastname', 'firstname', 'email', 'phoneNumber'];
  const labels = ['Nom', 'Prénom', 'Email', 'Numéro de téléphone'];

  fields.forEach((field, index) => {
    const title = document.createElement('h6');
    title.textContent = labels[index];
    title.className = 'fw-bold mb-1';

    const input = document.createElement('input');
    input.id = field;
    input.value = `${authenticatedUser[field]}`;
    input.className = 'form-control mb-3';

    const popup = document.createElement('div');
    popup.id = 'popup';
    popup.style.display = 'none';
    document.body.appendChild(popup);

    input.addEventListener('input', () => {
      const existingSaveButton = document.querySelector('#sauver');
      const existingCancelButton = document.querySelector('#annuler');
      if (!existingSaveButton) {
        const div = document.createElement('div');
        div.style.display = 'flex';
        const sauver = document.createElement('button');
        sauver.textContent = 'Sauver';
        sauver.id = 'sauver';
        sauver.className = 'btn btn-success';
        sauver.addEventListener('click', onSaveProfile);
        div.appendChild(sauver);
        const annuler = document.createElement('button');
        annuler.textContent = 'Annuler';
        annuler.className = 'btn btn-secondary ms-2';
        annuler.id = 'annuler';
        annuler.addEventListener('click', (e) => {
          e.preventDefault()
          sauver.style.display = 'none';
          annuler.style.display = 'none';
          input.value = `${authenticatedUser[field]}`;
        });
        div.appendChild(annuler);
        form.appendChild(div);
      } else {
        existingSaveButton.style.display = 'block';
        existingCancelButton.style.display = 'block';
      }
    });
    form.appendChild(title);
    form.appendChild(input);
  });

  const changePasswordButton = document.createElement('button');
  changePasswordButton.textContent = 'Modifier votre mot de passe';
  changePasswordButton.className = 'btn btn-primary';
  changePasswordButton.id = 'changePasswordButton';
  form.appendChild(changePasswordButton);
  rightDiv.appendChild(form);

  changePasswordButton.addEventListener('click', event => {
    event.preventDefault();

    changePasswordButton.style.display = 'none';
    const passwordDiv = document.createElement('div');
    passwordDiv.id = 'passwordDiv';
    document.querySelector('#mainDiv').appendChild(passwordDiv);
    const passwordLabel = document.createElement('label');
    passwordLabel.textContent = 'Modification du mot de passe';
    passwordLabel.className = 'fw-bold mb-1';
    passwordDiv.appendChild(passwordLabel);
    passwordDiv.className = 'form-control bg-light rounded shadow';
    passwordDiv.style.width = '400px';
    passwordDiv.style.marginLeft = '20px';

    const passwordFields = {
      oldPassword: "Ancien mot de passe",
      newPassword: "Nouveau mot de passe",
      confirmPassword: "Confirmer le nouveau mot de passe"
    };

    Object.entries(passwordFields).forEach(([key, value]) => {
      const div = document.createElement('div');
      const passwordHtml = `
        <div class="input-group">
          <input id="${key}" type="password" placeholder="${value}" class="form-control mb-3" required>
          <button class="btn btn-outline-secondary mb-3 greybutton" type="button" id="${key}Hide">
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
      div.innerHTML += passwordHtml;
      passwordDiv.appendChild(div);
      const button = document.querySelector(`#${key}Hide`);
      button.addEventListener('click', () => {
        const password = document.querySelector(`#${key}`);
        if (password.type === 'password') {
          password.type = 'text';
        } else {
          password.type = 'password';
        }
      });
    });

    const submitButton = document.createElement('button');
    submitButton.textContent = 'Sauver';
    submitButton.className = 'btn btn-primary';
    submitButton.id = 'submitPassword';
    submitButton.addEventListener('click', onSavePassword);
    passwordDiv.appendChild(submitButton);

    const cancelButton = document.createElement('button');
    cancelButton.textContent = 'Annuler';
    cancelButton.id = 'annuler';
    cancelButton.className = 'btn btn-secondary ms-2';
    cancelButton.addEventListener('click', () => {
      passwordDiv.remove();
      changePasswordButton.style.display = 'block';
    });
    passwordDiv.appendChild(cancelButton);
  });

}

async function onSaveProfile(e) {
  e.preventDefault();
  const firstname = document.querySelector('#firstname').value;
  const lastname = document.querySelector('#lastname').value;
  const email = document.querySelector('#email').value.toLowerCase();
  const phoneNumber = document.querySelector('#phoneNumber').value;

  const options = {
    method: 'PUT',
    body: JSON.stringify({
      firstname,
      lastname,
      email,
      phoneNumber,

    }),
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    },
  };

  const response = await fetch('http://localhost:3000/users/update', options);

  if (response.status !== 200) {
    displayToast(await response.text(), 'danger');
  }

  const sauver = document.querySelector('#sauver');
  sauver.style.display = 'none';
  const annuler = document.querySelector('#annuler');
  annuler.style.display = 'none';
}

async function onSavePassword(e) {
  e.preventDefault();
  const oldPassword = document.querySelector('#oldPassword').value;
  const newPassword = document.querySelector('#newPassword').value;
  const confirmationPassword = document.querySelector(
      '#confirmPassword').value;

  const options = {
    method: 'POST',
    body: JSON.stringify({
      oldPassword,
      newPassword,
      confirmationPassword,
    }),
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    },
  };

  const response = await fetch('http://localhost:3000/users/changepassword',
      options);

  if (response.status !== 204) {
    displayToast(await response.text(), 'danger');
  }
  if (response.status === 204) {
    displayToast('Mot de passe modifié avec succès', 'success');
    const passwordDiv = document.querySelector('#passwordDiv');
    passwordDiv.style.display = 'none';
    // Show the change password button
    const changePasswordButton = document.querySelector(
        '#changePasswordButton');
    changePasswordButton.style.display = 'block';
  }
}

export default ProfilePage;