import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

import ClinicalServiceSectorCreate from './ClinicalServiceSectorCreate';
import ClinicalServiceSectorList from './ClinicalServiceSectorList';
import ClinicalServiceSectorShow from './ClinicalServiceSectorShow';

const clinicalservicesectors = (permissions: SGXPermissions) => ({
    show: ClinicalServiceSectorShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? ClinicalServiceSectorList : undefined,
    create: ClinicalServiceSectorCreate,
    options: {
        submenu: 'facilities'
    }
});

export default clinicalservicesectors;