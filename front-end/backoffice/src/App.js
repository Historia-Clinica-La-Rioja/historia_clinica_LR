import React from 'react';
import { Admin, Resource } from 'react-admin';

import { Dashboard } from './dashboard';

import cities from './modules/cities';
import departments from './modules/departments';

import springbootRestProvider from './providers/sgxDataProvider';
import authProvider from './providers/authProvider';
import i18nProvider from './providers/i18nProvider';

const dataProvider = springbootRestProvider('/api/backoffice', {
});

const App = () => (
    <Admin title="Hospitales" 
    dataProvider={dataProvider} 
    authProvider={authProvider}
    i18nProvider={i18nProvider}
    dashboard={Dashboard}
    >
        <Resource name="cities" {...cities} />
        <Resource name="departments" {...departments} />
    </Admin>
);

export default App;