import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES,
} from '../../roles-set';

import SectorShow from './SectorShow';
import SectorList from './SectorList';
import SectorCreate from './SectorCreate';
import SectorEdit from './SectorEdit';


const sectors = (permissions: SGXPermissions) => ({
    show: SectorShow,
    list: permissions.hasAnyAssignment(...ADMIN_ROLES) ? SectorList : undefined,
    create: SectorCreate,
    edit: SectorEdit,
    options: {
        submenu: 'facilities'
    }
});

export default sectors;
