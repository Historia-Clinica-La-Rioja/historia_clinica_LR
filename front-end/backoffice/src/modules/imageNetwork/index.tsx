import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import globalpacs from './globalpacservers';
import imagelvlpacservers from './imagelvlpacservers';
import orchestrator from './orchestrator';
import equipment from './equipment';
import movestudies from './movestudies';
import resultstudies from './resultstudies';
import allmovestudies from './allmovestudies';

const resourcesImageNetwork = (permissions: SGXPermissions) => [
    <Resource name="pacservers" {...globalpacs(permissions)} />,
    <Resource name="pacserversimagelvl" {...imagelvlpacservers(permissions)} />,
    <Resource name="orchestrator" {...orchestrator(permissions)} />,
    <Resource name="equipment" {...equipment(permissions)} />,
    <Resource name="movestudies" {...movestudies(permissions)} />,
    <Resource name="resultstudies" {...resultstudies(permissions)} />,
    <Resource name="allmovestudies" {...allmovestudies(permissions)} />,
    <Resource name="pacservertypes" />,
    <Resource name="pacserverprotocols" />,
];

export default resourcesImageNetwork;