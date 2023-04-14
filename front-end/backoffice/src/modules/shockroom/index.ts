import ShockRoomCreate from './ShockRoomCreate';
import ShockRoomEdit from './ShockRoomEdit';
import ShockRoomList from './ShockRoomList';
import ShockRoomShow from './ShockRoomShow';

const shockroom = {
    create: ShockRoomCreate,
    show: ShockRoomShow,
    list: ShockRoomList,
    edit: ShockRoomEdit,
    options: {
        submenu: 'facilities'
    }
};

export default shockroom;