import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import institutions from './institutions';
import sectors from './sectors';
import rootSectors from './root-sectors';
import clinicalservicesectors from './clinicalservicesectors';
import doctorsoffices from './doctorsoffices';
import rooms from './rooms';
import beds from './beds';
import shockroom from './shockroom';
import careLines from './carelines';
import careLineInstitution from './carelineinstitution';
import hierarchicalunits from './hierarchicalunits';
import modality from './modality';
import carelineinstitutionspecialty from './carelineinstitutionspecialty';
import clinicalspecialtycarelines from './clinicalspecialtycarelines';
import careLineInstitutionPractice from './carelineinstitutionpractice';
import addresses from './addresses';
import carelineproblems from './carelineproblems';
import carelinerole from './carelinerole';
import hierarchicalunitrelationships from './hierarchicalunitrelationships';
import hierarchicalunitstaff from './hierarchicalunitstaff';
import institutionuserpersons from './institutionuserpersons';
import hierarchicalunitsectors from './hierarchicalunitsectors';
import institutionalgroups from './institutionalgroups';
import institutionalgroupinstitutions from './institutionalgroupinstitutions';
import institutionalgroupusers from './institutionalgroupusers';
import institutionalgrouprules from './institutionalgrouprules';

const resourcesFacilities = (permissions: SGXPermissions) => [
    <Resource name="institutions" {...institutions(permissions)} />,
    <Resource name="sectors" {...sectors(permissions)} />,
    <Resource name="rootsectors" {...rootSectors} />,
    <Resource name="clinicalservicesectors" {...clinicalservicesectors(permissions)} />,
    <Resource name="doctorsoffices" {...doctorsoffices(permissions)} />,
    <Resource name="rooms" {...rooms(permissions)} />,
    <Resource name="beds" {...beds(permissions)} />,
    <Resource name="shockroom" {...shockroom(permissions)}/>,
    <Resource name="carelines" {...careLines(permissions)} />,
    <Resource name="carelineinstitution" {...careLineInstitution(permissions)} />,
    <Resource name="hierarchicalunits" {...hierarchicalunits(permissions)} />,
    <Resource name="modality" {...modality} />,
    <Resource name="carelineinstitutionspecialty" {...carelineinstitutionspecialty(permissions)} />,
    <Resource name="clinicalspecialtycarelines" {...clinicalspecialtycarelines} />,
    <Resource name="carelineinstitutionpractice" {...careLineInstitutionPractice(permissions)} />,
    <Resource name="addresses" {...addresses} />,
    <Resource name="carelineproblems" {...carelineproblems} />,
    <Resource name="carelinerole" {...carelinerole} />,
    <Resource name="hierarchicalunitrelationships" {...hierarchicalunitrelationships} />,
    <Resource name="hierarchicalunitstaff" {...hierarchicalunitstaff} />,
    <Resource name="institutionuserpersons" {...institutionuserpersons} />,
    <Resource name="hierarchicalunitsectors" {...hierarchicalunitsectors} />,
    <Resource name="institutionalgroups" {...institutionalgroups(permissions)} />,
    <Resource name="institutionalgroupinstitutions" {...institutionalgroupinstitutions(permissions)} />,
    <Resource name="institutionalgroupusers" {...institutionalgroupusers(permissions)} />,
    <Resource name="institutionalgrouprules" {...institutionalgrouprules(permissions)} />,
    <Resource name="institutionalparameterizedform" />,
];

export default resourcesFacilities;