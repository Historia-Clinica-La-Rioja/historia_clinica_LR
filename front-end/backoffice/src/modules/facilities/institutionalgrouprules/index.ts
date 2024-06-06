import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

import InstitutionalGroupRuleCreate from './InstitutionalGroupRuleCreate';
import InstitutionalGroupRuleEdit from './InstitutionalGroupRuleEdit';

import { ADMINISTRADOR_DE_ACCESO_DOMINIO } from '../../roles';

const institutionalgrouprules = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupRuleCreate : undefined,
    edit: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupRuleEdit : undefined
})

export default institutionalgrouprules;