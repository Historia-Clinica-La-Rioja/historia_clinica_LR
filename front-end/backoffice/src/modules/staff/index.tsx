import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import person from './person';
import admin from './admin';
import users from './users';
import userroles from './userroles';
import passwordReset from './password-reset';
import professionalprofessions from './professionalprofessions';
import healthcareprofessionalspecialties from './healthcareprofessionalspecialties';
import healthcareprofessionallicensenumbers from './healthcareprofessionallicensenumbers';
import licensenumbertypes from './licensenumbertypes';
import healthcareprofessionalspecialtylicensenumbers from './healthcareprofessionalspecialtylicensenumbers';

export const resourcesStaff = (permissions: SGXPermissions) => [
    <Resource name="person" {...person(permissions)} />,
    <Resource name="admin" {...admin(permissions)}/>,
    <Resource name="users" {...users}/>,
    <Resource name="userroles" {...userroles}/>,
    <Resource name="roles" />,
    <Resource name="password-reset" {...passwordReset(permissions)} />,
    <Resource name="professionalprofessions" {...professionalprofessions} />,
    <Resource name="healthcareprofessionalspecialties" {...healthcareprofessionalspecialties} />,
    <Resource name="healthcareprofessionallicensenumbers" {...healthcareprofessionallicensenumbers} />,
    <Resource name="healthcareprofessionalspecialtylicensenumbers" {...healthcareprofessionalspecialtylicensenumbers} />,
    <Resource name="licensenumbertypes" {...licensenumbertypes} />,
];

export const resourcesForAdministradorDeDatosPersonales = (permissions: SGXPermissions) => [
    <Resource name="person" {...person(permissions)} />,
    <Resource name="identificationTypes" />,
    <Resource name="genders" />,
];