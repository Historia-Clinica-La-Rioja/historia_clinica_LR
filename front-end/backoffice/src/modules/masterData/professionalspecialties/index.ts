import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import ProfessionalSpecialtyShow from './show';
import ProfessionalSpecialtyList from './list';

const professionalSpecialties = (permissions: SGXPermissions) => ({
    show: ProfessionalSpecialtyShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? ProfessionalSpecialtyList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default professionalSpecialties;
