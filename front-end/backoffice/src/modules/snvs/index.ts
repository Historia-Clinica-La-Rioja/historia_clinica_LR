import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import { DEFAULT_BO_ROLES } from '../roles-set';

import SnvsShow from './SnvsShow';
import SnvsList from './SnvsList';

const snvs = (permissions: SGXPermissions) => ({
    show: SnvsShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? SnvsList : undefined,
    options: {
         submenu: 'more'
    }
});

export default snvs;
