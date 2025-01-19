import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import ProcedureTemplateSnomedCreate from './ProcedureTemplateSnomedCreate';

const procedureTemplateSnomeds = (permissions: SGXPermissions) => {
    const enabled = permissions.hasAnyAssignment(...BASIC_BO_ROLES);
    return {
        list: undefined,
        show: undefined,
        create: enabled ? ProcedureTemplateSnomedCreate : undefined,
        edit: undefined,
        options: {
            submenu: 'masterData'
        }
    }
};

export default procedureTemplateSnomeds;