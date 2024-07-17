import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import InstitutionMedicineGroupEdit from './InstitutionMedicineGroupEdit';
import InstitutionMedicineGroupCreate from './InstitutionMedicineGroupCreate';
import InstitutionMedicineGroupShow from './InstitutionMedicineGroupShow';

const institutionMedicineGroups = (permissions: SGXPermissions) => ({
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionMedicineGroupCreate : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionMedicineGroupEdit : undefined,
    show: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionMedicineGroupShow : undefined
})

export default institutionMedicineGroups;