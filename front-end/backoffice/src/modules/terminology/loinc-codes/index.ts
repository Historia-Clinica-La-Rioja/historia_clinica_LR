import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    DEFAULT_BO_ROLES,
    BASIC_BO_ROLES,
} from '../../roles-set';
import LoincCodeShow from './LoincCodeShow';
import LoincCodeList from './LoincCodeList';
import LoincCodeEdit from './LoincCodeEdit';

const loincCodes = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? LoincCodeList : undefined,
    show: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? LoincCodeShow : undefined,
    create: undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ?  LoincCodeEdit : undefined,
    options: {
        submenu: 'terminology'
    }
});
export default loincCodes;
