import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import PersonShow from './PersonShow';
import PersonList from './PersonList';

import { ROOT, ADMINISTRADOR, ADMINISTRADOR_DE_ACCESO_DOMINIO } from '../roles';

const person = (permissions: SGXPermissions) => ({
    show: PersonShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR, ADMINISTRADOR_DE_ACCESO_DOMINIO) ? PersonList : undefined,
    options: {
        submenu: 'staff'
    }
});

export default person;
