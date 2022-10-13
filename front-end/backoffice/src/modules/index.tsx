import React from 'react';
import {Resource} from 'react-admin';
import SGXPermissions from "../libs/sgx/auth/SGXPermissions";

import cities from './cities';
import departments from './departments';
import institutions from './institutions';
import snvs from './snvs';
import addresses from './addresses';
import sectors from './sectors';
import rootSectors from './root-sectors';
import clinicalspecialties from './clinicalspecialties';
import clinicalservices from './clinicalservices';
import clinicalservicesectors from './clinicalservicesectors';
import rooms from './rooms';
import beds from './beds';
import professionalprofessions from './professionalprofessions';
import professionalSpecialties from './professionalspecialties';
import healthcareprofessionalspecialties from './healthcareprofessionalspecialties';
import doctorsoffices from './doctorsoffices';
import holidays from './holidays';
import person from './person';
import admin from './admin';
import users from './users';
import passwordReset from './password-reset';
import careLines from './carelines';
import clinicalspecialtycarelines from './clinicalspecialtycarelines';
import documentTypes from './documenttypes';
import documentFiles from './documentfiles';
import properties from './properties';
import restClientMeasures from './rest-client-measures';
import medicalCoverage from './medicalcoverage';
import snomedgroups from './snomedgroups';
import carelineproblems from './carelineproblems';


import { ROOT, ADMINISTRADOR } from './roles';
import snomedconcepts from './snomedconcepts';
import snomedrelatedgroups from './snomedrelatedgroups';
import medicalcoverageplans from "./medicalcoverageplans";

// Ampliación
import healthcareprofessionalhealthinsurances from './healthcareprofessionalhealthinsurances';
import mandatorymedicalpractices from './mandatorymedicalpractices';
import clinicalspecialtymandatorymedicalpractices from './clinicalspecialtymandatorymedicalpractices';
import healthinsurancepractices from './healthinsurancepractices';
import mandatoryprofessionalpracticefreedays from './mandatoryprofessionalpracticefreedays';
import bookingInstitutions from "./booking-institutions";
import healthcareprofessionallicensenumbers from "./healthcareprofessionallicensenumbers";
import licensenumbertypes from "./licensenumbertypes";
import healthcareprofessionalspecialtylicensenumbers from "./healthcareprofessionalspecialtylicensenumbers";
//
import wcDefinitionPath from './wcDefinitionPathCreate';
import careLineInstitution from "./carelineinstitution";
import carelineinstitutionspecialty from "./carelineinstitutionspecialty";
import careLineInstitutionPractice from "./carelineinstitutionpractice";
import institutionpractices from "./institutionpractices";
import institutionpracticesrelatedgroups from "./institutionpracticesrelatedgroups";

const resourcesAdminInstitucional = (permissions: SGXPermissions) =>
    permissions.isOn('BACKOFFICE_MOSTRAR_ABM_RESERVA_TURNOS') ?
        [
        <Resource name="professionalprofessions" {...professionalprofessions}/>,
        <Resource name="booking-institution" {...bookingInstitutions(permissions)}/>,
        <Resource name="healthcareprofessionalhealthinsurances"  {...healthcareprofessionalhealthinsurances}/>,
        <Resource name="mandatorymedicalpractices"  {...mandatorymedicalpractices}/>,
        <Resource name="clinicalspecialtymandatorymedicalpractices"  {...clinicalspecialtymandatorymedicalpractices}/>,
        <Resource name="healthinsurancepractices"  {...healthinsurancepractices}/>,
        <Resource name="mandatoryprofessionalpracticefreedays"  {...mandatoryprofessionalpracticefreedays}/>,
        ] : []
;

const resourcesAdminRoot = (permissions: SGXPermissions) => [
    <Resource name="professionalprofessions" {...professionalprofessions} />,
    <Resource name="healthcareprofessionalspecialties" {...healthcareprofessionalspecialties} />,
    <Resource name="medicalcoveragetypes" />,
    <Resource name="medicalcoverageplans" {...medicalcoverageplans} />,
    <Resource name="medicalcoveragesmerge" />,
    // Ampliación
    // 
];

const resourcesFor = (permissions: SGXPermissions) =>
    permissions.hasAnyAssignment(
        ROOT, ADMINISTRADOR
    ) ? resourcesAdminRoot(permissions): resourcesAdminInstitucional(permissions);

const resources = (permissions: SGXPermissions) => [
    // staff
    <Resource name="person" {...person(permissions)} />,
    <Resource name="admin" {...admin(permissions)}/>,
    <Resource name="users" {...users}/>,
    <Resource name="roles" />,
    <Resource name="password-reset" {...passwordReset(permissions)} />,
    ...resourcesFor(permissions),
    // facilities
    <Resource name="institutions" {...institutions(permissions)} />,
    <Resource name="sectors" {...sectors} />,
    <Resource name="rootsectors" {...rootSectors} />,
    <Resource name="clinicalservicesectors" {...clinicalservicesectors} />,
    <Resource name="doctorsoffices" {...doctorsoffices} />,
    <Resource name="rooms" {...rooms} />,
    <Resource name="beds" {...beds} />,
    <Resource name="clinicalspecialtycarelines" {...clinicalspecialtycarelines} />,
    <Resource name="carelines" {...careLines(permissions)} />,
    <Resource name="carelineproblems" {...carelineproblems} />,
    <Resource name="carelineinstitution" {...careLineInstitution(permissions)} />,
    <Resource name="carelineinstitutionspecialty" {...carelineinstitutionspecialty(permissions)} />,
    <Resource name="carelineinstitutionpractice" {...careLineInstitutionPractice(permissions)} />,
    <Resource name="practicesinstitution" />,
    <Resource name="carelinespecialtyinstitution" />,
    <Resource name="snowstormproblems" />,
    // debug
    <Resource name="snvs"  {...snvs} />,
    <Resource name="documentfiles" {...documentFiles(permissions)} />,
    <Resource name="rest-client-measures" {...restClientMeasures(permissions)} />,
    <Resource name="properties" {...properties(permissions)} />,
    // masterData
    <Resource name="cities" {...cities(permissions)} />,
    <Resource name="departments" {...departments(permissions)} />,
    <Resource name="addresses" {...addresses} />,
    <Resource name="documenttypes" {...documentTypes(permissions)} />,
    <Resource name="snomedgroups"   {...snomedgroups} />,
    <Resource name="medicalcoverages" {...medicalCoverage(permissions)} />,
    <Resource name="clinicalspecialties" {...clinicalspecialties(permissions)} />,
    <Resource name="clinicalservices" {...clinicalservices(permissions)} />,
    <Resource name="professionalspecialties" {...professionalSpecialties(permissions)} />,
    <Resource name="holidays" {...holidays(permissions)} />,
    <Resource name="snomedgrouptypes" />,
    // more
    <Resource name="identificationTypes" />,
    <Resource name="patient" />,
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
    <Resource name="healthcareprofessionallicensenumbers" {...healthcareprofessionallicensenumbers} />,
    <Resource name="healthcareprofessionalspecialtylicensenumbers" {...healthcareprofessionalspecialtylicensenumbers} />,
    <Resource name="licensenumbertypes" {...licensenumbertypes} />,
    <Resource name="institutionpractices" {...institutionpractices} />,
    <Resource name="institutionpracticesrelatedgroups" {...institutionpracticesrelatedgroups} />,
    <Resource name="snowstormpractices" />,

    <Resource name="snomedgroupconcepts" />,
    <Resource name="snomedrelatedgroups"  {...snomedrelatedgroups} />,
    <Resource name="snomedconcepts" {...snomedconcepts} />,
    <Resource name="internmentepisodes" />,
    <Resource name="healthcareprofessionals" />,

    //Extension
    <Resource name="wcDefinitionPath" {...wcDefinitionPath(permissions)} />,
];

export default resources;
