import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import ClinicalSpecialtyShow from './ClinicalSpecialtyShow';
import ClinicalSpecialtyList from './ClinicalSpecialtyList';
import ClinicalSpecialtyCreate from './ClinicalSpecialtyCreate';
import ClinicalSpecialtyEdit from './ClinicalSpecialtyEdit';

import { ROOT, ADMINISTRADOR } from '../roles';

const clinicalspecialties = (permissions: SGXPermissions) => ({
    show: ClinicalSpecialtyShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ClinicalSpecialtyList : undefined,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ClinicalSpecialtyCreate : undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ClinicalSpecialtyEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default clinicalspecialties;
