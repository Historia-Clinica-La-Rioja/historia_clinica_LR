import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import MedicalCoverageList from './MedicalCoverageList';
import MedicalCoverageShow from './MedicalCoverageShow';
import MedicalCoverageCreate from './MedicalCoverageCreate';
import MedicalCoverageEdit from './MedicalCoverageEdit';

const medicalCoverage = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? MedicalCoverageList : undefined,
    show: MedicalCoverageShow,
    create: MedicalCoverageCreate,
    edit: MedicalCoverageEdit,
    options: {
        submenu: 'masterData'
    }
});

export default medicalCoverage;