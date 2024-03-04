
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    DEFAULT_BO_ROLES,
} from '../../roles-set';
import PersonShow from './PersonShow';
import PersonList from './PersonList';

const person = (permissions: SGXPermissions) => ({
    show: PersonShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? PersonList : undefined,
    options: {
        submenu: 'staff'
    }
});

export default person;
