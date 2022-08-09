import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";
import WcDefinitionPathCreate from "./WcDefinitionPathCreate";
import WcDefinitionPathList from "./WcDefinitionPathList";



const check = (permissions: SGXPermissions) =>
    permissions.isOn('HABILITAR_EXTENSIONES_WEB_COMPONENTS');

const wcDefinitionPath = (permissions: SGXPermissions) => (
    {
        list: check(permissions) ? WcDefinitionPathList : undefined,
        create: WcDefinitionPathCreate,
        options: {
            submenu: 'more'
        }
    });


export default wcDefinitionPath;
