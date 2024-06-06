import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

import HierarchicalUnitShow from './HierarchicalUnitShow';
import HierarchicalUnitList from './HierarchicalUnitList';
import HierarchicalUnitCreate from './HierarchicalUnitCreate';
import HierarchicalUnitEdit from './HierarchicalUnitEdit';

const hierarchicalunits = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? HierarchicalUnitList : undefined,
    show: HierarchicalUnitShow,
    create: HierarchicalUnitCreate,
    edit: HierarchicalUnitEdit,
    options: {
        submenu: 'facilities'
    }
});

export default hierarchicalunits;
