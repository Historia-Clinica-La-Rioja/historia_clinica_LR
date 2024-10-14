import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES
} from '../../roles-set';
import InstitutionMedicineFinancingStatusEdit from './InstitutionMedicineFinancingStatusEdit';

const institutionMedicineFinancingStatus = (permissions: SGXPermissions) => ({
    edit: permissions.hasAnyAssignment(...ADMIN_ROLES) ? InstitutionMedicineFinancingStatusEdit : undefined
})

export default institutionMedicineFinancingStatus;