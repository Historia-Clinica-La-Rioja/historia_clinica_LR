import React from 'react';
import { Resource } from 'react-admin';
import SGXPermissions from '../libs/sgx/auth/SGXPermissions';

//submenu
import resourcesStaff from './staff';
import resourcesFacilities from './facilities';
import resourcesMasterData from './masterData';
import resourcesTerminology from './terminology';
import resourcesImageNetwork from './imageNetwork';
import resourcesReservaTurnos from './booking';
import resourcesMore from './more';
import resourcesDebug from './debug';

const resources = (permissions: SGXPermissions) => [
    ...resourcesStaff(permissions),
    ...resourcesFacilities(permissions),
    ...resourcesReservaTurnos(permissions),
    ...resourcesMasterData(permissions),
    ...resourcesTerminology(permissions),
    ...resourcesImageNetwork(permissions),
    ...resourcesMore(permissions),
    ...resourcesDebug(permissions),
    <Resource name="practicesinstitution" />,
    <Resource name="carelinespecialtyinstitution" />,
    <Resource name="snomedproblems" />,
    <Resource name="institutionalgrouptypes" />,
    <Resource name="departmentinstitutions" />,
    <Resource name="manageruserpersons" />,
    <Resource name="snomedgrouptypes" />,
    <Resource name="clinicalspecialtyrules" />,
    <Resource name="snomedprocedurerules" />,
    <Resource name="loinc-statuses" />,
    <Resource name="loinc-systems" />,
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
    <Resource name="internmentepisodes" />,
    <Resource name="healthcareprofessionals" />,
];

export default resources;
