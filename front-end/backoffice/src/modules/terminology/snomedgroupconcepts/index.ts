
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    DEFAULT_BO_ROLES,
} from '../../roles-set';
import SnomedGroupConceptList from './SnomedGroupConceptList';

const snomedGroupConcept = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? SnomedGroupConceptList : undefined,
    options: {
        submenu: 'terminology'
    }
});
export default snomedGroupConcept;
