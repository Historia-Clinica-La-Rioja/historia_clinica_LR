import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import appInfoProvider from './providers/appInfoProvider';

document.getElementById('root').innerHTML = '<div id="loading">Cargando...</div>';

appInfoProvider.loadInfo().then(
  _ => {
    document.getElementById('root').innerHTML = '';

    ReactDOM.render(
      <React.StrictMode>
        <App />
      </React.StrictMode>,
      document.getElementById('root')
    );

    // If you want your app to work offline and load faster, you can change
    // unregister() to register() below. Note this comes with some pitfalls.
    // Learn more about service workers: https://bit.ly/CRA-PWA
    serviceWorker.unregister();
  },
  _ => document.getElementById('loading').innerHTML = 'Verifique la conexión con el servidor'
);
