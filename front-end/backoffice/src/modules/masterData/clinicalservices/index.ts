import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import ClinicalSpecialtyList from '../clinicalspecialties/ClinicalSpecialtyList';
import ClinicalSpecialtyShow from '../clinicalspecialties/ClinicalSpecialtyShow';


const clinicalservices = (permissions: SGXPermissions) => ({
    show: ClinicalSpecialtyShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? ClinicalSpecialtyList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default clinicalservices;