import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import ClinicalSpecialtyShow from './ClinicalSpecialtyShow';
import ClinicalSpecialtyList from './ClinicalSpecialtyList';

import { ROOT, ADMINISTRADOR } from '../roles';

const clinicalspecialties = (permissions: SGXPermissions) => ({
    show: ClinicalSpecialtyShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ClinicalSpecialtyList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default clinicalspecialties;
