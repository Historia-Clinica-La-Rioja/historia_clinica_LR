import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import ProcedureTemplateList from './ProcedureTemplateList';
import ProcedureTemplateShow from './ProcedureTemplateShow';
import ProcedureTemplateEdit from './ProcedureTemplateEdit';
import ProcedureTemplateCreate from './ProcedureTemplateCreate';
import { ROOT, ADMINISTRADOR } from '../roles';

const procedureTemplates = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProcedureTemplateList : undefined,
    show: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProcedureTemplateShow : undefined,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProcedureTemplateCreate : undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProcedureTemplateEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default procedureTemplates;