import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES,
} from '../../roles-set';

import ShockRoomCreate from './ShockRoomCreate';
import ShockRoomEdit from './ShockRoomEdit';
import ShockRoomList from './ShockRoomList';
import ShockRoomShow from './ShockRoomShow';

const shockroom = (permissions: SGXPermissions) => ({
    create: ShockRoomCreate,
    show: ShockRoomShow,
    list: permissions.hasAnyAssignment(...ADMIN_ROLES) ? ShockRoomList : undefined,
    edit: ShockRoomEdit,
    options: {
        submenu: 'facilities'
    }
});

export default shockroom;