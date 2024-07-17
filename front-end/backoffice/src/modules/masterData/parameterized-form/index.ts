import SGXPermissions from "../../../libs/sgx/auth/SGXPermissions";
import { ADMIN_ROLES } from "../../roles-set";
import ParameterizedFormCreate from "./ParameterizedFormCreate";
import ParameterizedFormEdit from "./ParameterizedFormEdit";
import ParameterizedFormList from "./ParameterizedFormList";
import ParameterizedFormShow from "./ParameterizedFormShow";

const parameterizedform =  (permissions: SGXPermissions) => {
    const hasPermission = permissions.hasAnyAssignment(...ADMIN_ROLES) && permissions.isOn('HABILITAR_FORMULARIOS_CONFIGURABLES_EN_DESARROLLO');
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

export default parameterizedform;