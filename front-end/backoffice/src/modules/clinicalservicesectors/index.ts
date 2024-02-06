import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import { DEFAULT_BO_ROLES } from '../roles-set';

import ClinicalServiceSectorCreate from './ClinicalServiceSectorCreate';
import ClinicalServiceSectorList from './ClinicalServiceSectorList';
import ClinicalServiceSectorShow from './ClinicalServiceSectorShow';

const clinicalservicesectors = (permissions: SGXPermissions) => ({
    show: ClinicalServiceSectorShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? ClinicalServiceSectorList : undefined,
    create: ClinicalServiceSectorCreate,
    options: {
        submenu: 'facilities'
    }
});

export default clinicalservicesectors;