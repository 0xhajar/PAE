import {clearPage, displayToast, renderBreadcrumb} from "../../../utils/render";
import {
  getToken, isAuthenticated
} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";

let companyList = [];

const AddContactPage = async () => {
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    clearPage();
    document.title = "Ajouter un contact";
    renderBreadcrumb({"Accueil": "/", "Contacts": "/contact", "Ajouter un contact": "/contact/add"})
    companyList = await getCompanies();
    buildPage();
  }
}

function addCompaniesOptions() {
  const companies = document.querySelector('#companySelect');
  companies.innerHTML = '';
  const defaultOption = document.createElement('option');
  defaultOption.text = 'Choisissez votre entreprise';
  defaultOption.value = 'default';
  companies.appendChild(defaultOption);

  const companyNames = [...new Set(companyList.map(e => e.tradeName))]; // Suppression des doublons
  companyNames.forEach(name => {
    const option = document.createElement('option');
    option.value = name;
    option.text = name;
    option.style.color = companyList.find(c => c.tradeName === name).blacklisted
        ? 'red' : 'black';
    companies.appendChild(option);
  });
}

function buildPage() {
  const main = document.querySelector('main');
  const containerDiv = document.createElement('div');
  containerDiv.className = 'container';
  const rowDiv = document.createElement('div');
  rowDiv.className = 'row';
  const colDiv1 = document.createElement('div');
  colDiv1.className = 'col-6';
  const colDiv2 = document.createElement('div');
  colDiv2.className = 'col-6';
  const title = document.createElement('h3');
  title.textContent = 'Ajouter un nouveau contact';
  title.style.textAlign = 'center';
  title.style.marginBottom = '5%';
  main.appendChild(title);
  rowDiv.appendChild(colDiv1);
  containerDiv.appendChild(rowDiv);
  main.appendChild(containerDiv);

  
  

  const company = document.createElement('label');
  company.textContent = 'Entreprise';
  company.style.marginLeft = '15%';
  colDiv1.appendChild(company);

  const companies = document.createElement('select');
  companies.className = 'form-control';
  companies.style.width = '50%';
  companies.style.marginLeft = '15%';
  companies.id = 'companySelect';

  colDiv1.appendChild(companies);

  addCompaniesOptions();

  const designation = document.createElement('label');
  designation.textContent = 'Appellation';
  designation.style.marginLeft = '15%';
  colDiv1.appendChild(designation);

  const designations = document.createElement('select');
  designations.id = 'designation';
  designations.className = 'form-control';
  designations.style.width = '50%';
  designations.style.marginLeft = '15%';
  designations.style.marginBottom = '10px';

  colDiv1.appendChild(designations);

  companies.addEventListener('change', (e) => {
    const alert = document.querySelector('#alert');
    if (alert.hidden === false) {
      alert.hidden = true;
    }
    while (designations.firstChild) {
      designations.removeChild(designations.firstChild);
    }

    const selectedCompany = companyList.find(
        (c) => c.tradeName === e.target.value);
    if(selectedCompany.blacklisted) {
      alert.hidden = false;
      alert.textContent = 'Cette entreprise est blacklistée';
      return;
    }

    if (e.target.value === 'default') {
      const defaultOptionDesignation = document.createElement('option');
      defaultOptionDesignation.text = "Choisissez d'abord votre entreprise";
      defaultOptionDesignation.value = 'default';
      designations.appendChild(defaultOptionDesignation);
    } else {
      const designationList = companyList.filter((c) => c.tradeName === e.target.value).sort((a, b) => {
        if (a.designation === null) {
          return -1;
        }
        if (b.designation === null) {
          return 1;
        }
        return a.designation.localeCompare(b.designation);
      });
      designationList.forEach(name => {
        if (name.designation === null) {
          const option = document.createElement('option');
          option.value = 'Aucune appellation'
          option.text = 'Aucune appellation';
          designations.appendChild(option);
          return;
        }
        const option = document.createElement('option');
        option.value = name.designation;
        option.text = name.designation;
        designations.appendChild(option);
      });
    }
  });

  const alert = document.createElement('p');
  alert.id = 'alert';
  alert.className = 'alert alert-danger';
  alert.style.marginLeft = '15%';
  alert.style.width = '50%';
  alert.hidden = true;
  colDiv1.appendChild(alert);

  const submit = document.createElement('input');
  submit.value = 'Enregistrer';
  submit.type = 'submit';
  submit.className = 'btn btn-primary';
  submit.style.marginBottom = '25%';
  submit.style.marginLeft = '15%';
  submit.style.width = '50%';
  colDiv1.appendChild(submit);

  submit.addEventListener('click', onSubmit);

  const createCompanyButton = document.createElement('button');
  createCompanyButton.textContent = 'Ajouter une entreprise non répertoriée';
  createCompanyButton.className = 'btn btn-secondary';
  createCompanyButton.id = 'createCompanyButton';
  colDiv2.style.marginBottom = '50%';
  colDiv2.appendChild(createCompanyButton);
  rowDiv.appendChild(colDiv2);

  createCompanyButton.addEventListener('click', () => {
    createCompanyButton.hidden = !createCompanyButton.hidden;
      if (document.getElementById('companyForm')) {
      return;
      }
      const form = document.createElement('form');
      form.id = 'companyForm';

      const nameLabel = document.createElement('label');
      nameLabel.textContent = 'Nom';
      form.appendChild(nameLabel);
      const nameInput = document.createElement('input');
      nameInput.type = 'text';
      nameInput.className = 'form-control';
      nameInput.required = true;
      form.appendChild(nameInput);

      const addressLabel = document.createElement('label');
      addressLabel.textContent = 'Adresse';
      form.appendChild(addressLabel);
      const addressInput = document.createElement('input');
      addressInput.type = 'text';
      addressInput.className = 'form-control';
      addressInput.required = true;
      form.appendChild(addressInput);

      const cityLabel = document.createElement('label');
      cityLabel.textContent = 'Ville (optionnel)';
      form.appendChild(cityLabel);
      const cityInput = document.createElement('input');
      cityInput.type = 'text';
      cityInput.className = 'form-control';
      cityInput.required = false;
      form.appendChild(cityInput);

      const phoneLabel = document.createElement('label');
      phoneLabel.textContent = 'Numero de téléphone (optionnel)';
      form.appendChild(phoneLabel);
      const phoneInput = document.createElement('input');
      phoneInput.type = 'text';
      phoneInput.className = 'form-control';
      phoneInput.required = false;
      form.appendChild(phoneInput);

      const emailLabel = document.createElement('label');
      emailLabel.textContent = 'Email (optionnel)';
      form.appendChild(emailLabel);
      const emailInput = document.createElement('input');
      emailInput.type = 'text';
      emailInput.className = 'form-control';
      emailInput.required = false;
      form.appendChild(emailInput);

      const appellationLabel = document.createElement('label');
      appellationLabel.textContent = 'Appellation (optionnel)';
      form.appendChild(appellationLabel);
      const appellationInput = document.createElement('input');
      appellationInput.type = 'text';
      appellationInput.className = 'form-control';
      appellationInput.required = false;
      appellationInput.style.marginBottom = '10px';
      form.appendChild(appellationInput);

      const submitButton = document.createElement('input');
      submitButton.type = 'submit';
      submitButton.value = "Ajouter l'entreprise";
      submitButton.className = 'btn btn-primary';
      submitButton.style.marginRight = '10px';
      form.appendChild(submitButton);

      const cancelButton = document.createElement('button');
      cancelButton.textContent = 'Annuler';
      cancelButton.className = 'btn btn-secondary';
      form.appendChild(cancelButton);
      cancelButton.addEventListener('click', () => {
        createCompanyButton.hidden = !createCompanyButton.hidden;
        form.remove();
      });

      colDiv2.appendChild(form);
      containerDiv.appendChild(rowDiv);
      main.appendChild(containerDiv);

      submitButton.addEventListener('click', createSubmit);
  });
}
// fetch function to get all entreprises
async function getCompanies() {
  const response = await fetch('http://localhost:3000/companies', {
    method: 'GET', headers: {
      'Content-Type': 'application/json', 'Authorization': getToken(),
    },
  });
  if (response.status === 200) {
    return response.json();
  }
  displayToast(await response.text(), 'error');
  return undefined;
}

async function createSubmit(e) {
  e.preventDefault();
  const tradeName = document.querySelector('input[type="text"]').value;
  const address = document.querySelectorAll('input[type="text"]')[1].value;
  let city = document.querySelectorAll('input[type="text"]')[2].value;
  let phoneNumber = document.querySelectorAll('input[type="text"]')[3].value;
  let email = document.querySelectorAll('input[type="text"]')[4].value;
  let designation = document.querySelectorAll('input[type="text"]')[5].value;
  const alert = document.querySelector('#alert');
  if (tradeName === '' || address === '') {
    alert.hidden = false;
    alert.textContent = 'Veuillez remplir les champs obligatoires';
    return;
  }
  if (designation === '') {
    designation = null;
  }
  if (city === '') {
    city = null;
  }
  if (phoneNumber === '') {
    phoneNumber = null;
  }
  if (email === '') {
    email = null;
  }
  const options = {
    method: 'POST',
    body: JSON.stringify({
          tradeName,
          designation,
          address,
          city,
          phoneNumber,
          email
    }),
    headers: {
      'Content-Type': 'application/json', 'Authorization': getToken(),
    },
  };
  const response = await fetch('http://localhost:3000/companies', options);
  if (response.status === 200) {
    companyList.push(await response.json());
    addCompaniesOptions();
    document.querySelector('#companySelect').value = tradeName;

    const optionDesignation = document.createElement('option');
    if(designation !== null) {
      optionDesignation.value = designation;
      optionDesignation.text = designation;
      document.querySelector('#designation').value = designation;
    } else {
      optionDesignation.value = 'Aucune appellation';
      optionDesignation.text = 'Aucune appellation';
      document.querySelector('#designation').value = 'Aucune appellation';
    }
    document.querySelector('#designation').appendChild(optionDesignation);
    document.querySelector('form').remove();
    document.querySelector('#createCompanyButton').hidden = !document.querySelector('#createCompanyButton').hidden;
    alert.hidden = true;
  } else {
    displayToast(await response.text(), 'error');
  }
}
// function to submit the form
async function onSubmit(e) {
  e.preventDefault();
  const company = document.querySelector('#companySelect').value;
  const designation = document.querySelector('#designation').value;
  const alert = document.querySelector('#alert');
  if (company === 'default' || designation === 'default') {
    alert.hidden = false;
    alert.textContent = 'Veuillez sélectionner une entreprise et une appellation';
    return;
  }
  if(companyList.find(c => c.tradeName === company).blacklisted) {
    alert.hidden = false;
    alert.textContent = 'Cette entreprise est blacklistée';
    return;
  }

  const companyFound = designation === 'Aucune appellation'
      ? companyList.find((c) => c.tradeName === company && c.designation === null)
      : companyList.find((c) => c.tradeName === company && c.designation === designation);

  if(!companyFound) {
    alert.hidden = false;
    alert.textContent = 'Entreprise ou appellation introuvable';
    return;
  }

  alert.hidden = true;

  const options = {
    method: 'POST',
    body: JSON.stringify({
      idCompany: companyFound.idCompany
    }),
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken(),
    },
  };

  const response = await fetch('http://localhost:3000/contacts', options);
  if (response.status === 200) {
    Navigate('/contact');

  } else {
    displayToast(await response.text(), 'danger');
  }
}

export default AddContactPage;

