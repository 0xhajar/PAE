import {
  getToken,
  isAuthenticated
} from '../../../utils/auths';
import {
  clearPage,
  displayToast,
  renderBreadcrumb,
  renderPageTitle
} from '../../../utils/render';
import Navigate from '../../Router/Navigate';

let contact;
const MeetContactPage = (data) => {
  if (!isAuthenticated()) {
    Navigate('/login');
  }
  else if (data === undefined) {
    Navigate('/contact');
  }
  else {
    contact = data;
    clearPage();
    document.title = "Rencontrer un contact";
    renderBreadcrumb({"Accueil": "/", "Contacts": "/contacts", "Rencontrer un contact": "/contact/meet"})
    renderPageTitle('Rencontre avec un contact');
    renderMeetContactPage();
  }
};

function renderMeetContactPage() {
  const main = document.querySelector('main');
  main.innerHTML += `
      <form class="container mt-5">
        <div class="mb-3">
          <label for="entreprise" class="form-label">Entreprise</label>
          <input type="text" class="form-control" id="entreprise" name="entreprise" value="${contact.company.tradeName}" readonly>
        </div>
        <div class="mb-3" id="appelationDiv">
          <label for="appellation" class="form-label">Appellation</label>
          <input type="text" class="form-control" id="appellation" name="appellation" readonly>
        </div>
        <div class="mb-3">
          <label for="lieu" class="form-label">Lieu de rencontre</label>
            <select name="lieu" id="lieu">
              <option value="Dans l'entreprise">Dans l'entreprise</option>
              <option value="A distance">A distance</option>
           </select>
        </div>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
      </form>
    `;
    const form = document.querySelector('form');
    form.addEventListener('submit', submitFunc);
    const appelationDiv = document.getElementById('appelationDiv');
    if (contact.company.designation) {
      document.getElementById('appellation').value = contact.company.designation;
    } else {
      appelationDiv.style.display = 'none';
    }
}

async function submitFunc (event) {
  event.preventDefault();
  const form = event.target;
  const formData = new FormData(form);
  const lieu = formData.get('lieu');
  console.log(lieu)
  try {
    const response = await fetch(`http://localhost:3000/contacts/${contact.idContact}/meet`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: getToken(),
      },
      body: JSON.stringify({ meetPlace: lieu }),
    });
    if (response.status === 200) {
      Navigate('/contact');
    } else {
      displayToast(await response.text(), 'danger');
    }
  } catch (error) {
    console.error(error);
  }
}

export default MeetContactPage;
