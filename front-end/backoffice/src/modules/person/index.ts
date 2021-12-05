import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import PersonShow from './PersonShow';
import PersonList from './PersonList';

const ROLE_ROOT = { role: 'ROOT', institutionId: -1 };
const ROLE_ADMINISTRADOR = { role: 'ADMINISTRADOR', institutionId: -1 };

const person = (permissions: SGXPermissions) => ({
    show: PersonShow,
    list: permissions.hasAnyAssignment(ROLE_ROOT, ROLE_ADMINISTRADOR) ? PersonList : undefined
});

export default person;
