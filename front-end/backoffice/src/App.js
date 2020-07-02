import React from 'react';
import { Admin, Resource } from 'react-admin';

import { Dashboard } from './dashboard';
import OauthLoginPage from './login/OauthLoginPage';

import cities from './modules/cities';
import departments from './modules/departments';
import institutions from './modules/institutions';
import addresses from './modules/addresses';
import sectors from './modules/sectors';
import clinicalspecialties from './modules/clinicalspecialties';
import clinicalspecialtysectors from './modules/clinicalspecialtysectors';
import rooms from './modules/rooms'
import beds from './modules/beds'
import healthcareprofessionals from './modules/healthcareprofessionals'
import professionalspecialties from './modules/professionalspecialties'
import healthcareprofessionalspecialties from './modules/healthcareprofessionalspecialties'
import people from './modules/people'
import users from './modules/users';
import passwordReset from './modules/password-reset';

import springbootRestProvider from './providers/sgxDataProvider';
import authProvider from './providers/authProvider';
import i18nProvider from './providers/i18nProvider';
import customRoutes from './layout/routes';
import appInfoProvider from './providers/appInfoProvider'

const dataProvider = springbootRestProvider('/backoffice', {
});

const App = () => {
    const oauth = appInfoProvider.getInfo().oauthConfig.enabled;

    const resourcesAdminInstitucional = [
        <Resource name="institutions" {...institutions} />,
        <Resource name="sectors" {...sectors}/>,
        <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors}/>,
        <Resource name="rooms" {...rooms}/>,
        <Resource name="beds" {...beds}/>,
    ];

    const resourcesAdminRoot = [
        <Resource name="provinces" />,
        <Resource name="cities" {...cities} />,
        <Resource name="departments" {...departments} />,
        <Resource name="addresses" {...addresses}/>,
        <Resource name="clinicalspecialties" {...clinicalspecialties}/>,
        <Resource name="bedcategories" />,
        <Resource name="healthcareprofessionals" {...healthcareprofessionals}/>,
        <Resource name="professionalspecialties" {...professionalspecialties}/>,
        <Resource name="educationtypes"/>,
        <Resource name="people" {...people}/>,
        <Resource name="users" {...users}/>,
        <Resource name="password-reset" {...passwordReset}/>,
        <Resource name="roles" />,
        <Resource name="internmentepisodes" />,
        <Resource name="healthcareprofessionalspecialties" {...healthcareprofessionalspecialties}/>,
    ];

    return <Admin title="Historia de salud integrada"
                  customRoutes={customRoutes}
                  dataProvider={dataProvider}
                  authProvider={authProvider}
                  i18nProvider={i18nProvider}
                  loginPage={oauth ? OauthLoginPage : undefined}
                  dashboard={Dashboard}
    >
        {
            permissions => permissions.hasAnyAssignment(
                {role: 'ROOT', institutionId: -1},
                {role: 'ADMINISTRADOR', institutionId: -1}) ?
                [
                    resourcesAdminRoot,
                    resourcesAdminInstitucional
                ]
                :
                [
                    resourcesAdminInstitucional
                ]
        }
    </Admin>
}

export default App;
