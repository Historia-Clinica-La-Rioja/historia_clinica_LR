import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DescriptionIcon from '@material-ui/icons/Description';
import DocumentFileShow from './DocumentFileShow';
import DocumentFileList from './DocumentFileList';

import { ROOT, ADMINISTRADOR } from '../roles';

const documentFiles = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: DocumentFileShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? DocumentFileList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default documentFiles;
