import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DepartmentList from './DepartmentList';
import DepartmentShow from './DepartmentShow';

import { ROOT, ADMINISTRADOR } from '../roles';

const departments = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? DepartmentList : undefined,
    show: DepartmentShow,
    options: {
        submenu: 'masterData'
    }
});

export default departments;
