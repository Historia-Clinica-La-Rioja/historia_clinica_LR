import React from 'react';
import {Admin, Resource} from 'react-admin';

import {Dashboard} from './dashboard';
import OauthLoginPage from './login/OauthLoginPage';
import LoginPage from './login/LoginPage.js';

import cities from './modules/cities';
import departments from './modules/departments';
import institutions from './modules/institutions';
import InstitutionShow from './modules/institutions/show';
import InstitutionList from './modules/institutions/list';
import InstitutionEdit from './modules/institutions/edit';
import addresses from './modules/addresses';
import sectors from './modules/sectors';
import clinicalspecialties from './modules/clinicalspecialties';
import ClinicalSpecialtyShow from './modules/clinicalspecialties/show';
import clinicalspecialtysectors from './modules/clinicalspecialtysectors';
import rooms from './modules/rooms'
import beds from './modules/beds'
import healthcareprofessionals from './modules/healthcareprofessionals'
import professionalspecialties from './modules/professionalspecialties'
import ProfessionalSpecialtyShow from './modules/professionalspecialties/show'
import healthcareprofessionalspecialties from './modules/healthcareprofessionalspecialties'
import doctorsoffices from './modules/doctorsoffices'

import person from './modules/person'
import users from './modules/users';
import passwordReset from './modules/password-reset';

import springbootRestProvider from './providers/sgxDataProvider';
import authProvider from './providers/authProvider';
import i18nProvider from './providers/i18nProvider';
import customRoutes from './layout/routes';
import appInfoProvider from './providers/appInfoProvider'
import ImmunizationDataList from "./modules/nomivac/immunizationdata/list";
import ImmunizationSyncList from "./modules/nomivac/immunizationsync/list";

const dataProvider = springbootRestProvider('/backoffice', {
});

const App = () => {
    const oauth = appInfoProvider.getInfo().oauthConfig.enabled;

    const resourcesAdminInstitucional = [
        <Resource name="sectortypes" />,
        <Resource name="agegroups" />,
        <Resource name="caretypes" />,
        <Resource name="sectororganizations" />,
        <Resource name="hospitalizationtypes" />,
        <Resource name="provinces" />,
        <Resource name="identificationTypes" />,
        <Resource name="cities" />,
        <Resource name="departments" />,
        <Resource name="educationtypes"/>,
        <Resource name="internmentepisodes" />,
        <Resource name="bedcategories" />,
        <Resource name="healthcareprofessionals" />,
        <Resource name="professionalspecialties" show={ProfessionalSpecialtyShow} />,
        <Resource name="institutions" show={InstitutionShow} list={InstitutionList} edit={InstitutionEdit}/>,
        <Resource name="addresses" {...addresses}/>,
        <Resource name="sectors" {...sectors}/>,      
        <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors}/>,
        <Resource name="doctorsoffices" {...doctorsoffices}/>,
        <Resource name="rooms" {...rooms}/>,
        <Resource name="beds" {...beds}/>,
        <Resource name="clinicalspecialties" show={ClinicalSpecialtyShow}/>,
        <Resource name="dependencies" />,
        <Resource name="personextended" />,
    ];

    const resourcesAdminRoot = [
        <Resource name="person" {...person}/>,
        <Resource name="identificationTypes" />,
        <Resource name="genders" />,
        <Resource name="sectortypes" />,
        <Resource name="agegroups" />,
        <Resource name="caretypes" />,
        <Resource name="sectororganizations" />,
        <Resource name="hospitalizationtypes" />,
        <Resource name="provinces" />,
        <Resource name="cities" {...cities} />,
        <Resource name="departments" {...departments} />,
        <Resource name="addresses" {...addresses}/>,
        <Resource name="bedcategories" />,
        <Resource name="clinicalspecialties" {...clinicalspecialties}/>,
        <Resource name="professionalspecialties" {...professionalspecialties}/>,
        <Resource name="healthcareprofessionals" {...healthcareprofessionals}/>,
        <Resource name="healthcareprofessionalspecialties" {...healthcareprofessionalspecialties}/>,
        <Resource name="educationtypes"/>,
        <Resource name="password-reset" {...passwordReset}/>,
        <Resource name="roles" />,
        <Resource name="internmentepisodes" />,
        <Resource name="institutions" {...institutions} />,
        <Resource name="addresses" {...addresses}/>,
        <Resource name="sectors" {...sectors}/>,      
        <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors}/>,
        <Resource name="doctorsoffices" {...doctorsoffices}/>,
        <Resource name="rooms" {...rooms}/>,
        <Resource name="beds" {...beds}/>,
        <Resource name="users" {...users}/>,
        <Resource name="dependencies" />,
        <Resource name="personextended" />,
        <Resource name="nomivac-immunizationdata" list={ImmunizationDataList}/>,
        <Resource name="nomivac-immunizationsync" list={ImmunizationSyncList}/>,
    ];

    return <Admin title="Historia de salud integrada"
                  customRoutes={customRoutes}
                  dataProvider={dataProvider}
                  authProvider={authProvider}
                  i18nProvider={i18nProvider}
                  loginPage={oauth ? OauthLoginPage : LoginPage}
                  dashboard={Dashboard}
    >
        {
            permissions => permissions.hasAnyAssignment(
                {role: 'ROOT', institutionId: -1},
                {role: 'ADMINISTRADOR', institutionId: -1}) ?
                [
                    resourcesAdminRoot
                ]
                :
                [
                    resourcesAdminInstitucional
                ]
        }
    </Admin>
}

export default App;
