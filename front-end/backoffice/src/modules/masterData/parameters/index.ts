import SGXPermissions from "../../../libs/sgx/auth/SGXPermissions";
import { BASIC_BO_ROLES } from "../../roles-set";
import ParameterCreate from "./ParameterCreate";
import ParameterEdit from "./ParameterEdit";
import ParameterList from "./ParameterList";
import ParameterShow from "./ParameterShow";


const parameters =  (permissions: SGXPermissions) => {
    const hasPermission = permissions.hasAnyAssignment(...BASIC_BO_ROLES)  && permissions.isOn('HABILITAR_FORMULARIOS_CONFIGURABLES_EN_DESARROLLO');
    return {
        list: hasPermission ? ParameterList : undefined,
        show: hasPermission ? ParameterShow : undefined,
        create: hasPermission ? ParameterCreate : undefined,
        edit: hasPermission ? ParameterEdit : undefined,
        options: {
            submenu: 'masterData'
        }
    }
};

export default parameters;