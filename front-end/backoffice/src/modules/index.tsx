import React from 'react';
import { Resource } from 'react-admin';
import SGXPermissions from "../libs/sgx/auth/SGXPermissions";

import cities from './cities';
import departments from './departments';
import institutions from './institutions';
import InstitutionShow from './institutions/InstitutionShow';
import InstitutionList from './institutions/InstitutionList';
import InstitutionEdit from './institutions/InstitutionEdit';
import addresses from './addresses';
import sectors from './sectors';
import clinicalspecialties from './clinicalspecialties';
import ClinicalSpecialtyShow from './clinicalspecialties/ClinicalSpecialtyShow';
import clinicalspecialtysectors from './clinicalspecialtysectors';
import rooms from './rooms';
import beds from './beds';
import healthcareprofessionals from './healthcareprofessionals';
import professionalspecialties from './professionalspecialties';
import ProfessionalSpecialtyShow from './professionalspecialties/show';
import healthcareprofessionalspecialties from './healthcareprofessionalspecialties';
import doctorsoffices from './doctorsoffices';

import person from './person';
import admin from './admin';
import users from './users';
import passwordReset from './password-reset';

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

const resources = (permissions: SGXPermissions) => 
    permissions.hasAnyAssignment(
    { role: 'ROOT', institutionId: -1 },
    { role: 'ADMINISTRADOR', institutionId: -1 }) ?
    [
        resourcesAdminRoot
    ]
    :
    [
        resourcesAdminInstitucional
    ];

export default resources;