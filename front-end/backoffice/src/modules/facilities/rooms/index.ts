import MeetingRoomIcon from '@material-ui/icons/MeetingRoom';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMIN_ROLES,
} from '../../roles-set';

import RoomShow from './RoomShow';
import RoomList from './RoomList';
import RoomCreate from './RoomCreate';
import RoomEdit from './RoomEdit';

const rooms = (permissions: SGXPermissions) => ({
    icon: MeetingRoomIcon,
    show: RoomShow,
    list: permissions.hasAnyAssignment(...ADMIN_ROLES) ? RoomList : undefined,
    create: RoomCreate,
    edit: RoomEdit,
    options: {
        submenu: 'facilities'
    }
});

export default rooms;
