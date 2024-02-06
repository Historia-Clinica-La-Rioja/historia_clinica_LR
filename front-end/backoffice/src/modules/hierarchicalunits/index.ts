import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import { DEFAULT_BO_ROLES } from '../roles-set';

import HierarchicalUnitShow from './HierarchicalUnitShow';
import HierarchicalUnitList from './HierarchicalUnitList';
import HierarchicalUnitCreate from './HierarchicalUnitCreate';
import HierarchicalUnitEdit from './HierarchicalUnitEdit';

const hierarchicalunits = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? HierarchicalUnitList : undefined,
    show: HierarchicalUnitShow,
    create: HierarchicalUnitCreate,
    edit: HierarchicalUnitEdit,
    options: {
        submenu: 'facilities'
    }
});

export default hierarchicalunits;
