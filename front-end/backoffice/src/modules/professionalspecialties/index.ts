import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import ProfessionalSpecialtyShow from './show';
import ProfessionalSpecialtyList from './list';

import { ROOT, ADMINISTRADOR } from '../roles';

const professionalSpecialties = (permissions: SGXPermissions) => ({
    show: ProfessionalSpecialtyShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ProfessionalSpecialtyList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default professionalSpecialties;
