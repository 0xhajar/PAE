import Chart from 'chart.js/auto';
import {getToken, isAuthenticated} from "../../utils/auths";
import Navigate from "../Router/Navigate";
import {clearPage, displayToast, renderBreadcrumb} from "../../utils/render";

let companies = [];
let filteredCompanies;
const lastOrder = {};

const DashboardPage = async () => {
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    clearPage();
    document.title = "Statistiques";
    renderBreadcrumb({"Accueil": "/", "Statistiques": "/dashboard"});
    await buildPage();
  }
}

async function buildPage() {
  const main = document.querySelector('main');

  const mainDiv = document.createElement('div');
  mainDiv.style.display = 'flex';

  const statsDiv = document.createElement('div');
  statsDiv.style.width = '30%';
  statsDiv.id = 'stats';

  const rightDiv = document.createElement('div');
  rightDiv.style.width = '70%';
  rightDiv.id = 'rightDiv';

  const companiesDiv = document.createElement('div');
  companiesDiv.id = 'companies';
  companiesDiv.style.maxHeight = '70vh';

  const searchDiv = document.createElement('div');
  searchDiv.id = 'searchDiv';
  searchDiv.style.display = 'flex';

  rightDiv.appendChild(companiesDiv);
  rightDiv.appendChild(searchDiv);
  mainDiv.appendChild(statsDiv);
  mainDiv.appendChild(rightDiv);
  main.appendChild(mainDiv);

  companies = await getCompanies();

  renderStats();
  await renderGraph(getCurrentAcademicYear());
  renderCompanies();
  renderSearch();
}

function renderStats() {
  const div = document.getElementById('stats');
  const academicYears = getAcademicYears();
  const select = document.createElement('select');
  select.id = 'academicYear';
  select.className = 'form-select';
  select.style.width = '50%';
  select.style.margin = '10px auto';
  select.style.display = 'block';
  select.style.fontSize = '1.2em';
  select.style.borderRadius = '5px';
  select.style.padding = '5px';
  select.style.cursor = 'pointer';
  select.style.textAlign = 'center';
  academicYears.forEach(academicYear => {
    const option = document.createElement('option');
    option.value = academicYear;
    option.textContent = academicYear;
    select.appendChild(option);
  });
  const option = document.createElement('option');
  option.value = 'all';
  option.textContent = 'Toutes les années';
  select.appendChild(option);
  select.addEventListener('change', async (e) => {
    renderCompanies();
    await renderGraph(e.target.value);
  });
  div.appendChild(select);
}

async function renderGraph(academicYear) {
  const graph = document.getElementById('graph');
  if(graph) {
    graph.remove();
  }
  const div = document.getElementById('stats');
  const graphDiv = document.createElement('div');
  graphDiv.id = 'graph';

  // nb of students by academic year
  const response = await fetch('http://localhost:3000/users/students', {
    method: 'GET',
    headers: {
      Authorization: getToken(),
    }
  });
  const result = await response.json();

  // nb of students for all academic years or for a specific academic year
  let nbStudents = 0;
  if(academicYear === 'all') {
    nbStudents = result.length;
  }
  else {
    const years = {};
    result.forEach(student => {
      const year = getAcademicYearFromRegisterDate(student.registerDate);
      if(years[year] === undefined) {
        years[year] = 1;
      }
      else {
        years[year] += 1;
      }
    });
    const stillSearching = result.filter(student =>
      getAcademicYearFromRegisterDate(student.registerDate) !== student.academicYear
    )
    nbStudents = years[academicYear] || stillSearching.length;
  }

  const nbStudentsWithInternship = getNbStudentsWithIntership(academicYear);
  const nbStudentsWithoutInternship = nbStudents - nbStudentsWithInternship;
  console.log(nbStudents, nbStudentsWithInternship, nbStudentsWithoutInternship)

  async function showGraph() {
    const canvas = document.createElement('canvas');
    new Chart(canvas, {
      type: 'pie',
      data: {
        labels: ['Ont un stage', 'Pas de stage'],
        datasets: [{
          label: `Nombre d'étudiants`,
          data: [
            nbStudentsWithInternship,
            nbStudentsWithoutInternship
          ],
          backgroundColor: [
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)'
          ],
        }]
      }
    });
    canvas.style.width = '60%';
    canvas.style.height = '60%';
    canvas.style.margin = '10px auto';
    graphDiv.appendChild(canvas);

    const p = document.createElement('p');
    p.textContent = `Nombre d'étudiants : ${nbStudents}`;
    p.style.textAlign = 'center';
    graphDiv.appendChild(p);
    div.appendChild(graphDiv);
  }

  await showGraph();
}

function getAcademicYearFromRegisterDate(registerDate) {
  const date = new Date(registerDate);
  const month = date.getMonth();
  const year = date.getFullYear();
  if(month+1 >= 9) {
    return `${year}-${year + 1}`;
  }
  return `${year - 1}-${year}`;
}

function fillTable() {
  const tbody = document.querySelector('tbody');
  tbody.innerHTML = '';
  const academicYear = document.querySelector('select').value;
  const tableContent = filteredCompanies !== undefined ? filteredCompanies : companies;
  tableContent.forEach(company => {
    const trow = document.createElement('tr');
    const td1 = document.createElement('td');
    const td2 = document.createElement('td');
    const td3 = document.createElement('td');
    const td4 = document.createElement('td');
    const td5 = document.createElement('td');
    const td6 = document.createElement('td');
    td1.textContent = company.tradeName;
    td2.textContent = company.designation;
    td3.textContent = company.phoneNumber;
    td4.textContent = company.city;
    td5.textContent = getNbStudentsWithIntershipForCompany(company.idCompany,
        academicYear);
    td6.textContent = company.blacklisted ? "oui" : "non";
    trow.appendChild(td1);
    trow.appendChild(td2);
    trow.appendChild(td3);
    trow.appendChild(td4);
    trow.appendChild(td5);
    trow.appendChild(td6);
    tbody.appendChild(trow);
    trow.style.cursor = 'pointer';
    trow.addEventListener('click', () => {
      Navigate(`/company`, company);
    });
  });
}

function renderCompanies() {
  const div = document.getElementById('companies');
  div.style.overflow = 'auto';
  div.style.height = '80vh';
  div.style.scrollBehavior = 'smooth'
  div.innerHTML = '';

  const table = document.createElement('table');
  table.className = 'table table-bordered table-hover';
  table.style.width = '80%';
  table.style.maxHeight = '40vh';

  const thead = document.createElement('thead');
  thead.style.position = 'sticky';
  thead.style.top = '0';
  const tr = document.createElement('tr');
  const th1 = document.createElement('th');
  const th2 = document.createElement('th');
  const th3 = document.createElement('th');
  const th4 = document.createElement('th');
  const th5 = document.createElement('th');
  const th6 = document.createElement('th');
  th1.textContent = 'Nom ▼';
  th1.style.cursor = 'pointer';
  th1.addEventListener('click', orderByTradename);
  th2.textContent = 'Appellation ▼';
  th2.style.cursor = 'pointer';
  th2.addEventListener('click', orderByDesignation);
  th3.textContent = 'Téléphone';
  th4.textContent = 'Ville';
  th4.style.cursor = 'pointer';
  th4.addEventListener('click', orderByCity);
  th5.textContent = 'Nombre d\'étudiants';
  th5.style.cursor = 'pointer';
  th5.addEventListener('click', orderByAcceptedStudents);
  th6.textContent = 'Black-listée';
  th6.style.cursor = 'pointer';
  th6.addEventListener('click', orderByBlacklisted);
  thead.appendChild(tr);
  tr.appendChild(th1);
  tr.appendChild(th2);
  tr.appendChild(th3);
  tr.appendChild(th4);
  tr.appendChild(th5);
  tr.appendChild(th6);
  table.appendChild(thead);

  const tbody = document.createElement('tbody');
  table.appendChild(tbody);
  div.appendChild(table);
  const error = document.createElement('p');
  error.textContent = 'Aucune entreprise trouvée';
  error.hidden = true;
  error.className = 'alert alert-danger';
  error.id = 'companies-error';
  error.style.textAlign = 'center';
  error.style.width = '30%';
  error.style.height = '7%';
  error.style.margin = '10px auto';
  error.style.display = 'block';
  error.style.fontSize = '1.2em';
  error.style.borderRadius = '5px';
  error.style.padding = '5px';
  div.appendChild(error);

  if(filteredCompanies !== undefined && filteredCompanies.length === 0 || companies.length === 0) {
    error.hidden = false;
  }
  else if(filteredCompanies) {
    fillTable(filteredCompanies);
  }
  else {
    companies.sort((a, b) => {
      const tradeNameComparison = a.tradeName.localeCompare(b.tradeName);
      if(tradeNameComparison === 0) return a.designation ? a.designation.localeCompare(b.designation) : 0;
      return tradeNameComparison;
    });
    lastOrder.tradeName = 'asc';
    fillTable(companies);
  }
}

function renderSearch() {
  const div = document.querySelector('#searchDiv');
  const search = document.createElement('input');
  search.type = 'text';
  search.placeholder = 'Rechercher une entreprise';
  search.className = 'form-control';
  search.style.width = '45%';
  search.style.margin = '10px auto';
  search.style.display = 'block';
  search.style.fontSize = '1.2em';
  search.style.borderRadius = '5px';
  search.style.padding = '5px';
  search.style.cursor = 'pointer';
  search.style.textAlign = 'center';
  div.appendChild(search);
  search.addEventListener('input', () => {
    if(search.value === '')
      filteredCompanies = undefined;
    else
      filteredCompanies = companies.filter(company => company.tradeName.toLowerCase().includes(search.value.toLowerCase()));
    fillTable();
  });

  const reset = document.createElement('button');
  reset.textContent = 'Réinitialiser';
  reset.className = 'btn btn-warning';
  reset.style.width = '15%';
  reset.style.margin = '10px auto';
  reset.style.display = 'block';
  reset.id = 'reset';
  reset.style.fontSize = '1.2em';
  reset.style.borderRadius = '5px';
  reset.style.padding = '5px';
  reset.style.cursor = 'pointer';
  reset.style.textAlign = 'center';
  div.appendChild(reset);
  reset.addEventListener('click', () => {
    search.value = '';
    renderCompanies();
  });
}

async function getCompanies() {
  // fetch companies
  const response = await fetch('http://localhost:3000/companies/contacts', {
    method: 'GET',
    headers: {
      Authorization: getToken(),
    }
  });
  if(response.status !== 200) {
    displayToast(await response.text(), 'danger')
  }
  return response.json();
}

function getNbStudentsWithIntershipForCompany(companyId, academicYear) {
  let nbStudents = 0;
  const index = companies.findIndex(company => company.idCompany === companyId);

  if (academicYear === undefined || academicYear === 'all') {
    companies[index].contacts.forEach(contact => {
      if(contact.state === 'accepté') {
        nbStudents+=1;
      }
    });
    return nbStudents;
  }
  companies[index].contacts.forEach(contact => {
    if(contact.state === 'accepté' && contact.academicYear === academicYear) {
      nbStudents+=1;
    }
  });
  return nbStudents;
}

function getNbStudentsWithIntership(academicYear) {
  const students = [];
  if (academicYear === undefined || academicYear === 'all') {
    companies.forEach(company => {
      company.contacts.forEach(contact => {
        if(!students.includes(contact.idStudent) && contact.state === 'accepté') {
          students.push(contact.idStudent);
        }
      });
    });
    return students.length;
  }
  companies.forEach(company => {
    company.contacts.forEach(contact => {
      if(contact.academicYear === academicYear && !students.includes(contact.idStudent) && contact.state === 'accepté') {
        students.push(contact.idStudent);
      }
    });
  });
  return students.length;
}

function getAcademicYears() {
  const academicYears = [];
  companies.forEach(company => {
    company.contacts.forEach(contact => {
      if(!academicYears.includes(contact.academicYear))
        academicYears.push(contact.academicYear);
    });
  });
  academicYears.sort((a, b) => b.split('-')[0] - a.split('-')[0]);
  return academicYears;
}

function getCurrentAcademicYear() {
  const date = new Date();
  const month = date.getMonth();
  const year = date.getFullYear();
  if(month+1 >= 9) {
    return `${year}-${year + 1}`;
  }
  return `${year - 1}-${year}`;
}

function resetColumNames() {
  document.querySelectorAll('th').forEach(th => {
    th.textContent = th.textContent.replace(' ▲', '').replace(' ▼', '');
  });
}

function orderByTradename(e) {
  resetColumNames();
  if(lastOrder.tradeName === 'asc') {
    lastOrder.tradeName = 'desc';
    e.target.textContent = 'Nom ▼';
  } else {
    lastOrder.tradeName = 'asc';
    e.target.textContent = 'Nom ▲';
  }

  function orderTable(tableContent) {
    return lastOrder.tradeName === 'asc'
        ? tableContent.sort((a, b) => b.tradeName.localeCompare(a.tradeName))
        : tableContent.sort((a, b) => a.tradeName.localeCompare(b.tradeName));
  }

  filteredCompanies = filteredCompanies !== undefined
      ? orderTable(filteredCompanies)
      : orderTable(companies);
  fillTable();
}

function orderByDesignation(e) {
  resetColumNames();
  if (lastOrder.designation === 'asc') {
    lastOrder.designation = 'desc';
    e.target.textContent = 'Appellation ▲';
  } else {
    lastOrder.designation = 'asc';
    e.target.textContent = 'Appellation ▼';
  }

  function orderTable(tableContent) {
    return lastOrder.designation === 'asc'
        ? tableContent.sort((a, b) => {
          if (a.designation === null) {
            return 1;
          }
          if (b.designation === null) {
            return -1;
          }
          return a.designation.localeCompare(b.designation);
        })
        : tableContent.sort((a, b) => {
          if (a.designation === null) {
            return -1;
          }
          if (b.designation === null) {
            return 1;
          }
          return b.designation.localeCompare(a.designation);
        });
  }

  filteredCompanies = filteredCompanies !== undefined
      ? orderTable(filteredCompanies)
      : orderTable(companies);
  fillTable();
}

function orderByCity(e) {
  resetColumNames();
  if (lastOrder.city === 'asc') {
    lastOrder.city = 'desc';
    e.target.textContent = 'Ville ▼';
  } else {
    lastOrder.city = 'asc';
    e.target.textContent = 'Ville ▲';
  }

  function orderTable(tableContent) {
    return lastOrder.city === 'asc'
        ? tableContent.sort((a, b) => {
          if (a.city === null) {
            return 1;
          }
          if (b.city === null) {
            return -1;
          }
          return a.city.localeCompare(b.city);
        })
        : tableContent.sort((a, b) => {
          if (a.city === null) {
            return -1;
          }
          if (b.city === null) {
            return 1;
          }
          return b.city.localeCompare(a.city);
        });
  }

  filteredCompanies = filteredCompanies !== undefined
      ? orderTable(filteredCompanies)
      : orderTable(companies);
  fillTable();
}

function orderByAcceptedStudents(e) {
  resetColumNames();
  if (lastOrder.students === 'asc') {
    lastOrder.students = 'desc';
    e.target.textContent = 'Nombre d\'étudiants ▲';
  } else {
    lastOrder.students = 'asc';
    e.target.textContent = 'Nombre d\'étudiants ▼';
  }

  function orderTable(tableContent) {
    return lastOrder.students === 'asc'
        ? tableContent.sort((a, b) => {
          const aStudents = a.contacts.filter(
              contact => contact.state === 'accepté');
          const bStudents = b.contacts.filter(
              contact => contact.state === 'accepté');
          return bStudents.length - aStudents.length;
        })
        : tableContent.sort((a, b) => {
          const aStudents = a.contacts.filter(
              contact => contact.state === 'accepté');
          const bStudents = b.contacts.filter(
              contact => contact.state === 'accepté');
          return aStudents.length - bStudents.length;
        });
  }

  filteredCompanies = filteredCompanies !== undefined
      ? orderTable(filteredCompanies)
      : orderTable(companies);
  fillTable();
}

function orderByBlacklisted(e) {
  resetColumNames();
  if (lastOrder.blacklisted === 'asc') {
    lastOrder.blacklisted = 'desc';
    e.target.textContent = 'Black-listée ▼';
  } else {
    lastOrder.blacklisted = 'asc';
    e.target.textContent = 'Black-listée ▲';
  }

  function orderTable(tableContent) {
    return lastOrder.blacklisted === 'asc'
        ? tableContent.sort((a, b) => a.blacklisted - b.blacklisted)
        : tableContent.sort((a, b) => b.blacklisted - a.blacklisted);
  }

  filteredCompanies = filteredCompanies !== undefined
      ? orderTable(filteredCompanies)
      : orderTable(companies);
  fillTable();
}

export default DashboardPage;