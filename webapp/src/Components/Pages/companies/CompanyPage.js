import {getToken, isAuthenticated} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";
import {clearPage, renderBreadcrumb, displayToast} from "../../../utils/render";

const CompanyPage = (data) => {
  if (!isAuthenticated()) {
    Navigate('/login');
  } else if (!data) {
    Navigate('/dashboard');
  } else {
    clearPage();
    document.title = `${data.tradeName}`;
    document.title += data.designation ? ` - ${data.designation}` : '';
    renderBreadcrumb({"Accueil": "/", "Statistiques": "/dashboard", [data.designation ? `${data.tradeName} - ${data.designation}` : `${data.tradeName}`]: "/company"})
    buildPage(data);
  }
}

function buildPage(data) {
  const main = document.querySelector('main');
  const mainDiv = document.createElement('div');
  mainDiv.style.width = '100%';
  const infosDiv = document.createElement('div');
  infosDiv.style.display = 'flex';
  infosDiv.style.justifyContent = 'center';
  const companyInfos = document.createElement('div');
  companyInfos.id = 'companyInfos';
  companyInfos.className = 'card';
  companyInfos.style.width = '40%';
  companyInfos.style.border = '2px solid black';
  const blacklisted = document.createElement('div');
  blacklisted.id = 'blacklisted';
  blacklisted.className = 'card';
  blacklisted.style.width = '40%';
  blacklisted.style.border = '2px solid black';
  infosDiv.appendChild(companyInfos);
  infosDiv.appendChild(blacklisted);
  const contactsDiv = document.createElement('div');
  infosDiv.id = 'infosDiv';
  contactsDiv.id = 'contacts';
  mainDiv.appendChild(infosDiv);
  mainDiv.appendChild(contactsDiv);
  mainDiv.style.height = '80vh';
  main.appendChild(mainDiv);
  displayCompanyInfos(data);
  displayContacts(data.contacts);
}

function displayCompanyInfos(company) {
  const companyDiv = document.getElementById('companyInfos');
  companyDiv.innerHTML = '';

  const title = document.createElement('h3');
  title.textContent = 'Informations de l\'entreprise';
  title.style.textAlign = 'center';
  companyDiv.appendChild(title);

  const tradeName = document.createElement('h5');
  tradeName.textContent = company.tradeName;
  tradeName.style.textAlign = 'center';
  companyDiv.appendChild(tradeName);

  const designation = document.createElement('h5');
  designation.textContent = company.designation;
  designation.style.textAlign = 'center';
  companyDiv.appendChild(designation);

  const email = document.createElement('p');
  email.textContent = company.email;
  email.style.textAlign = 'center';
  companyDiv.appendChild(email);

  const phone = document.createElement('p');
  phone.textContent = company.phoneNumber;
  phone.style.textAlign = 'center';
  companyDiv.appendChild(phone);

  const address = document.createElement('p');
  address.textContent = company.address;
  address.style.textAlign = 'center';
  companyDiv.appendChild(address);

  const city = document.createElement('p');
  city.textContent = company.city;
  city.style.textAlign = 'center';
  companyDiv.appendChild(city);

  const blacklistedDiv = document.getElementById('blacklisted');
  blacklistedDiv.innerHTML = '';
  if(company.blacklisted) {
    const h2 = document.createElement('h3');
    h2.textContent = 'Entreprise black-listée';
    h2.style.textAlign = 'center';
    blacklistedDiv.appendChild(h2);
    const reason = document.createElement('p');
    reason.textContent = company.blacklistMotivation;
    reason.style.textAlign = 'center';
    blacklistedDiv.appendChild(reason);
  } else {
    const h2 = document.createElement('h3');
    h2.textContent = 'Black-lister cette entreprise';
    h2.style.textAlign = 'center';
    blacklistedDiv.appendChild(h2);
    const button = document.createElement('button');
    button.className = 'btn btn-primary';
    button.id = 'blacklist';
    button.textContent = 'Black-lister';
    button.style.margin = 'auto';
    button.style.display = 'block';
    button.style.marginTop = '20px';
    blacklistedDiv.appendChild(button);
    const motivation = document.createElement('textarea');
    motivation.className = 'form-control';
    motivation.id = 'motivation';
    motivation.placeholder = 'Motivation';
    motivation.style.margin = 'auto';
    motivation.style.display = 'block';
    motivation.style.marginTop = '10px';
    motivation.style.width = '70%';
    motivation.style.height = '100px';
    motivation.hidden = true;
    blacklistedDiv.appendChild(motivation);

    const saveButton = document.createElement('button');
    saveButton.className = 'btn btn-primary';
    saveButton.id = 'saveButton';
    saveButton.textContent = 'Sauvegarder';
    saveButton.style.margin = 'auto';
    saveButton.style.marginLeft = '15%'
    saveButton.hidden = true;

    const cancelButton = document.createElement('button');
    cancelButton.className = 'btn btn-secondary ms-2';
    cancelButton.textContent = 'Annuler';
    cancelButton.style.margin = 'auto';
    cancelButton.id = 'cancelButton';
    cancelButton.hidden = true;

    const buttonDiv = document.createElement('div');
    buttonDiv.style.marginTop = '10px';
    buttonDiv.style.marginBottom = '10px';
    blacklistedDiv.appendChild(buttonDiv);
    buttonDiv.appendChild(saveButton);
    buttonDiv.appendChild(cancelButton);

    button.addEventListener('click', () => {
      motivation.hidden = !motivation.hidden;
      button.hidden = !button.hidden;
      saveButton.hidden = !saveButton.hidden;
      cancelButton.hidden = !cancelButton.hidden;
    });
    saveButton.addEventListener('click', () => {
      blacklistCompany(company.idCompany);
    });
    cancelButton.addEventListener('click', () => {
      motivation.hidden = !motivation.hidden;
      button.hidden = !button.hidden;
      saveButton.hidden = !saveButton.hidden;
      cancelButton.hidden = !cancelButton.hidden;
    });
  }
}

function displayContacts(contacts) {
  const div = document.getElementById('contacts');
  div.style.marginTop = '20px';
  div.innerHTML = '';
  div.style.overflow = 'auto';
  div.style.height = '50vh';
  div.style.scrollBehavior = 'smooth';
  const table = document.createElement('table');
  table.className = 'table table-bordered';
  table.style.width = '80%';
  table.style.margin = 'auto';
  const thead = document.createElement('thead');
  thead.style.position = 'sticky';
  thead.style.top = '0';
  const tbody = document.createElement('tbody');
  const tr = document.createElement('tr');
  const th1 = document.createElement('th');
  th1.textContent = 'Etudiant';
  const th2 = document.createElement('th');
  th2.textContent = 'Statut';
  const th3 = document.createElement('th');
  th3.textContent = 'Lieu de rencontre';
  const th4 = document.createElement('th');
  th4.textContent = 'Raison de refus';
  thead.appendChild(tr);
  tr.appendChild(th1);
  tr.appendChild(th2);
  tr.appendChild(th3);
  tr.appendChild(th4);
  table.appendChild(thead);
  table.appendChild(tbody);
  document.getElementById('contacts').appendChild(table);
  contacts.forEach(contact => {
    const trow = document.createElement('tr');
    const td1 = document.createElement('td');
    td1.textContent = `${contact.student.firstname} ${contact.student.lastname}`;
    const td2 = document.createElement('td');
    td2.textContent = contact.state;
    const td3 = document.createElement('td');
    td3.textContent = contact.meetPlace;
    const td4 = document.createElement('td');
    td4.textContent = contact.refusalReason;
    trow.appendChild(td1);
    trow.appendChild(td2);
    trow.appendChild(td3);
    trow.appendChild(td4);
    tbody.appendChild(trow);
  });
}

async function blacklistCompany(idCompany) {
  const response = await fetch(`http://localhost:3000/companies/${idCompany}/blacklist`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    },
    body: JSON.stringify({
      reason: document.getElementById('motivation').value
    })
  });

  if(response.status !== 200) {
    const responseError = await response.text();
    displayToast(responseError, 'danger');
    document.getElementById('motivation').hidden = true;
    document.getElementById('blacklist').addEventListener('click', () => {
      const motivation = document.getElementById('motivation');
      motivation.hidden = !motivation.hidden;
    });
  } else {
    document.getElementById('motivation').hidden = true;
    document.getElementById('blacklist').hidden = true;
    document.getElementById('saveButton').hidden = true;
    document.getElementById('cancelButton').hidden = true;
    const data = await response.json();
    displayCompanyInfos(data.company)
    displayContacts(data.contacts)
  }
}

export default CompanyPage;