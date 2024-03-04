import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import CareLineList from './CareLineList';
import CareLineCreate from './CareLineCreate';
import CareLineShow from './CareLineShow';
import CareLineEdit from './CareLineEdit';

const careLines = (permissions: SGXPermissions) => ({
    show: CareLineShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? CareLineList : undefined,
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? CareLineCreate : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? CareLineEdit : undefined,
    options: {
        submenu: 'facilities'
    }
});

export default careLines;
