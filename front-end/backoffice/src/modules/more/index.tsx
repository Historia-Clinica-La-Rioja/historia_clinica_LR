import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import wcDefinitionPath from './wcDefinitionPathCreate';
import vclinichistoryaudit from './vclinichistoryaudit';
import cipresencounters from './cipresencounters';

const resourcesMore = (permissions: SGXPermissions) => [
    <Resource name="wcDefinitionPath" {...wcDefinitionPath(permissions)} />,
    <Resource name="vclinichistoryaudit" {...vclinichistoryaudit(permissions)} />,
    <Resource name="cipresencounters" {...cipresencounters(permissions)}/>,
];

export default resourcesMore;