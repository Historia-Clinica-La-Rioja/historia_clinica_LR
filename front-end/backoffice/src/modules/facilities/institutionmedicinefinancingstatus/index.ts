import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import InstitutionMedicineFinancingStatusEdit from './InstitutionMedicineFinancingStatusEdit';

const institutionMedicineFinancingStatus = (permissions: SGXPermissions) => ({
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionMedicineFinancingStatusEdit : undefined
})

export default institutionMedicineFinancingStatus;