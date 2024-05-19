import {clearPage, renderBreadcrumb, displayToast} from "../../../utils/render";
import {
    getToken,
    isAuthenticated
} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";

let contact;
const AcceptRefusePage = (data) => {
    if (!isAuthenticated()) {
        Navigate('/login');
    }
    else if(data === undefined) {
        Navigate('/contact');
    }
    else {
        contact = data;
        clearPage();
        document.title = "Accepter ou refuser un contact";
        renderBreadcrumb({"Accueil": "/", "Contacts": "/contact", "Accepter ou refuser un contact": "/contact/refusal"})
        buildPage();
    }
}

function buildPage() {
    const main = document.querySelector('main');
    const mainDiv = document.createElement('div');
    mainDiv.style.display = 'flex';
    const leftDiv = document.createElement('div');
    leftDiv.style.width = '50%';
    leftDiv.appendChild(getContactInfos());
    const rightDiv = document.createElement('div');
    rightDiv.style.width = '50%';
    const title = document.createElement('h3');
    title.textContent = 'Accepter ou refuser un contact';
    title.style.textAlign = 'center';
    main.appendChild(title)
    rightDiv.appendChild(getForm());
    mainDiv.appendChild(leftDiv);
    mainDiv.appendChild(rightDiv);
    main.appendChild(mainDiv);
}

// Display contact informations
function getContactInfos() {
    const contactInfosDiv = document.createElement('div');
    contactInfosDiv.className = 'p-5';
    const entrepriseName = document.createElement('label');
    entrepriseName.textContent = 'Entreprise';
    contactInfosDiv.appendChild(entrepriseName);
    const entrepriseNameValue = document.createElement('input');
    entrepriseNameValue.type = 'text';
    entrepriseNameValue.value = contact.company.tradeName;
    entrepriseNameValue.readOnly = true;
    entrepriseNameValue.className = 'form-control';
    entrepriseNameValue.disabled = true;
    contactInfosDiv.appendChild(entrepriseNameValue);
    if(contact.company.designation){
        const entrepriseDesignation = document.createElement('label');
        entrepriseDesignation.textContent = 'Appellation';
        const entrepriseDesignationValue = document.createElement('input');
        entrepriseDesignationValue.type = 'text';
        entrepriseDesignationValue.value = contact.company.designation;
        entrepriseDesignationValue.readOnly = true;
        entrepriseDesignationValue.className = 'form-control';
        entrepriseDesignationValue.disabled = true;
        contactInfosDiv.appendChild(entrepriseDesignation);
        contactInfosDiv.appendChild(entrepriseDesignationValue);
    }
    const contactMeetPlace = document.createElement('label');
    contactMeetPlace.textContent = 'Lieu de rencontre';
    contactInfosDiv.appendChild(contactMeetPlace);
    const entrepriseMeetPlaceValue = document.createElement('input');
    entrepriseMeetPlaceValue.type = 'text';
    entrepriseMeetPlaceValue.value = contact.meetPlace;
    entrepriseMeetPlaceValue.readOnly = true;
    entrepriseMeetPlaceValue.className = 'form-control';
    entrepriseMeetPlaceValue.disabled = true;
    contactInfosDiv.appendChild(entrepriseMeetPlaceValue);
    return contactInfosDiv;
}

// Display form to accept or refuse contact
function getForm() {
    const contactState = document.createElement('label');
    contactState.textContent = 'Etat';
    const contactStateValue = document.createElement('select');
    contactStateValue.className = 'form-select';
    contactStateValue.style.marginBottom = '10px';
    const optionAccepted = document.createElement('option');
    optionAccepted.value = 'true';
    optionAccepted.textContent = 'Accepté';
    const optionRefused = document.createElement('option');
    optionRefused.value = 'false';
    optionRefused.textContent = 'Refusé';
    contactStateValue.appendChild(optionAccepted);
    contactStateValue.appendChild(optionRefused);
    const refusalReason = document.createElement('label');
    refusalReason.hidden = true;
    refusalReason.textContent = 'Raison du refus';
    const refusalReasonValue = document.createElement('textarea');
    refusalReasonValue.placeholder = 'Entrer la raison du refus';
    refusalReasonValue.hidden = true;
    refusalReasonValue.required = !refusalReasonValue.hidden;
    refusalReasonValue.className = 'form-control';
    refusalReasonValue.rows = 5;
    contactStateValue.addEventListener('change', (event) => {
        refusalReason.hidden = event.target.value !== 'false';
        refusalReasonValue.hidden = event.target.value !== 'false';
        refusalReasonValue.required = !refusalReasonValue.hidden;
    });
    const submit = document.createElement('input');
    submit.type = 'submit';
    submit.value = 'Enregistrer';
    submit.className = 'btn btn-primary mt-2 right';
    const form = document.createElement('form');
    form.className = 'p-5';
    form.addEventListener('submit', onSubmit);
    form.appendChild(contactState);
    form.appendChild(contactStateValue);
    form.appendChild(refusalReason);
    form.appendChild(refusalReasonValue);
    form.appendChild(submit);
    return form;
}

async function onSubmit(event) {
    event.preventDefault();
    const contactState = document.querySelector('select').value;
    const refusalReason = document.querySelector('textarea').value;
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': getToken(),
        },
        body: JSON.stringify({
            refusalReason,
        }),
    };
    if (contactState === 'true') {
        Navigate('/stage/add', contact);
    } else {
        fetch(`http://localhost:3000/contacts/${contact.idContact}/refuse`, options)
        .then(request => {
            if (request.status === 401 || request.status === 403 || request.status === 500) {
                const requestError = request.text();
                displayToast(requestError, 'danger');
            } else {
                Navigate('/contact');
            }
        });
    }
}

export default AcceptRefusePage;