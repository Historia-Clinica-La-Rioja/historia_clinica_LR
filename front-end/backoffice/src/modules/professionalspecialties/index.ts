import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import ProfessionalSpecialtyShow from './show';
import ProfessionalSpecialtyList from './list';
import ProfessionalSpecialtyCreate from './create';
import ProfessionalSpecialtyEdit from './edit';

import { ROOT, ADMINISTRADOR } from '../roles';

const professionalSpecialties = (permissions: SGXPermissions) => ({
    show: ProfessionalSpecialtyShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProfessionalSpecialtyList : undefined,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProfessionalSpecialtyCreate : undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProfessionalSpecialtyEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default professionalSpecialties;
