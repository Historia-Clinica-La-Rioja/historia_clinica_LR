import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMINISTRADOR_DE_ACCESO_DOMINIO,
} from '../../roles';

import InstitutionalGroupInstitutionCreate from './InstitutionalGroupInstitutionCreate';


const institutionalgroupinstitutions = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupInstitutionCreate : undefined
})

export default institutionalgroupinstitutions;
