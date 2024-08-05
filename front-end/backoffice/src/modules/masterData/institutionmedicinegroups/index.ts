import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES
} from '../../roles-set';
import InstitutionMedicineGroupEdit from './InstitutionMedicineGroupEdit';
import InstitutionMedicineGroupCreate from './InstitutionMedicineGroupCreate';
import InstitutionMedicineGroupShow from './InstitutionMedicineGroupShow';

const institutionMedicineGroups = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(...ADMIN_ROLES) ? InstitutionMedicineGroupCreate : undefined,
    edit: permissions.hasAnyAssignment(...ADMIN_ROLES) ? InstitutionMedicineGroupEdit : undefined,
    show: permissions.hasAnyAssignment(...ADMIN_ROLES) ? InstitutionMedicineGroupShow : undefined
})

export default institutionMedicineGroups;