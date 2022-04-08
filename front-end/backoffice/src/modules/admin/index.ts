import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import AccountBoxIcon from '@material-ui/icons/AccountBox';

import AdminList from './AdminList';
import AdminEdit from './AdminEdit';

import { ROOT, ADMINISTRADOR } from '../roles';

const admin = (permissions: SGXPermissions) => ({
    icon: AccountBoxIcon,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? AdminList : undefined,
    edit: AdminEdit,
    options: {
        submenu: 'staff'
    }
});

export default admin;
