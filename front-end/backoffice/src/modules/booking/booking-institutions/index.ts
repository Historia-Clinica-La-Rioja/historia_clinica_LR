import LocationCityIcon from '@material-ui/icons/LocationCity';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import BookingInstitutionShow from './BookingInstitutionShow';
import BookingInstitutionList from './BookingInstitutionList';
import BookingInstitutionCreate from './BookingInstitutionCreate';
import BookingInstitutionEdit from './BookingInstitutionEdit';

const BookingInstitutions = (permissions: SGXPermissions) => ({
    icon: LocationCityIcon,
    show: BookingInstitutionShow,
    list: BookingInstitutionList,
    create: BookingInstitutionCreate,
    edit: BookingInstitutionEdit,
    options: {
        submenu: 'booking'
    }
});

export default BookingInstitutions;
