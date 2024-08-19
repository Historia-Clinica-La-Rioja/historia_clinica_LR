import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import MedicineFinancingStatusList from './MedicineFinancingStatusList';
import MedicineFinancingStatusEdit from './MedicineFinancingStatusEdit';
import MedicineFinancingStatusShow from './MedicineFinancingStatusShow'; 

const medicineFinancingStatus = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineFinancingStatusList : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineFinancingStatusEdit : undefined,
    show: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicineFinancingStatusShow : undefined,
    create: undefined,
    options: {
        submenu: 'masterData'
    }
});

export default medicineFinancingStatus;