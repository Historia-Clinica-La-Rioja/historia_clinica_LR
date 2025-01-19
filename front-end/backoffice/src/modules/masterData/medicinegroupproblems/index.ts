import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES,
} from '../../roles-set';
import MedicineGroupProblemCreate from './MedicineGroupProblemCreate';


const medicineGroupProblems = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(...ADMIN_ROLES) ? MedicineGroupProblemCreate : undefined
});

export default medicineGroupProblems;