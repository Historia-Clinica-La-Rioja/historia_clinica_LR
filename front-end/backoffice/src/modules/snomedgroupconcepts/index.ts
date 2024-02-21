
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import SnomedGroupConceptList from './SnomedGroupConceptList';
import { ROOT, ADMINISTRADOR } from '../roles';

const snomedGroupConcept = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? SnomedGroupConceptList : undefined,
    options: {
        submenu: 'terminology'
    }
});
export default snomedGroupConcept;
