import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";
import WcDefinitionPathCreate from "./WcDefinitionPathCreate";
import WcDefinitionPathEdit from "./WcDefinitionPathEdit";
import WcDefinitionPathList from "./WcDefinitionPathList";
import WcDefinitionPathShow from "./WcDefinitionPathShow";



const check = (permissions: SGXPermissions) =>
    permissions.isOn('HABILITAR_EXTENSIONES_WEB_COMPONENTS');

const wcDefinitionPath = (permissions: SGXPermissions) => (
    {
        list: check(permissions) ? WcDefinitionPathList : undefined,
        create: WcDefinitionPathCreate,
        show: WcDefinitionPathShow,
        edit: WcDefinitionPathEdit,
        options: {
            submenu: 'more'
        },
    });


export default wcDefinitionPath;
