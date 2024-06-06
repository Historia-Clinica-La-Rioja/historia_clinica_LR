import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import DocumentTypeShow from './DocumentTypeShow';
import DocumentTypeList from './DocumentTypeList';

const documentTypes = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? DocumentTypeShow : undefined,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? DocumentTypeList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default documentTypes;

