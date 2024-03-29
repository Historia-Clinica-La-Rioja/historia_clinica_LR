import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import PersonShow from './PersonShow';
import PersonList from './PersonList';

import {
    ROOT,
    ADMINISTRADOR,
    ADMINISTRADOR_DE_ACCESO_DOMINIO,
    AUDITORIA_DE_ACCESO
} from '../roles';

const person = (permissions: SGXPermissions) => ({
    show: PersonShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR, ADMINISTRADOR_DE_ACCESO_DOMINIO,AUDITORIA_DE_ACCESO) ? PersonList : undefined,
    options: {
        submenu: 'staff'
    }
});

export default person;
