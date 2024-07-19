import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_RDI_ROLES,
} from '../roles';

const resultStudies = (permissions: SGXPermissions) => ({
    list:  undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default resultStudies;
