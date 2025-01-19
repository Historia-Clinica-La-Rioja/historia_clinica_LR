import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import MedicineGroupList from './MedicineGroupList';
import MedicineGroupCreate from './MedicineGroupCreate'; 
import MedicineGroupShow from './MedicineGroupShow'; 
import MedicineGroupEdit from './MedicineGroupEdit';


const medicineGroups = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineGroupList : undefined,
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineGroupCreate : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineGroupEdit : undefined,
    show: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineGroupShow : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default medicineGroups;