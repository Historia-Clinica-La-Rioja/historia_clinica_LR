import ContactMailIcon from '@material-ui/icons/ContactMail';

import EquipmentShow from './EquipmentShow';
import EquipmentList from './EquipmentList';
import EquipmentCreate from './EquipmentCreate';
import EquipmentEdit from './EquipmentEdit';

const equipment = {
    icon: ContactMailIcon,
    show: EquipmentShow,
    list: EquipmentList,
    create: EquipmentCreate,
    edit: EquipmentEdit,
    options: {
        submenu: 'facilities'
    }
};

export default equipment;
