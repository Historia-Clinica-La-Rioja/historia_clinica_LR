import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import { ROOT, ADMINISTRADOR } from '../roles';
import ProcedureTemplateSnomedCreate from './ProcedureTemplateSnomedCreate';

const procedureTemplateSnomeds = (permissions: SGXPermissions) => {
    const enabled = permissions.hasAnyAssignment(ROOT, ADMINISTRADOR);
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