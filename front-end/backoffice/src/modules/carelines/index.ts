import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import CareLineList from './CareLineList';
import CareLineCreate from "./CareLineCreate";
import CareLineShow from "./CareLineShow";
import CareLineEdit from "./CareLineEdit";

import { ROOT, ADMINISTRADOR } from '../roles';

const careLines = (permissions: SGXPermissions) => ({
    show: CareLineShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? CareLineList : undefined,
    create: CareLineCreate,
    edit: CareLineEdit,
    options: {
        submenu: 'facilities'
    }
});

export default careLines;
