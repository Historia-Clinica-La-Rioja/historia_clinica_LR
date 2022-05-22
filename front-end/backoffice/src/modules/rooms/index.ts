import MeetingRoomIcon from '@material-ui/icons/MeetingRoom';

import RoomShow from './RoomShow';
import RoomList from './RoomList';
import RoomCreate from './RoomCreate';
import RoomEdit from './RoomEdit';

const rooms = {
    icon: MeetingRoomIcon,
    show: RoomShow,
    list: RoomList,
    create: RoomCreate,
    edit: RoomEdit,
    options: {
        submenu: 'facilities'
    }
};

export default rooms;
