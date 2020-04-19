import React from 'react';
import { Admin, Resource } from 'react-admin';

import { Dashboard } from './dashboard';

import cities from './modules/cities';
import departments from './modules/departments';
import institutions from './modules/institutions';
import addresses from './modules/addresses';
import sectors from './modules/sectors';
import clinicalspecialties from './modules/clinicalspecialties';
import clinicalspecialtysectors from './modules/clinicalspecialtysectors';

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
        <Resource name="institutions" {...institutions} />
        <Resource name="provinces" />
        <Resource name="addresses" {...addresses}/>
        <Resource name="sectors" {...sectors}/>
        <Resource name="clinicalspecialties" {...clinicalspecialties}/>
        <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors}/>
    </Admin>
);

export default App;
