/* eslint-disable import/no-extraneous-dependencies */
import anime from 'animejs';
import {clearPage, renderBreadcrumb} from "../../utils/render";
import {getAuthenticatedUser, isAuthenticated} from "../../utils/auths";
import Navigate from "../Router/Navigate";

let user;

const HomePage = () => {
    if (!isAuthenticated()) {
        Navigate('/login');
    } else {
        clearPage();
        document.title = "Home";
        console.log(getAuthenticatedUser());
        renderBreadcrumb({"Accueil": "/"});
        user = getAuthenticatedUser();
        if (getAuthenticatedUser().role === 'étudiant') {
            studentHomePage();
        } else if (getAuthenticatedUser().role === 'professeur') {
            teacherHomePage();
        } else if (getAuthenticatedUser().role === 'administratif') {
            adminHomePage();
        }
    }
};

function studentHomePage() {
    const main = document.querySelector('main');

    main.style.overflowX = 'hidden';

    const title = document.createElement('h1');
    title.innerText = `Bienvenue sur votre espace étudiant, ${user.firstname} !`;
    title.id = 'title';
    main.appendChild(title);

    anime({
        targets: '#title',
        translateX: 250,
        duration: 3000
    });
    const diiv = document.createElement('div');
    diiv.style.display = 'flex';
    diiv.style.justifyContent = 'space-around';
    diiv.style.marginTop = '5%';

    const profile = document.createElement('div');
    profile.className = 'card';
    profile.style.marginTop = '5%';
    profile.style.cursor = 'pointer';
    profile.style.width = '15rem'
    profile.style.width = 'auto';
    profile.style.display = 'flex';
    profile.style.alignItems = 'center';
    profile.style.justifyContent = 'center';
    profile.style.border = 'none';
    const text3 = document.createElement('div');
    text3.className = 'card-body';
    profile.innerHTML += `<svg xmlns="http://www.w3.org/2000/svg" width="50" height="40" fill="currentColor" class="bi bi-arrow-right-circle" viewBox="0 0 16 16">
  <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0M4.5 7.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5z"/>
</svg> `;
    profile.innerHTML += 'Vous voulez consulter votre profil ? Cliquez ici !';
    profile.appendChild(text3);
    diiv.appendChild(profile);
    profile.addEventListener('click', () => {
        Navigate('/profile');
    });

    const div = document.createElement('div');
    const internship = document.createElement('div');
    internship.className = 'card';
    const text = document.createElement('div');
    text.className = 'card-body';
    text.textContent = 'Vous voulez consulter votre stage ?';
    internship.appendChild(text);
    const button = document.createElement('button');
    button.className = 'btn btn-primary';
    button.id = 'bluebutton';
    button.textContent = 'Accéder à votre stage';
    internship.appendChild(button);
    internship.style.width = '18rem';
    internship.addEventListener('click', () => {
        Navigate('/stage');
    });

    div.appendChild(internship);

    const contacts = document.createElement('div');
    contacts.className = 'card';
    contacts.style.marginTop = '10%';
    const text2 = document.createElement('div');
    text2.className = 'card-body';
    text2.textContent = 'Vous voulez consulter vos contacts ?';
    contacts.appendChild(text2);
    const button2 = document.createElement('button');
    button2.className = 'btn btn-primary';
    button2.id = 'bluebutton';
    button2.textContent = 'Accéder à vos contacts';
    contacts.appendChild(button2);
    contacts.style.width = '18rem';
    main.appendChild(contacts);
    contacts.addEventListener('click', () => {
        Navigate('/contact');
    });

    div.appendChild(contacts);
    diiv.appendChild(div);
    main.appendChild(diiv);

}

/**
 * Function to display the teacher home page with buttons to see stats and students
 */
function teacherHomePage() {
    const main = document.querySelector('main');

    main.style.overflowX = 'hidden';

    const title = document.createElement('h1');
    title.innerText = `Bienvenue sur votre espace professeur, ${user.firstname} !`;
    title.id = 'title';
    main.appendChild(title);

    anime({
        targets: '#title',
        translateX: 250,
        duration: 3000
    });

    const diiv = document.createElement('div');
    diiv.style.display = 'flex';
    diiv.style.justifyContent = 'space-around';
    diiv.style.marginTop = '5%';

    const profile = document.createElement('div');
    profile.className = 'card';
    profile.style.marginTop = '5%';
    profile.style.cursor = 'pointer';
    profile.style.width = '15rem'
    profile.style.width = 'auto';
    profile.style.display = 'flex';
    profile.style.alignItems = 'center';
    profile.style.justifyContent = 'center';
    profile.style.border = 'none';
    const text3 = document.createElement('div');
    text3.className = 'card-body';
    profile.innerHTML += `<svg xmlns="http://www.w3.org/2000/svg" width="50" height="40" fill="currentColor" class="bi bi-arrow-right-circle" viewBox="0 0 16 16">
  <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0M4.5 7.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5z"/>
</svg> `;
    profile.innerHTML += 'Vous voulez consulter votre profil ? Cliquez ici !';
    profile.appendChild(text3);
    diiv.appendChild(profile);
    profile.addEventListener('click', () => {
        Navigate('/profile');
    });

    const div = document.createElement('div');
    div.style.display = 'flex-column';

    const stats = document.createElement('div');
    stats.className = 'card';
    const text = document.createElement('div');
    text.className = 'card-body';
    text.textContent = 'Vous voulez consulter les statistiques ?';
    stats.appendChild(text);
    const button = document.createElement('button');
    button.className = 'btn btn-primary';
    button.textContent = 'Accéder aux statistiques';
    stats.appendChild(button);
    stats.style.width = '18rem';
    stats.addEventListener('click', () => {
        Navigate('/dashboard');
    });

    div.appendChild(stats);

    const students = document.createElement('div');
    students.className = 'card';
    students.style.marginTop = '10%';
    const text2 = document.createElement('div');
    text2.className = 'card-body';
    text2.textContent = 'Vous voulez consulter les étudiants ?';
    students.appendChild(text2);
    const button2 = document.createElement('button');
    button2.className = 'btn btn-primary';
    button2.textContent = 'Rechercher un utilisateur';
    students.appendChild(button2);
    students.style.width = '18rem';
    students.addEventListener('click', () => {
        Navigate('/search');
    });

    div.appendChild(students);
    diiv.appendChild(div);
    main.appendChild(diiv);
}

/**
 * Function to display the admin home page with buttons to see students
 */
function adminHomePage() {
    const main = document.querySelector('main');

    main.style.overflowX = 'hidden';

    const title = document.createElement('h1');
    title.innerText = `Bienvenue sur votre espace administratif, ${user.firstname} !`;
    title.id = 'title';
    main.appendChild(title);

    anime({
        targets: '#title',
        translateX: 250,
        duration: 3000
    });

    const diiv = document.createElement('div');
    diiv.style.display = 'flex';
    diiv.style.justifyContent = 'space-around';
    diiv.style.marginTop = '5%';

    const profile = document.createElement('div');
    profile.className = 'card';
    profile.style.marginTop = '7%';
    profile.style.marginLeft = '5%';
    profile.style.cursor = 'pointer';
    profile.style.width = '15rem'
    profile.style.width = 'auto';
    profile.style.display = 'flex';
    profile.style.alignItems = 'center';
    profile.style.justifyContent = 'center';
    profile.style.border = 'none';
    const text3 = document.createElement('div');
    text3.className = 'card-body';
    profile.innerHTML += `<svg xmlns="http://www.w3.org/2000/svg" width="50" height="40" fill="currentColor" class="bi bi-arrow-right-circle" viewBox="0 0 16 16">
  <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0M4.5 7.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5z"/>
</svg> `;
    profile.innerHTML += 'Vous voulez consulter votre profil ? Cliquez ici !';
    profile.appendChild(text3);
    diiv.appendChild(profile);
    profile.addEventListener('click', () => {
        Navigate('/profile');
    });

    const students = document.createElement('div');
    students.className = 'card';
    students.style.marginTop = '5%';
    const text2 = document.createElement('div');
    text2.className = 'card-body';
    text2.textContent = 'Vous voulez consulter les étudiants ?';
    students.appendChild(text2);
    const button2 = document.createElement('button');
    button2.className = 'btn btn-primary';
    button2.textContent = 'Accéder aux utilisateurs';
    students.appendChild(button2);
    students.style.width = '18rem';
    students.addEventListener('click', () => {
        Navigate('/search');
    });

    diiv.appendChild(students);
    main.appendChild(diiv);
}

export default HomePage;