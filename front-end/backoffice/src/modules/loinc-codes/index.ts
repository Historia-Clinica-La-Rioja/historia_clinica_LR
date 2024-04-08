import LoincCodeShow from './LoincCodeShow';
import LoincCodeList from './LoincCodeList';
import LoincCodeEdit from './LoincCodeEdit';
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";
import { ROOT, ADMINISTRADOR } from '../roles';
const loincCodes = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? LoincCodeList : undefined,
    show: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? LoincCodeShow : undefined,
    create: undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ?  LoincCodeEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});
export default loincCodes;
