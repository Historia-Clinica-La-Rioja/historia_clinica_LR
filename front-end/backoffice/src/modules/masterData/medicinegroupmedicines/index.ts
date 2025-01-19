import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES
} from '../../roles-set';
import MedicineGroupMedicineCreate from './MedicineGroupMedicineCreate';


const medicineGroupMedicines = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(...ADMIN_ROLES) ? MedicineGroupMedicineCreate : undefined
});

export default medicineGroupMedicines;