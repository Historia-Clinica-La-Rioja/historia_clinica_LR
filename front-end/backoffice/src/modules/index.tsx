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
import properties from './properties';
import restClientMeasures from './rest-client-measures';
import medicalCoverage from './medicalcoverage';
import snomedgroups from './snomedgroups';
import carelineproblems from './carelineproblems';
import carelinerole from './carelinerole';
import userroles from './userroles';
import hierarchicalunittypes from './hierarchicalunittypes';

import {
    ROOT,
    ADMINISTRADOR,
    ADMINISTRADOR_DE_ACCESO_DOMINIO,
    AUDITORIA_DE_ACCESO,
} from './roles';
import snomedconcepts from './snomedconcepts';
import snomedrelatedgroups from './snomedrelatedgroups';
import medicalcoverageplans from "./medicalcoverageplans";
import hierarchicalunits from "./hierarchicalunits";

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
import files from "./files";
import documentFiles from "./documentfiles";
import episodesDocumentTypes from './episode-document-type';
import globalpacs from "./globalpacservers";
import imagelvlpacservers from "./imagelvlpacservers";
import orchestrator from "./orchestrator";
import equipment from "./equipment";
import modality from "./modality";
import shockroom from './shockroom';
import hierarchicalunitrelationships from "./hierarchicalunitrelationships";
import hierarchicalunitstaff from "./hierarchicalunitstaff";
import institutionuserpersons from "./institutionuserpersons";
import movestudies from './movestudies';
import clinichistoryaudit from './clinichistoryaudit';

import hierarchicalunitsectors from './hierarchicalunitsectors';
import rules from './rules';
import institutionalgroups from './institutionalgroups';
import institutionalgroupinstitutions from './institutionalgroupinstitutions';
import institutionalgroupusers from './institutionalgroupusers';
import institutionalgrouprules from './institutionalgrouprules';
import procedureTemplates from './proceduretemplate';
import procedureTemplateSnomeds from './proceduretemplatesnomeds';
import loincCodes from './loinc-codes';
import unitsOfMeasure from './units-of-measure';
import procedureTemplateParameters from './proceduretemplateparameters'; 


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
        ROOT, ADMINISTRADOR, ADMINISTRADOR_DE_ACCESO_DOMINIO,AUDITORIA_DE_ACCESO
    ) ? resourcesAdminRoot(permissions): resourcesAdminInstitucional(permissions);

const resources = (permissions: SGXPermissions) => [
    // staff
    <Resource name="person" {...person(permissions)} />,
    <Resource name="admin" {...admin(permissions)}/>,
    <Resource name="users" {...users}/>,
    <Resource name="roles" />,
    <Resource name="userroles" {...userroles}/>,
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
    <Resource name="shockroom" {...shockroom}/>,
    <Resource name="clinicalspecialtycarelines" {...clinicalspecialtycarelines} />,
    <Resource name="carelines" {...careLines(permissions)} />,
    <Resource name="carelineproblems" {...carelineproblems} />,
    <Resource name="carelinerole" {...carelinerole} />,
    <Resource name="carelineinstitution" {...careLineInstitution(permissions)} />,
    <Resource name="carelineinstitutionspecialty" {...carelineinstitutionspecialty(permissions)} />,
    <Resource name="carelineinstitutionpractice" {...careLineInstitutionPractice(permissions)} />,
    <Resource name="practicesinstitution" />,
    <Resource name="carelinespecialtyinstitution" />,
    <Resource name="snomedproblems" />,
    <Resource name="hierarchicalunits" {...hierarchicalunits} />,
    <Resource name="hierarchicalunitrelationships" {...hierarchicalunitrelationships} />,
    <Resource name="hierarchicalunitstaff" {...hierarchicalunitstaff} />,
    <Resource name="institutionuserpersons" {...institutionuserpersons} />,
    <Resource name="hierarchicalunitsectors" {...hierarchicalunitsectors} />,
    <Resource name="institutionalgroups" {...institutionalgroups(permissions)} />,
    <Resource name="institutionalgrouptypes" />,
    <Resource name="institutionalgroupinstitutions" {...institutionalgroupinstitutions(permissions)} />,
    <Resource name="departmentinstitutions" />,
    <Resource name="institutionalgroupusers" {...institutionalgroupusers(permissions)} />,
    <Resource name="manageruserpersons" />,
    <Resource name="institutionalgrouprules" {...institutionalgrouprules(permissions)} />,

    // debug
    <Resource name="snvs"  {...snvs} />,
    <Resource name="documentfiles" {...documentFiles(permissions)} />,
    <Resource name="files" {...files(permissions)} />,
    <Resource name="rest-client-measures" {...restClientMeasures(permissions)} />,
    <Resource name="properties" {...properties(permissions)} />,
    // masterData
    <Resource name="episodedocumenttypes" {...episodesDocumentTypes(permissions)} />,
    <Resource name="hierarchicalunittypes" {...hierarchicalunittypes(permissions)} />,
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
    <Resource name="rules" {...rules(permissions)} />,
    <Resource name="clinicalspecialtyrules" />,
    <Resource name="snomedprocedurerules" />,
    <Resource name="proceduretemplates" {...procedureTemplates(permissions)}/>,
    <Resource name="proceduretemplatesnomeds" {...procedureTemplateSnomeds(permissions)}/>,
    <Resource name="loinc-codes" {...loincCodes(permissions)} />,
    <Resource name="loinc-statuses" />,
    <Resource name="loinc-systems" />,
    <Resource name="units-of-measure" {...unitsOfMeasure(permissions)} />,
    <Resource name="proceduretemplateparameters" {...procedureTemplateParameters(permissions)} />,
    
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
    <Resource name="snomedpractices" />,
    <Resource name="pacservers" {...globalpacs(permissions)} />,
    <Resource name="pacservertypes" />,
    <Resource name="pacserverprotocols" />,
    <Resource name="pacserversimagelvl" {...imagelvlpacservers} />,
    <Resource name="orchestrator" {...orchestrator} />,
    <Resource name="equipment" {...equipment} />,
    <Resource name="modality" {...modality} />,
    <Resource name="movestudies" {...movestudies(permissions)} />,
    <Resource name="clinichistoryaudit" {...clinichistoryaudit(permissions)} />,

    <Resource name="snomedgroupconcepts" />,
    <Resource name="snomedrelatedgroups"  {...snomedrelatedgroups} />,
    <Resource name="snomedconcepts" {...snomedconcepts} />,
    <Resource name="internmentepisodes" />,
    <Resource name="healthcareprofessionals" />,

    //Extension
    <Resource name="wcDefinitionPath" {...wcDefinitionPath(permissions)} />,
];

export default resources;
