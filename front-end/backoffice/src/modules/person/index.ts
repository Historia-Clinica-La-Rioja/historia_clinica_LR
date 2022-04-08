import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import PersonShow from './PersonShow';
import PersonList from './PersonList';

import { ROOT, ADMINISTRADOR } from '../roles';

const person = (permissions: SGXPermissions) => ({
    show: PersonShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? PersonList : undefined,
    options: {
        submenu: 'staff'
    }
});

export default person;
