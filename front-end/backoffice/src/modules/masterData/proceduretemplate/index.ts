import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import ProcedureTemplateList from './ProcedureTemplateList';
import ProcedureTemplateShow from './ProcedureTemplateShow';
import ProcedureTemplateEdit from './ProcedureTemplateEdit';
import ProcedureTemplateCreate from './ProcedureTemplateCreate';

const procedureTemplates = (permissions: SGXPermissions) => {
    const enabled = permissions.hasAnyAssignment(...BASIC_BO_ROLES);
    return {
        list: enabled ? ProcedureTemplateList : undefined,
        show: enabled ? ProcedureTemplateShow : undefined,
        create: enabled ? ProcedureTemplateCreate : undefined,
        edit: enabled ? ProcedureTemplateEdit : undefined,
        options: {
            submenu: 'masterData'
        }
    }
};

export default procedureTemplates;