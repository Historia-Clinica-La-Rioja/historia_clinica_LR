import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import DepartmentList from './DepartmentList';
import DepartmentShow from './DepartmentShow';

const departments = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? DepartmentList : undefined,
    show: DepartmentShow,
    options: {
        submenu: 'masterData'
    }
});

export default departments;
