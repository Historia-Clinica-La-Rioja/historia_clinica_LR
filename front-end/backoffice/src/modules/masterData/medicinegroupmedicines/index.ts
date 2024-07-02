import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import MedicineGroupMedicineCreate from './MedicineGroupMedicineCreate';


const medicineGroupMedicines = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineGroupMedicineCreate : undefined
});

export default medicineGroupMedicines;