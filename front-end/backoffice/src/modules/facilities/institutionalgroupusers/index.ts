import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

import InstitutionalGroupUserCreate from './InstitutionalGroupUserCreate';

import { ADMINISTRADOR_DE_ACCESO_DOMINIO } from '../../roles';

const institutionalgroupusers = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupUserCreate : undefined
})

export default institutionalgroupusers;
