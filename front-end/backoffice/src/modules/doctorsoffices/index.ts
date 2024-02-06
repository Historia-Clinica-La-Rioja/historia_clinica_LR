import ContactMailIcon from '@material-ui/icons/ContactMail';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import { DEFAULT_BO_ROLES } from '../roles-set';

import DoctorsOfficeShow from './DoctorsOfficeShow';
import DoctorsOfficeList from './DoctorsOfficeList';
import DoctorsOfficeCreate from './DoctorsOfficeCreate';
import DoctorsOfficeEdit from './DoctorsOfficeEdit';

const doctorsoffices = (permissions: SGXPermissions) => ({
    icon: ContactMailIcon,
    show: DoctorsOfficeShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? DoctorsOfficeList : undefined,
    create: DoctorsOfficeCreate,
    edit: DoctorsOfficeEdit,
    options: {
        submenu: 'facilities'
    }
});

export default doctorsoffices;
