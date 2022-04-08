import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DocumentTypeShow from './DocumentTypeShow';
import DocumentTypeList from './DocumentTypeList';

import { ROOT, ADMINISTRADOR } from '../roles';

const documentTypes = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? DocumentTypeShow : undefined,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? DocumentTypeList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default documentTypes;

