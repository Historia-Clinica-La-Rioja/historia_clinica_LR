import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    DEFAULT_BO_ROLES,
} from '../../roles-set';

import SnomedGroupList from './SnomedGroupList';
import SnomedGroupShow from './SnomedGroupShow';
import SnomedGroupCreate from './SnomedGroupCreate';
import SnomedGroupEdit from './SnomedGroupEdit';

const snomedgroups = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? SnomedGroupList : undefined,
    show: SnomedGroupShow,
    create: SnomedGroupCreate,
    edit: SnomedGroupEdit,
    options: {
        submenu: 'terminology'
    }
});

export default snomedgroups;
