import SGXPermissions from "../../../libs/sgx/auth/SGXPermissions";
import { BASIC_BO_ROLES } from "../../roles-set";
import ParameterizedFormParameterCreate from "./ParameterizedFormParameterCreate";

const parameterizedFormParameter =  (permissions: SGXPermissions) => {
    const hasPermission = permissions.hasAnyAssignment(...BASIC_BO_ROLES) && permissions.isOn('HABILITAR_FORMULARIOS_CONFIGURABLES_EN_DESARROLLO');
    return {
        list: undefined,
        show: undefined,
        create: hasPermission ? ParameterizedFormParameterCreate : undefined,
        edit:undefined,
        options: {
            submenu: 'masterData'
        }
    }
};

export default parameterizedFormParameter;