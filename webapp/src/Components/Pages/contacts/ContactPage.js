import {clearPage, displayToast, renderBreadcrumb} from "../../../utils/render";
import {
  getToken,
  isAuthenticated
} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";

const ContactPage = async () => {
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    clearPage();
    document.title = "Contacts";
    renderBreadcrumb({"Accueil": "/", "Contacts": "/contact"})
    await buildPage();
  }
}

function followContact(contact, stateCell) {
  return async () => {
    console.log("enter_fetch", contact.idContact);
    const response = await fetch(
        `http://localhost:3000/contacts/${contact.idContact}/follow`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': getToken()
          }
        });
    if (response.status === 200) {
      document.getElementById(`followButton${contact.idContact}`).hidden = true;
      document.getElementById(`unfollowButton${contact.idContact}`).hidden = false;
      stateCell.textContent = 'initié';
    } else {
      displayToast(await response.text(), 'danger');
    }
  };
}

function unfollowContact(contact, stateCell) {
  return async () => {
    console.log("enter_fetch", contact.idContact);
    const response = await fetch(
        `http://localhost:3000/contacts/${contact.idContact}/unfollow`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': getToken()
          }
        });
    if (response.status === 200) {
      document.getElementById(`followButton${contact.idContact}`).hidden = false;
      document.getElementById(`unfollowButton${contact.idContact}`).hidden = true;
      stateCell.textContent = 'non suivi';
    } else {
      displayToast(await response.text(), 'danger');
    }
  };
}

async function buildPage() {
  const main = document.querySelector('main');
  const title = document.createElement('h3');
  const containerDiv = document.createElement('div');
  title.textContent = 'Contacts';
  title.style.textAlign = 'center';
  const table = document.createElement('table');
  table.className = 'table table-bordered';
  table.style.width = '80%';
  table.style.tableLayout = 'auto';
  const tableHead = document.createElement('thead');
  tableHead.classList.add('thead-dark');
  const tableHeadRow = document.createElement('tr');
  const headings = ['Entreprise', 'État'];
  const contacts = await getContacts();
  const hasAccepted = contacts.some(contact => contact.state === 'accepté');
  if(!hasAccepted) table.classList.add('table-hover');
  const hasRefused = contacts.some(contact => contact.state === 'refusé');
  const hasAdmitted = contacts.some(contact => contact.state === 'pris');

  if (hasAccepted || hasAdmitted || hasRefused) {
    headings.splice(2, 0, 'Lieu de rencontre');
  }

  if (hasRefused) {
    headings.splice(3, 0, 'Raison du refus');
  }

  headings.forEach(headingText => {
    const th = document.createElement('th');
    th.textContent = headingText;
    tableHeadRow.appendChild(th);
  });

  tableHead.appendChild(tableHeadRow);
  table.appendChild(tableHead);

  const tableBody = document.createElement('tbody');

  contacts.forEach(contact => {
    const row = document.createElement('tr');

    // row company
    const companyCell = document.createElement('td');
    companyCell.textContent = contact.company.tradeName;
    if (contact.company.designation !== null) {
      companyCell.textContent += `,  ${contact.company.designation}`;
    }

    if(contact.state === 'accepté') {
      companyCell.classList.add('clickable-row');
      companyCell.addEventListener('click', (event) => {
        event.preventDefault();
        Navigate('/stage');
      });
    }
    else if (contact.state === 'initié') {
      companyCell.classList.add('clickable-row');
      companyCell.addEventListener('click', (event) => {
        event.preventDefault();
        Navigate('/contact/meet', contact);
      });
      companyCell.addEventListener('mouseover', () => {
        companyCell.style.backgroundColor = 'lightgrey';
      });
      companyCell.addEventListener('mouseout', () => {
        companyCell.style.backgroundColor = '';
      });
    }
    else if (contact.state === 'pris') {
      companyCell.classList.add('clickable-row');
      companyCell.addEventListener('click', (event) => {
        event.preventDefault();
        Navigate('/contact/refusal', contact);
      });
      companyCell.addEventListener('mouseover', () => {
        companyCell.style.backgroundColor = 'lightgrey';
      });
      companyCell.addEventListener('mouseout', () => {
        companyCell.style.backgroundColor = '';
      });
    }
    row.appendChild(companyCell);

    // row state
    const stateCell = document.createElement('td');
    stateCell.textContent = contact.state;
    row.appendChild(stateCell);

    // row meetPlace
    const meetPlaceCell = document.createElement('td');
    if (contact.state === 'initié' && !contacts.some(c => c.state !== 'initié')) {
      meetPlaceCell.hidden = true;
    } else if (contact.meetPlace){
      meetPlaceCell.textContent = contact.meetPlace;
    } else {
      meetPlaceCell.textContent = '/';
    }
    row.appendChild(meetPlaceCell);

    // row refusal
    const refusalCell = document.createElement('td');
    if (contact.state === 'refusé') {
      refusalCell.textContent = contact.refusalReason;
    } else if (hasRefused) {
      refusalCell.textContent = '/';
    } else {
      refusalCell.hidden = true;
    }

    row.appendChild(refusalCell);

    // row follow/unfollow
    if(contacts.find(c => c.state === 'accepté') === undefined
        && (contact.state === 'non suivi'
        || contact.state === 'initié'
        || contact.state === 'accepté'
        || contact.state === 'pris')) {
      const followCell = document.createElement('td');
      const followButton = document.createElement('button');
      const unfollowButton = document.createElement('button');
      followButton.classList.add('btn', 'btn-primary');
      followButton.textContent = 'Suivre';
      followButton.id = `followButton${contact.idContact}`;
      followButton.addEventListener('click', followContact(contact, stateCell));
      unfollowButton.classList.add('btn', 'btn-primary');
      unfollowButton.textContent = 'Ne plus suivre';
      unfollowButton.id = `unfollowButton${contact.idContact}`;
      unfollowButton.addEventListener('click', unfollowContact(contact, stateCell));
      if (contact.state === 'non suivi') {
        followButton.hidden = false;
        unfollowButton.hidden = true;
      } else {
        followButton.hidden = true;
        unfollowButton.hidden = false;
      }
      followCell.appendChild(followButton);
      followCell.appendChild(unfollowButton);
      row.appendChild(followCell);
    }

    tableBody.appendChild(row);
  });
  table.appendChild(tableBody);
  main.appendChild(title);
  const addContactButton = document.createElement('button');
  addContactButton.textContent = 'Ajouter un contact';
  addContactButton.className = 'btn btn-primary';
  addContactButton.style.marginLeft = '10%';
  addContactButton.style.marginBottom = '2%';
  addContactButton.style.width = '25%';
  if(contacts.find(contact => contact.state === 'accepté')) {
    addContactButton.disabled = true;
    addContactButton.className = 'btn greybutton';
    addContactButton.innerHTML += `
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-lock" viewBox="0 0 16 16">
      <path d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2m3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2M5 8h6a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V9a1 1 0 0 1 1-1"/>
    </svg>
    `;
  }
  addContactButton.addEventListener('click', () => {
    Navigate('/contact/add');
  });
  main.appendChild(addContactButton);
  containerDiv.appendChild(table);
  main.appendChild(containerDiv);

  if (contacts.length === 0) {
    noContacts();
  }
}

function noContacts() {
  const main = document.querySelector('main');
  const title = document.createElement('h3');
  title.textContent = 'Aucun contact';
  title.style.textAlign = 'center';
  title.style.fontSize = '3rem';
  title.style.marginTop = '100px';
  main.appendChild(title);
}

async function getContacts() {
  const response = await fetch('http://localhost:3000/contacts', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    }
  });
  return response.json();
}

export default ContactPage;