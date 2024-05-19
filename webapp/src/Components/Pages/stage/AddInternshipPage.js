import {clearPage, displayToast} from "../../../utils/render";
import {isAuthenticated, getToken} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";

let contact;
const InternshipPage = (data) => {
    if (!isAuthenticated()) {
      Navigate('/login');
    }
    else if(data === undefined) {
      Navigate('/contact');
    }
    else {
      contact = data;
      clearPage();
      renderInternshipPage();
      document.title = "Ajouter un Stage";
    }
  }

async function renderInternshipPage() {
    const main = document.querySelector('main');
    const title = document.createElement('h3');
    title.textContent = 'Ajouter un stage';
    title.style.textAlign = 'center';
    main.appendChild(title);
    const mainDiv = document.createElement('div');
    mainDiv.style.display = 'flex';
    const leftDiv = document.createElement('div');
    leftDiv.id = 'leftDiv';
    leftDiv.style.width = '50%';
    leftDiv.appendChild(getInternshipInfos());
    const rightDiv = document.createElement('div');
    rightDiv.style.width = '50%';
    rightDiv.appendChild(await getSupervisorInfos());
    rightDiv.appendChild(addNewSupervisor());
    const date = document.createElement('label');
    date.textContent = 'Date de signature';
    date.style.marginTop = '5%';
    rightDiv.appendChild(date);
    const dateInput = document.createElement('input');
    dateInput.type = 'date';
    dateInput.className = 'form-control';
    dateInput.style.width = '50%';
    dateInput.required = true;
    rightDiv.appendChild(dateInput);
    const subjectDiv = document.createElement('div');
    subjectDiv.style.marginTop = '5%';
    const subjectLabel = document.createElement('label');
    subjectLabel.textContent = 'Sujet du stage (s’il est déjà mentionné dans le document de modalités)';
    subjectDiv.appendChild(subjectLabel);
    const subjectInput = document.createElement('input');
    subjectInput.id = 'subject';
    subjectInput.type = 'text';
    subjectInput.className = 'form-control';
    subjectInput.required = false;
    subjectInput.style.width = '50%';
    subjectDiv.appendChild(subjectInput);
    rightDiv.appendChild(subjectDiv);
    const submitButton = document.createElement('button');
    submitButton.textContent = 'Enregistrer';
    submitButton.className = 'btn btn-primary';
    submitButton.style.width = '50%';
    submitButton.style.marginTop = '5%';
    submitButton.addEventListener('click', (e) => {
        e.preventDefault();
        let subjectInternship = document.querySelector('#subject').value;
        if(subjectInternship === ''){ 
            subjectInternship = null;
        }
        const internship = {
            idCompany: contact.idCompany,
            idContact: contact.idContact,
            internshipProject: subjectInternship,
            idInternshipSupervisor: document.querySelector('select').value,
            signatureDate: dateInput.value,
        };
        addInternship(internship);
    });
    rightDiv.appendChild(submitButton);
    mainDiv.appendChild(leftDiv);
    mainDiv.appendChild(rightDiv);
    main.appendChild(mainDiv);
}

function getInternshipInfos() {
    const contactInfosDiv = document.createElement('div');
    contactInfosDiv.className = 'p-5';
    const entrepriseName = document.createElement('label');
    entrepriseName.textContent = 'Entreprise';
    contactInfosDiv.appendChild(entrepriseName);
    const entrepriseNameValue = document.createElement('input');
    entrepriseNameValue.type = 'text';
    entrepriseNameValue.value = contact.company.tradeName;
    entrepriseNameValue.readOnly = true;
    entrepriseNameValue.className = 'bg-info form-control';
    contactInfosDiv.appendChild(entrepriseNameValue);
    if(contact.designation !== 'null'){
        const entrepriseDesignation = document.createElement('label');
        entrepriseDesignation.textContent = 'Appellation';
        const entrepriseDesignationValue = document.createElement('input');
        entrepriseDesignationValue.type = 'text';
        entrepriseDesignationValue.value = contact.designation;
        entrepriseDesignationValue.readOnly = true;
        entrepriseDesignationValue.className = 'bg-info form-control';
        contactInfosDiv.appendChild(entrepriseDesignation);
        contactInfosDiv.appendChild(entrepriseDesignationValue);
    }
    return contactInfosDiv;
}

async function getSupervisorInfos() {
    const supervisorInfosDiv = document.createElement('div');
    const supervisorLabel = document.createElement('label');
    supervisorLabel.textContent = 'Veuillez selectionner un responsable de stage';
    supervisorLabel.style.marginTop = '5%';
    supervisorLabel.style.marginBottom = '1%';
    supervisorInfosDiv.appendChild(supervisorLabel);
    const supervisorSelect = document.createElement('select');
    supervisorSelect.className = 'form-control';
    supervisorSelect.style.width = '50%';
    const supervisorList = await getSupervisors();
    if(supervisorList.length === 0){
        const supervisorOption = document.createElement('option');
        supervisorOption.text = 'Aucun responsable de stage trouvé';
        supervisorSelect.appendChild(supervisorOption);
    } else {
        supervisorList.forEach(supervisor => {
            const supervisorOption = document.createElement('option');
            supervisorOption.text = `${supervisor.firstName} ${supervisor.lastName}`;
            supervisorOption.value = supervisor.idInternshipSupervisor;
            supervisorSelect.appendChild(supervisorOption);
        });
    }

    
    supervisorInfosDiv.appendChild(supervisorSelect);
    return supervisorInfosDiv;
}

async function getSupervisors(){
  const supervisors = await fetch(`http://localhost:3000/internshipSupervisors/company/${contact.idCompany}`, {
      method: 'GET',
      headers: {
          'Content-Type': 'application/json',
          'Authorization': getToken(),
      },
  });
  if (supervisors.status === 200) {
      return supervisors.json();
  }
  return undefined;
}

function addNewSupervisor() {
    const addSupervisorDiv = document.createElement('div');
    addSupervisorDiv.style.marginTop = '5%';
    const addSupervisorLabel = document.createElement('label');
    addSupervisorLabel.textContent = 'Vous ne trouvez pas le responsable de stage que vous cherchez ?';
    addSupervisorLabel.style.marginBottom = '1%';
    addSupervisorDiv.appendChild(addSupervisorLabel);
    const addSupervisorButton = document.createElement('button');
    addSupervisorButton.textContent = 'Ajouter un nouveau responsable de stage';
    addSupervisorButton.className = 'btn btn-primary';
    addSupervisorButton.style.width = '50%';
    addSupervisorButton.addEventListener('click', () => {
        if (document.querySelector('#addSupervisorForm') === null){
        addNewSupervisorForm();
        }
    });
    addSupervisorDiv.appendChild(addSupervisorButton);
    return addSupervisorDiv;
}

function addNewSupervisorForm() {
    const leftDiv = document.querySelector('#leftDiv');
    const form = document.createElement('form');
    form.className = 'p-5';
    form.id = 'addSupervisorForm';
    const firstNameLabel = document.createElement('label');
    firstNameLabel.textContent = 'Prénom';
    form.appendChild(firstNameLabel);
    const firstNameInput = document.createElement('input');
    firstNameInput.type = 'text';
    firstNameInput.className = 'form-control';
    firstNameInput.required = true;
    form.appendChild(firstNameInput);
    const lastNameLabel = document.createElement('label');
    lastNameLabel.textContent = 'Nom';
    form.appendChild(lastNameLabel);
    const lastNameInput = document.createElement('input');
    lastNameInput.type = 'text';
    lastNameInput.className = 'form-control';
    lastNameInput.required = true;
    form.appendChild(lastNameInput);
    const emailLabel = document.createElement('label');
    emailLabel.textContent = 'Email (facultatif)';
    form.appendChild(emailLabel);
    const emailInput = document.createElement('input');
    emailInput.type = 'email';
    emailInput.className = 'form-control';
    form.appendChild(emailInput);
    const phoneLabel = document.createElement('label');
    phoneLabel.textContent = 'Téléphone';
    form.appendChild(phoneLabel);
    const phoneInput = document.createElement('input');
    phoneInput.type = 'text';
    phoneInput.className = 'form-control';
    phoneInput.required = true;
    form.appendChild(phoneInput);
    const submitButton = document.createElement('button');
    submitButton.textContent = 'Enregistrer';
    submitButton.className = 'btn btn-primary';
    submitButton.style.width = '45%';
    submitButton.style.marginTop = '5%';
    submitButton.style.marginRight = '5%';

    submitButton.addEventListener('click', (e) => {
        e.preventDefault();
        let emailValue = document.querySelector('input[type="email"]').value;
        if(emailValue === ''){ 
            emailValue = null;
        }
        const supervisor = {
            firstName: firstNameInput.value,
            lastName: lastNameInput.value,
            email: emailValue,
            phoneNumber: phoneInput.value,
            idCompany: contact.idCompany,
        };
        addSupervisor(supervisor);
    });
    form.appendChild(submitButton);
    const cancelButton = document.createElement('button');
    cancelButton.textContent = 'Annuler';
    cancelButton.className = 'btn btn-secondary';
    cancelButton.style.width = '45%';
    cancelButton.style.marginTop = '5%';
    cancelButton.addEventListener('click', () => {
        leftDiv.removeChild(form);
    });
    form.appendChild(cancelButton);
    leftDiv.appendChild(form);
}

async function addSupervisor(supervisor) {
    const response = await fetch('http://localhost:3000/internshipSupervisors', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': getToken(),
        },
        body: JSON.stringify(supervisor),
    });
    if (response.status === 200) {
        InternshipPage();
    } else {
        displayToast('Erreur lors de l\'ajout du responsable de stage', 'danger');
    }
}

async function addInternship(internship) {
    const response = await fetch('http://localhost:3000/internships', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': getToken(),
        },
        body: JSON.stringify(internship),
    });
    if (response.status === 200) {
        Navigate('/stage');
    } else {
        displayToast(await response.text(), 'danger');
    }
}


export default InternshipPage;