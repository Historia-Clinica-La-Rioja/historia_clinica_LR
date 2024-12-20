import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import snvs from './snvs';
import properties from './properties';
import restClientMeasures from './rest-client-measures';
import files from './files';
import documentFiles from './documentfiles';
import reportQueue from './report-queue';
import sourceTypes from './sourcetypes';

const resourcesFacilities = (permissions: SGXPermissions) => [
    <Resource name="snvs"  {...snvs(permissions)} />,
    <Resource name="documentfiles" {...documentFiles(permissions)} />,
    <Resource name="files" {...files(permissions)} />,
    <Resource name="rest-client-measures" {...restClientMeasures(permissions)} />,
    <Resource name="properties" {...properties(permissions)} />,
    <Resource name="report-queue" {...reportQueue(permissions)} />,
    <Resource name="sourcetypes" {...sourceTypes} />,
];

export default resourcesFacilities;