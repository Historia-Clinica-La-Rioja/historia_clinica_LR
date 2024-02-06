import HotelIcon from '@material-ui/icons/Hotel';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import { DEFAULT_BO_ROLES } from '../roles-set';

import BedShow from './BedShow';
import BedList from './BedList';
import BedCreate from './BedCreate';
import BedEdit from './BedEdit';

const beds = (permissions: SGXPermissions) => ({
    icon: HotelIcon,
    show: BedShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? BedList : undefined,
    create: BedCreate,
    edit: BedEdit,
    options: {
        submenu: 'facilities'
    }
});

export default beds;
