import AccountBoxIcon from '@material-ui/icons/AccountBox';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import AdminList from './AdminList';
import AdminShow from './AdminShow';

const admin = (permissions: SGXPermissions) => ({
    icon: AccountBoxIcon,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? AdminList : undefined,
    show: AdminShow,
    options: {
        submenu: 'staff'
    }
});

export default admin;
