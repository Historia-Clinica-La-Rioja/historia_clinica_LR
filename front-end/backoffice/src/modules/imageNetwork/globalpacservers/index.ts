import RouterIcon from '@material-ui/icons/Router';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_RDI_ROLES,
} from '../roles';
import GlobalPacsList from './GlobalPacsList';
import GlobalPacsCreate from './GlobalPacsCreate';
import GlobalPacsShow from './GlobalPacsShow';
import GlobalPacsEdit from './GlobalPacsEdit';

const globalpacs = (permissions: SGXPermissions) => ({
    icon: RouterIcon,
    list: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? GlobalPacsList : undefined,
    show: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? GlobalPacsShow : undefined,
    create: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? GlobalPacsCreate : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? GlobalPacsEdit : undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default globalpacs;