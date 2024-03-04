import DescriptionIcon from '@material-ui/icons/Description';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import DocumentFileShow from './DocumentFileShow';
import DocumentFileList from './DocumentFileList';

const documentFiles = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: DocumentFileShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? DocumentFileList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default documentFiles;
