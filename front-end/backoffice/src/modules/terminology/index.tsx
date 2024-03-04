import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import snomedgroups from './snomedgroups';
import snomedGroupConcept from './snomedgroupconcepts';
import loincCodes from './loinc-codes';
import snomedconcepts from './snomedconcepts';
import snomedrelatedgroups from './snomedrelatedgroups';

const resourcesTerminology = (permissions: SGXPermissions) => [
    <Resource name="snomedgroups"   {...snomedgroups(permissions)} />,
    <Resource name="snomedgroupconcepts" {...snomedGroupConcept(permissions)}/>,
    <Resource name="loinc-codes" {...loincCodes(permissions)} />,
    <Resource name="snomedrelatedgroups"  {...snomedrelatedgroups} />,
    <Resource name="snomedconcepts" {...snomedconcepts} />,
    <Resource name="snomedpractices" />,
];

export default resourcesTerminology;