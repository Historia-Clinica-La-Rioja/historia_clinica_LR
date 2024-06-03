import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import cities from './cities';
import departments from './departments';
import documentTypes from './documenttypes';
import medicalCoverage from './medicalcoverage';
import clinicalspecialties from './clinicalspecialties';
import clinicalservices from './clinicalservices';
import professionalSpecialties from './professionalspecialties';
import institutionpractices from './institutionpractices';
import procedureTemplates from './proceduretemplate';
import procedureTemplateParameters from './proceduretemplateparameters';
import procedureTemplateSnomeds from './proceduretemplatesnomeds';
import unitsOfMeasure from './units-of-measure';
import rules from './rules';
import holidays from './holidays';
import hierarchicalunittypes from './hierarchicalunittypes';
import episodesDocumentTypes from './episode-document-type';
import medicalcoverageplans from './medicalcoverageplans';
import institutionpracticesrelatedgroups from './institutionpracticesrelatedgroups';
import parameters from './parameters';
import medicineFinancingStatus from './medicinefinancingstatus';
import medicineGroups from './medicinegroups';

const resourcesMasterData = (permissions: SGXPermissions) => [
    <Resource name="episodedocumenttypes" {...episodesDocumentTypes(permissions)} />,
    <Resource name="hierarchicalunittypes" {...hierarchicalunittypes(permissions)} />,
    <Resource name="cities" {...cities(permissions)} />,
    <Resource name="departments" {...departments(permissions)} />,
    <Resource name="documenttypes" {...documentTypes(permissions)} />,
    <Resource name="medicalcoverages" {...medicalCoverage(permissions)} />,
    <Resource name="clinicalspecialties" {...clinicalspecialties(permissions)} />,
    <Resource name="clinicalservices" {...clinicalservices(permissions)} />,
    <Resource name="professionalspecialties" {...professionalSpecialties(permissions)} />,
    <Resource name="holidays" {...holidays(permissions)} />,
    <Resource name="rules" {...rules(permissions)} />,
    <Resource name="proceduretemplates" {...procedureTemplates(permissions)}/>,
    <Resource name="proceduretemplatesnomeds" {...procedureTemplateSnomeds(permissions)}/>,
    <Resource name="proceduretemplatesnomedgroup"/>,
    <Resource name="units-of-measure" {...unitsOfMeasure(permissions)} />,
    <Resource name="proceduretemplateparameters" {...procedureTemplateParameters(permissions)} />,
    <Resource name="institutionpractices" {...institutionpractices(permissions)} />,
    <Resource name="medicalcoverageplans" {...medicalcoverageplans} />,
    <Resource name="medicalcoveragetypes" />,
    <Resource name="institutionpracticesrelatedgroups" {...institutionpracticesrelatedgroups} />,
    <Resource name="medicalcoveragesmerge" />,
    <Resource name="parameters" {...parameters(permissions)} />,
    <Resource name="medicinefinancingstatus" {...medicineFinancingStatus(permissions)} />,
    <Resource name="medicinegroups" {...medicineGroups(permissions)} />
];

export default resourcesMasterData;