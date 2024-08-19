import ContactMailIcon from '@material-ui/icons/ContactMail';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

import {
    BASIC_RDI_ROLES,
} from '../roles';

import EquipmentShow from './EquipmentShow';
import EquipmentList from './EquipmentList';
import EquipmentCreate from './EquipmentCreate';
import EquipmentEdit from './EquipmentEdit';

const equipment = (permissions: SGXPermissions) => ({
    icon: ContactMailIcon,
    show: EquipmentShow,
    list: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? EquipmentList : undefined,
    create: EquipmentCreate,
    edit: EquipmentEdit,
    options: {
        submenu: 'imageNetwork'
    }
});

export default equipment;
