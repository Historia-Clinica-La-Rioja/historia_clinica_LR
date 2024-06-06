import ContactMailIcon from '@material-ui/icons/ContactMail';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

import DoctorsOfficeShow from './DoctorsOfficeShow';
import DoctorsOfficeList from './DoctorsOfficeList';
import DoctorsOfficeCreate from './DoctorsOfficeCreate';
import DoctorsOfficeEdit from './DoctorsOfficeEdit';

const doctorsoffices = (permissions: SGXPermissions) => ({
    icon: ContactMailIcon,
    show: DoctorsOfficeShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? DoctorsOfficeList : undefined,
    create: DoctorsOfficeCreate,
    edit: DoctorsOfficeEdit,
    options: {
        submenu: 'facilities'
    }
});

export default doctorsoffices;
