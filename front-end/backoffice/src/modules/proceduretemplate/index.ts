import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import ProcedureTemplateList from './ProcedureTemplateList';
import ProcedureTemplateShow from './ProcedureTemplateShow';
import ProcedureTemplateEdit from './ProcedureTemplateEdit';
import ProcedureTemplateCreate from './ProcedureTemplateCreate';
import { ROOT, ADMINISTRADOR } from '../roles';

const procedureTemplates = (permissions: SGXPermissions) => {
    const enabled = permissions.hasAnyAssignment(ROOT, ADMINISTRADOR);
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