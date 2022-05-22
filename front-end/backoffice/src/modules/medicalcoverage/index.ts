import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import MedicalCoverageList from './MedicalCoverageList';
import MedicalCoverageShow from './MedicalCoverageShow';
import MedicalCoverageCreate from './MedicalCoverageCreate';
import MedicalCoverageEdit from './MedicalCoverageEdit';

import { ROOT, ADMINISTRADOR } from '../roles';

const medicalCoverage = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? MedicalCoverageList : undefined,
    show: MedicalCoverageShow,
    create: MedicalCoverageCreate,
    edit: MedicalCoverageEdit,
    options: {
        submenu: 'masterData'
    }
});

export default medicalCoverage;