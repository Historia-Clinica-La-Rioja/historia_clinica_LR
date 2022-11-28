import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import CareLineList from './CareLineList';
import CareLineCreate from "./CareLineCreate";
import CareLineShow from "./CareLineShow";
import CareLineEdit from "./CareLineEdit";

import { ROOT, ADMINISTRADOR } from '../roles';

const careLines = (permissions: SGXPermissions) => ({
    show: CareLineShow,
    list: CareLineList,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? CareLineCreate : undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? CareLineEdit : undefined,
    options: {
        submenu: 'facilities'
    }
});

export default careLines;
