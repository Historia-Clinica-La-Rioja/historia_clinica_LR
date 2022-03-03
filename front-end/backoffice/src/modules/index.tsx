import React from 'react';
import { Resource } from 'react-admin';
import SGXPermissions from "../libs/sgx/auth/SGXPermissions";

import cities from './cities';
import departments from './departments';
import institutions from './institutions';
import InstitutionShow from './institutions/InstitutionShow';
import InstitutionList from './institutions/InstitutionList';
import InstitutionEdit from './institutions/InstitutionEdit';
import SnvsShow from './snvs/SnvsShow';
import SnvsList from './snvs/SnvsList';
import addresses from './addresses';
import sectors from './sectors';
import clinicalspecialties from './clinicalspecialties';
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
import careLines from "./carelines";
import clinicalspecialtycarelines from "./clinicalspecialtycarelines";
import documenttypes from "./documenttypes";
import documentfiles from "./documentfiles";
import properties from "./properties";
import restClientMeasures from "./rest-client-measures";
import medicalCoverage from "./medicalcoverage";
import privatehealthinsuranceplans from "./privatehealthinsuranceplans";
import snomedgroups from "./snomedgroups";


import { ROOT, ADMINISTRADOR } from './roles';
import snomedconcepts from "./snomedconcepts";

// Ampliación
//

const resourcesAdminInstitucional = [
    <Resource name="healthcareprofessionals" />,
    <Resource name="professionalspecialties" show={ProfessionalSpecialtyShow} />,
    <Resource name="institutions" show={InstitutionShow} list={InstitutionList} edit={InstitutionEdit} />,
    <Resource name="snvs" show={SnvsShow} list={SnvsList} />,
    <Resource name="sectors" {...sectors} />,
    <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors} />,
    <Resource name="doctorsoffices" {...doctorsoffices} />,
    <Resource name="rooms" {...rooms} />,
    <Resource name="beds" {...beds} />,
    <Resource name="cities" />,
    <Resource name="snomedgroups"   {...snomedgroups} />,
    <Resource name="snomedrelatedgroups" />,
    <Resource name="snomedconcepts" {...snomedconcepts} />,
    <Resource name="departments" />,
];

const resourcesAdminRoot = (permissions: SGXPermissions) => [
    
    <Resource name="professionalspecialties" {...professionalspecialties} />,
    <Resource name="healthcareprofessionals" {...healthcareprofessionals} />,
    <Resource name="healthcareprofessionalspecialties" {...healthcareprofessionalspecialties} />,
    <Resource name="password-reset" {...passwordReset} />,
    <Resource name="roles" />,
    <Resource name="institutions" {...institutions} />,
    <Resource name="snvs" show={SnvsShow} list={SnvsList} />,
    <Resource name="addresses" {...addresses} />,
    <Resource name="sectors" {...sectors} />,
    <Resource name="clinicalspecialtysectors" {...clinicalspecialtysectors} />,
    <Resource name="doctorsoffices" {...doctorsoffices} />,
    <Resource name="rooms" {...rooms} />,
    <Resource name="beds" {...beds} />,
    <Resource name="admin" {...admin}/>,
    <Resource name="users" {...users} />,
    <Resource name="carelines" {...careLines} />,
    <Resource name="clinicalspecialtycarelines" {...clinicalspecialtycarelines} />,
    <Resource name="documentfiles" {...documentfiles} />,
    <Resource name="documenttypes" {...documenttypes} />,
    <Resource name="rest-client-measures" {...restClientMeasures} />,
    <Resource name="cities" {...cities} />,
    <Resource name="departments" {...departments} />,
    <Resource name="medicalcoverages" {...medicalCoverage} />,
    <Resource name="medicalcoveragetypes" />,
    <Resource name="privatehealthinsuranceplans" {...privatehealthinsuranceplans} />,
    <Resource name="medicalcoveragesmerge" />,
    <Resource name="snomedgroups"  {...snomedgroups} />,
    <Resource name="snomedrelatedgroups" />,
    <Resource name="snomedconcepts" {...snomedconcepts} />,
    <Resource name="properties" {...properties(permissions)} />,

    // Ampliación
    // 
];

const resourcesFor = (permissions: SGXPermissions) =>
    permissions.hasAnyAssignment(
        ROOT, ADMINISTRADOR
    ) ? resourcesAdminRoot(permissions): resourcesAdminInstitucional;

const resources = (permissions: SGXPermissions) => [
    <Resource name="person" {...person(permissions)} />,
    <Resource name="clinicalspecialties" {...clinicalspecialties(permissions)} />,
    ...resourcesFor(permissions),
    <Resource name="addresses" {...addresses} />,
    <Resource name="identificationTypes" />,
    <Resource name="dependencies" />,
    <Resource name="personextended" />,
    <Resource name="genders" />,
    <Resource name="sectortypes" />,
    <Resource name="agegroups" />,
    <Resource name="caretypes" />,
    <Resource name="sectororganizations" />,
    <Resource name="hospitalizationtypes" />,
    <Resource name="provinces" />,
    <Resource name="bedcategories" />,
    <Resource name="educationtypes" />,
    <Resource name="snomedgroups"   {...snomedgroups} />,
    <Resource name="snomedrelatedgroups" />,
    <Resource name="snomedconcepts" {...snomedconcepts} />,
    <Resource name="internmentepisodes" />,
];

export default resources;