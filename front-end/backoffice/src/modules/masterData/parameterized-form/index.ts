import SGXPermissions from "../../../libs/sgx/auth/SGXPermissions";
import { BASIC_BO_ROLES } from "../../roles-set";
import ParameterizedFormCreate from "./ParameterizedFormCreate";
import ParameterizedFormEdit from "./ParameterizedFormEdit";
import ParameterizedFormList from "./ParameterizedFormList";
import ParameterizedFormShow from "./ParameterizedFormShow";

const parameterizedForm =  (permissions: SGXPermissions) => {
    const hasPermission = permissions.hasAnyAssignment(...BASIC_BO_ROLES) && permissions.isOn('HABILITAR_FORMULARIOS_CONFIGURABLES_EN_DESARROLLO');
    return {
        list: hasPermission ? ParameterizedFormList : undefined,
        show: hasPermission ? ParameterizedFormShow : undefined,
        create: hasPermission ? ParameterizedFormCreate : undefined,
        edit: hasPermission ? ParameterizedFormEdit : undefined,
        options: {
            submenu: 'masterData'
        }
    }
};

export default parameterizedForm;