import GlobalPacsList from "./GlobalPacsList";
import GlobalPacsCreate from "./GlobalPacsCreate";
import GlobalPacsShow from "./GlobalPacsShow";
import GlobalPacsEdit from "./GlobalPacsEdit";
import {ROOT} from "../roles";
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";
import RouterIcon from "@material-ui/icons/Router";


const globalpacs = (permissions: SGXPermissions) => ({
    icon: RouterIcon,
    list: permissions.hasAnyAssignment(ROOT) ? GlobalPacsList : undefined,
    show: permissions.hasAnyAssignment(ROOT) ? GlobalPacsShow : undefined,
    create: permissions.hasAnyAssignment(ROOT) ? GlobalPacsCreate : undefined,
    edit: permissions.hasAnyAssignment(ROOT) ? GlobalPacsEdit : undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default globalpacs;