import React from 'react';
import { Admin } from 'react-admin';

import {
    dataProvider,
    authProvider,
    i18nProviderBuilder,
} from './libs/sgx/api';

import resources from './modules';
import messages from './providers/i18n';

import { 
    Dashboard,
    Layout,
    LoginPage,
    LogoutButton,
} from './layout';

import customRoutes from './layout/routes';

const App = () => (
    <Admin
        title="Historia de salud integrada"
        customRoutes={customRoutes}
        dataProvider={dataProvider}
        authProvider={authProvider}
        i18nProvider={i18nProviderBuilder(messages)}
        loginPage={LoginPage}
        dashboard={Dashboard}
        layout={Layout}
        logoutButton={LogoutButton}
        disableTelemetry >
        {permissions => resources(permissions)}
    </Admin>
);

export default App;
