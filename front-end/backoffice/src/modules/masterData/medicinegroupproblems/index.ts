import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import MedicineGroupProblemCreate from './MedicineGroupProblemCreate';


const medicineGroupProblems = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineGroupProblemCreate : undefined
});

export default medicineGroupProblems;