import React from 'react';
import { Admin } from 'react-admin';

import {
    dataProvider,
    authProvider,
} from './libs/sgx/api';

import resources from './modules';

import { 
    Dashboard,
    Layout,
    LoginPage,
} from './layout';

import i18nProvider from './providers/i18nProvider';
import customRoutes from './layout/routes';

const App = () => (
    <Admin
        title="Historia de salud integrada"
        customRoutes={customRoutes}
        dataProvider={dataProvider}
        authProvider={authProvider}
        i18nProvider={i18nProvider}
        loginPage={LoginPage}
        dashboard={Dashboard}
        layout={Layout}
        disableTelemetry >
        {permissions => resources(permissions)}
    </Admin>
);

export default App;
