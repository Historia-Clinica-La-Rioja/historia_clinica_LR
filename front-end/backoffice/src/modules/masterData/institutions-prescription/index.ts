import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import InstitutionPrescriptionList from './InstitutionPrescriptionList';

const institutionsPrescription = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionPrescriptionList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default institutionsPrescription;
