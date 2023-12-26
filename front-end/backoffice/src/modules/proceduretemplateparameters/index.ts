import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import { ROOT, ADMINISTRADOR } from '../roles';
import ProcedureTemplateParameterCreate from './ProcedureTemplateParameterCreate';
import ProcedureTemplateParameterShow from './ProcedureTemplateParameterShow';


const procedureTemplateParameters = (permissions: SGXPermissions) => {
    const enabled = permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) && permissions.isOn('HABILITAR_RESULTADOS_DE_ESTUDIO_EN_DESAROLLO')
    return {
        list: undefined,
        show: enabled ? ProcedureTemplateParameterShow : undefined,
        create: enabled ? ProcedureTemplateParameterCreate : undefined,
        edit: undefined
    }
};

export default procedureTemplateParameters;