import ContactMailIcon from '@material-ui/icons/ContactMail';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import { BASIC_BO_ROLES } from '../../roles-set';

import OrchestratorShow from './OrchestratorShow';
import OrchestratorList from './OrchestratorList';
import OrchestratorCreate from './OrchestratorCreate';
import OrchestratorEdit from './OrchestratorEdit';

const orchestrator = (permissions: SGXPermissions) => ({
    icon: ContactMailIcon,
    show: OrchestratorShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? OrchestratorList : undefined,
    create: OrchestratorCreate,
    edit: OrchestratorEdit,
    options: {
        submenu: 'imageNetwork'
    }
});

export default orchestrator;
