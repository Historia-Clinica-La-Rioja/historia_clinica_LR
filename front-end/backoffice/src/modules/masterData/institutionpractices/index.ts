import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    DEFAULT_BO_ROLES,
} from '../../roles-set';

import InstitutionPracticesList from './InstitutionPracticesList';
import InstitutionPracticesCreate from './InstitutionPracticesCreate';
import InstitutionPracticesShow from './InstitutionPracticesShow';
import InstitutionPracticesEdit from './InstitutionPracticesEdit';

const institutionpractices = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? InstitutionPracticesList : undefined,
    show: InstitutionPracticesShow,
    create: InstitutionPracticesCreate,
    edit: InstitutionPracticesEdit,
    options: {
        submenu: 'masterData'
    }
});

export default institutionpractices;
