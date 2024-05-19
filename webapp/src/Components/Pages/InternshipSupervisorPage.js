import {getToken, isAuthenticated} from "../../utils/auths";
import Navigate from "../Router/Navigate";
import {clearPage, renderBreadcrumb} from "../../utils/render";

const InternshipSupervisorPage = async () => {
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    clearPage();
    renderBreadcrumb({"Accueil": "/", "Responsabes": "/internship-supervisor"})
    await renderInternshipSupervisorPage();
    document.title = "Stage";
  }
}

async function renderInternshipSupervisorPage() {
  const internshipSupervisor = await getAllInternshipSupervisor();
  const mainElement = document.querySelector('main');
  const divElement = document.createElement('div');
  divElement.className = 'd-flex flex-row justify-content-around align-items-center';
  divElement.style.overflow = 'auto';
  divElement.style.height = '80vh'

  // Create table
  const table = document.createElement('table');
  table.className = 'table table-bordered';

  // Create table body
  const tbody = document.createElement('tbody');

  // Create table header
  const thead = document.createElement('thead');
  thead.style.position = 'sticky';
  thead.style.top = '0';
  const headerRow = document.createElement('tr');
  ['Nom ▼', 'Prénom ▼', 'Email', 'Numéro de téléphone', 'Nom de l\'entreprise ▼'].forEach((text, index) => {
    const th = document.createElement('th');
    th.textContent = text;
    th.addEventListener('click', () => {
      if (index === 0) { // If the column is Nom
        internshipSupervisor.sort((a, b) => {
          const aValue = a.lastName;
          const bValue = b.lastName;
          return aValue.localeCompare(bValue);
        });
      } else if (index === 1) { // If the column is Prénom
        internshipSupervisor.sort((a, b) => {
          const aValue = a.firstName;
          const bValue = b.firstName;
          return aValue.localeCompare(bValue);
        });
      } else if (index === 4) { // If the column is Nom de l'entreprise
        internshipSupervisor.sort((a, b) => {
          const aValue = a.company.tradeName;
          const bValue = b.company.tradeName;
          return aValue.localeCompare(bValue);
        });
      }
      // Clear the table body
      while (tbody.firstChild) {
        tbody.removeChild(tbody.firstChild);
      }
      // Re-render the table body with the sorted data
      renderTableBody(internshipSupervisor, tbody);
    });
    headerRow.appendChild(th);
  });
  thead.appendChild(headerRow);
  table.appendChild(thead);

  // Render table body
  renderTableBody(internshipSupervisor, tbody);
  table.appendChild(tbody);

  divElement.appendChild(table);
  mainElement.appendChild(divElement);
}

function renderTableBody(data, tbody) {
  data.forEach(supervisorData => {
    const row = document.createElement('tr');
    [supervisorData.lastName, supervisorData.firstName, supervisorData.email,
    supervisorData.phoneNumber, supervisorData.company.tradeName].forEach((text, index) => {
      const td = document.createElement('td');
      if (index === 2) { // If the text is an email
        const mailtoLink = document.createElement('a');
        mailtoLink.href = `mailto:${text}`;
        mailtoLink.textContent = text;
        td.appendChild(mailtoLink);
      } else {
        td.textContent = text;
      }
      row.appendChild(td);
    });
    tbody.appendChild(row);
  });
}

async function getAllInternshipSupervisor() {
  const response = await fetch('http://localhost:3000/internshipSupervisors', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    }
  });
  return response.json();
}

export default InternshipSupervisorPage;