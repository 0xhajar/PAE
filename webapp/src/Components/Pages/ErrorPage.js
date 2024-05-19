import {clearPage} from "../../utils/render";

const ErrorPage = () => {
  clearPage();
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.style.display = 'flex';
  div.style.flexDirection = 'column';

  const title = document.createElement('h3');
  title.textContent = 'Erreur';
  title.style.textAlign = 'center';
  div.appendChild(title);

  const content = document.createElement('p');
  content.textContent = 'La page demandée n\'éxiste pas !';
  content.style.textAlign = 'center';
  div.appendChild(content);

  main.appendChild(div);
}

export default ErrorPage;