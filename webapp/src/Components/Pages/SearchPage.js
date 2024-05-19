import {getToken, isAuthenticated} from '../../utils/auths';
import {
  clearPage,
  displayToast,
  renderBreadcrumb,
  renderPageTitle
} from '../../utils/render';
import Navigate from '../Router/Navigate';


async function fetchUsers() {
  const response = await fetch('http://localhost:3000/users', {
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
let users;

const SearchPage = async () => {
  
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    users = await fetchUsers();
    renderPageTitle('Recherche');
    document.title = 'Recherche';
    clearPage();
    renderBreadcrumb(
        {"Accueil": "/", "Liste des utilisateurs": "/search"});
    renderSearchPage();
    await renderUsers();
    
  }
};

function renderSearchPage() {
  const main = document.querySelector('main');
  main.innerHTML += `
    <div class="search-container justify-content-around d-flex">
      <div class="input-group mb-3" style="width: 25%">
        <div class="input-group-prepend">
          <label class="input-group-text" for="inputGroupSelect01">Année académique</label>
        </div>
        <select class="custom-select form-select">
          <option value="all">Toutes les années</option>
        </select>
      </div>
      
      <div class="input-group mb-3" style="width: 15%" hidden="hidden" id="checkboxDiv">
        <div class="input-group-prepend">
          <span class="input-group-text" id="basic-addon1">Que les étudiants</span>
        </div>
        <div class="input-group-text">
          <input type="checkbox" name="filter" value="etudiant" id="showStudents" checked>
        </div>
      </div>
      
      <div class="input-group mb-3" style="width: 30%">
        <div class="input-group-prepend">
          <span class="input-group-text" id="basic-addon1">Rechercher</span>
        </div>
        <input type="text" class="form-control" placeholder="Nom Prenom" aria-label="Username" aria-describedby="basic-addon1" id="search">
      </div>
    </div>
    <div class="d-flex overflow-auto" style="max-height: 70vh">
      <table class="table table-bordered">
        <thead class="table-light">
            <tr>
              <th>Nom</th>
              <th>Prénom</th>
              <th>Rôle</th>
              <th>Année académique</th>
              <th>Stage accepté</th>
            </tr>
        </thead>
      </table>
    </div>
  `;
  document.querySelector('thead').style.position = 'sticky';
  document.querySelector('thead').style.top = '0';
  // search the users
  const search = document.querySelector('#search');
  search.addEventListener('input', () => {
    const table = document.querySelector('table');
    const tbody = table.querySelector('tbody');
    const rows = tbody.querySelectorAll('tr');
    rows.forEach((row) => {
      const cells = row.querySelectorAll('td');
      const searchValue = search.value.toLowerCase();
      if (cells[0].textContent.toLowerCase().includes(searchValue) ||
        cells[1].textContent.toLowerCase().includes(searchValue)) {
        row.style.display = '';
      } else {
        row.style.display = 'none';
      }
    });
  });

  // filter the users by academic year
  const select = document.querySelector('select');
  
  const years = users.map((userMap) => userMap.user.academicYear);
  const uniqueYears = [...new Set(years)].filter((year) => year !== null && year !== undefined);
  uniqueYears.forEach((year) => {
    
    const currentYear= new Date().getFullYear();
    const currentMonth = new Date().getMonth();
    let academicYear;
    if (currentMonth >= 9) {
      academicYear = `${currentYear}-${currentYear+1}`;
    }
    else {
      academicYear = `${currentYear-1}-${currentYear}`;
    }
    if (year === academicYear) {
      const option = document.createElement('option');
      option.value = year;
      option.textContent = year;
      option.selected = true;
      select.appendChild(option);
    }
    else {
      const option = document.createElement('option');
      option.value = year;
      option.textContent = year;
      select.appendChild(option);
    }
  });
  select.addEventListener('change', () => {
    const table = document.querySelector('table');
    const tbody = table.querySelector('tbody');
    const rows = tbody.querySelectorAll('tr');
    const selectedYear = select.value;
    if (selectedYear === 'all') {
      document.querySelector('#checkboxDiv').hidden = false;
      renderUsers();
    } else {
      document.querySelector('#checkboxDiv').hidden = true;
    }
    rows.forEach((row) => {
      const cells = row.querySelectorAll('td');
      if (cells.length > 0 && cells[3].textContent !== selectedYear) {
        row.style.display = 'none';
      } else {
        row.style.display = '';
      }
    });
  });


  // filter the users to only show the students
  const filter = document.querySelector('#showStudents');
  filter.addEventListener('change', () => {
    const table = document.querySelector('table');
    const tbody = table.querySelector('tbody');
    const rows = tbody.querySelectorAll('tr');
    rows.forEach((row) => {
      const cells = row.querySelectorAll('td');
      console.log(cells)
      if (filter.checked) {
        if (cells.length > 0 && cells[2].textContent !== 'étudiant') {
          row.style.display = 'none';
        } else {
          row.style.display = '';
        }
      } else {
        row.style.display = '';
      }
    });
  });
}

function renderUsers() {
  const table = document.querySelector('table');
  if(document.querySelector('tbody') !== null) {
    document.querySelector('tbody').remove();
  }
  const tbody = document.createElement('tbody');

  users.forEach(u => {
    const tr = document.createElement('tr');
    const { user, accepted_contact: acceptedContact } = u;

    let academicYearDisplay = "N/A";
    let acceptedContactDisplay = "N/A";

    if (user.role === 'étudiant') {
      academicYearDisplay = user.academicYear;
      acceptedContactDisplay = acceptedContact ? 'Oui' : 'Non';
    }

    tr.innerHTML = `
      <td>${user.lastname}</td>
      <td>${user.firstname}</td>
      <td>${user.role}</td>
      <td>${academicYearDisplay}</td>
      <td>${acceptedContactDisplay}</td>
    `;
    
    if(user.role === 'étudiant') {
      tr.classList.add('clickable-row');
      tr.addEventListener('click', () => {
        Navigate(`/student-info`, user);
      });
    }
    const selectElement = document.querySelector('select');
    if(selectElement.value !== 'all' && user.academicYear !== selectElement.value) tr.style.display = 'none';
    const filter = document.querySelector('#showStudents');
    if(filter.checked && user.role !== 'étudiant') tr.style.display = 'none';
    tbody.appendChild(tr);
    if(user.role === 'étudiant') {
      const tdList = tr.querySelectorAll('td');
      tr.addEventListener('mouseover', () => {
        tdList.forEach((td) => {
          td.style.backgroundColor = 'lightgray'
        });
      });
      tr.addEventListener('mouseout', () => {
        tdList.forEach((td) => {
          td.style.backgroundColor = ''
        });
      });
    }
  });
  table.appendChild(tbody);
}

export default SearchPage;
