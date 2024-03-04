import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import ProcedureTemplateParameterCreate from './ProcedureTemplateParameterCreate';
import ProcedureTemplateParameterEdit from './ProcedureTemplateParameterEdit';
import ProcedureTemplateParameterShow from './ProcedureTemplateParameterShow';

const procedureTemplateParameters = (permissions: SGXPermissions) => {
    const enabled = permissions.hasAnyAssignment(...BASIC_BO_ROLES);
    return {
        list: undefined,
        show: enabled ? ProcedureTemplateParameterShow : undefined,
        create: enabled ? ProcedureTemplateParameterCreate : undefined,
        edit: enabled ? ProcedureTemplateParameterEdit : undefined,
    }
};

export default procedureTemplateParameters;