import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import InstitutionalGroupInstitutionCreate from "./InstitutionalGroupInstitutionCreate";

import { ADMINISTRADOR_DE_ACCESO_DOMINIO } from "../roles";

const institutionalgroupinstitutions = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupInstitutionCreate : undefined
})

export default institutionalgroupinstitutions;
