import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

import InstitutionalGroupList from './InstitutionalGroupList';
import InstitutionalGroupCreate from './InstitutionalGroupCreate';
import InstitutionalGroupShow from './InstitutionalGroupShow';
import InstitutionalGroupEdit from './InstitutionalGroupEdit';

import { ADMINISTRADOR_DE_ACCESO_DOMINIO } from '../../roles';


const institutionalgroups = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupShow: undefined,
    edit: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupEdit : undefined,
    create: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupCreate: undefined,
    list: permissions.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) ? InstitutionalGroupList : undefined,
    options: {
        submenu: 'facilities'
    }
})

export default institutionalgroups;