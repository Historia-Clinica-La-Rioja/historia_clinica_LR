import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import ClinicalSpecialtyShow from './ClinicalSpecialtyShow';
import ClinicalSpecialtyList from './ClinicalSpecialtyList';

const clinicalspecialties = (permissions: SGXPermissions) => ({
    show: ClinicalSpecialtyShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? ClinicalSpecialtyList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default clinicalspecialties;
