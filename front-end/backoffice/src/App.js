import React from 'react';
import { Admin, Resource } from 'react-admin';

import { Dashboard } from './dashboard';
import LoginPage from './login/LoginPage.js';

import cities from './modules/cities';
import departments from './modules/departments';
import institutions from './modules/institutions';
import InstitutionShow from './modules/institutions/InstitutionShow';
import InstitutionList from './modules/institutions/InstitutionList';
import InstitutionEdit from './modules/institutions/InstitutionEdit';
import addresses from './modules/addresses';
import sectors from './modules/sectors';
import clinicalspecialties from './modules/clinicalspecialties';
import ClinicalSpecialtyShow from './modules/clinicalspecialties/ClinicalSpecialtyShow';
import clinicalspecialtysectors from './modules/clinicalspecialtysectors';
import rooms from './modules/rooms';
import beds from './modules/beds';
import healthcareprofessionals from './modules/healthcareprofessionals';
import professionalspecialties from './modules/professionalspecialties';
import ProfessionalSpecialtyShow from './modules/professionalspecialties/show';
import healthcareprofessionalspecialties from './modules/healthcareprofessionalspecialties';
import doctorsoffices from './modules/doctorsoffices';

import person from './modules/person';
import admin from './modules/admin';
import users from './modules/users';
import passwordReset from './modules/password-reset';

import springbootRestProvider from './providers/sgxDataProvider';
import authProvider from './providers/authProvider';
import i18nProvider from './providers/i18nProvider';
import customRoutes from './layout/routes';

const dataProvider = springbootRestProvider('/backoffice', {
});

const App = () => {

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
        <Resource name="educationtypes" />,
        <Resource name="internmentepisodes" />,
        <Resource name="bedcategories" />,
        <Resource name="healthcareprofessionals" />,
        <Resource name="professionalspecialties" show={ProfessionalSpecialtyShow} />,
        <Resource name="institutions" show={InstitutionShow} list={InstitutionList} edit={InstitutionEdit} />,
        <Resource name="addresses" {...addresses} />,
        <Resource name="sectors" {...sectors} />,
        <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors} />,
        <Resource name="doctorsoffices" {...doctorsoffices} />,
        <Resource name="rooms" {...rooms} />,
        <Resource name="beds" {...beds} />,
        <Resource name="clinicalspecialties" show={ClinicalSpecialtyShow} />,
        <Resource name="dependencies" />,
        <Resource name="personextended" />,
    ];

    const resourcesAdminRoot = [
        <Resource name="person" {...person} />,
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
        <Resource name="addresses" {...addresses} />,
        <Resource name="bedcategories" />,
        <Resource name="clinicalspecialties" {...clinicalspecialties} />,
        <Resource name="professionalspecialties" {...professionalspecialties} />,
        <Resource name="healthcareprofessionals" {...healthcareprofessionals} />,
        <Resource name="healthcareprofessionalspecialties" {...healthcareprofessionalspecialties} />,
        <Resource name="educationtypes" />,
        <Resource name="password-reset" {...passwordReset} />,
        <Resource name="roles" />,
        <Resource name="internmentepisodes" />,
        <Resource name="institutions" {...institutions} />,
        <Resource name="addresses" {...addresses} />,
        <Resource name="sectors" {...sectors} />,
        <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors} />,
        <Resource name="doctorsoffices" {...doctorsoffices} />,
        <Resource name="rooms" {...rooms} />,
        <Resource name="beds" {...beds} />,
        <Resource name="admin" {...admin}/>,
        <Resource name="users" {...users} />,
        <Resource name="dependencies" />,
        <Resource name="personextended" />,
    ];

    return <Admin title="Historia de salud integrada"
                customRoutes={customRoutes}
                dataProvider={dataProvider}
                authProvider={authProvider}
                i18nProvider={i18nProvider}
                loginPage={LoginPage}
                dashboard={Dashboard}
    >
        {
            permissions => permissions.hasAnyAssignment(
                { role: 'ROOT', institutionId: -1 },
                { role: 'ADMINISTRADOR', institutionId: -1 }) ?
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
