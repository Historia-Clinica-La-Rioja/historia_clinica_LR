import DescriptionIcon from '@material-ui/icons/Description';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import FileShow from './FileShow';
import FileList from './FileList';
import FileEdit from './FileEdit';

const files = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: FileShow,
    edit: FileEdit,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? FileList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default files;
