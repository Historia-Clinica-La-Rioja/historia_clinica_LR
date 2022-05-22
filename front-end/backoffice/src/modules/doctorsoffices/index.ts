import ContactMailIcon from '@material-ui/icons/ContactMail';

import DoctorsOfficeShow from './DoctorsOfficeShow';
import DoctorsOfficeList from './DoctorsOfficeList';
import DoctorsOfficeCreate from './DoctorsOfficeCreate';
import DoctorsOfficeEdit from './DoctorsOfficeEdit';

const doctorsoffices = {
    icon: ContactMailIcon,
    show: DoctorsOfficeShow,
    list: DoctorsOfficeList,
    create: DoctorsOfficeCreate,
    edit: DoctorsOfficeEdit,
    options: {
        submenu: 'facilities'
    }
};

export default doctorsoffices;
