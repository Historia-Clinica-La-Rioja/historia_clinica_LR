import React from 'react';
import { Admin } from 'react-admin';

import {
    dataProvider,
    authProvider,
} from './libs/sgx/api';

import { Dashboard } from './dashboard';
import LoginPage from './login/LoginPage.js';

import resources from './modules';

import i18nProvider from './providers/i18nProvider';
import customRoutes from './layout/routes';


const App = () => {

    return <Admin title="Historia de salud integrada"
                customRoutes={customRoutes}
                dataProvider={dataProvider}
                authProvider={authProvider}
                i18nProvider={i18nProvider}
                loginPage={LoginPage}
                dashboard={Dashboard}
    >
        {
            permissions => resources(permissions)
        }
    </Admin>
}

export default App;
