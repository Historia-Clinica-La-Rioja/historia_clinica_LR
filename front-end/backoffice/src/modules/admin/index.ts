import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import AccountBoxIcon from '@material-ui/icons/AccountBox';

import AdminList from './AdminList';
import AdminShow from './AdminShow';

import { ROOT, ADMINISTRADOR } from '../roles';

const admin = (permissions: SGXPermissions) => ({
    icon: AccountBoxIcon,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? AdminList : undefined,
    show: AdminShow,
    options: {
        submenu: 'staff'
    }
});

export default admin;
