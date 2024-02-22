import LocationCityIcon from '@material-ui/icons/LocationCity';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import {
    DEFAULT_BO_ROLES,
    BASIC_BO_ROLES,
} from '../roles-set';

import InstitutionShow from './InstitutionShow';
import InstitutionList from './InstitutionList';
import InstitutionCreate from './InstitutionCreate';
import InstitutionEdit from './InstitutionEdit';

const institutions = (permissions: SGXPermissions) => ({
    icon: LocationCityIcon,
    show: InstitutionShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? InstitutionList : undefined,
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionCreate : undefined,
    edit: InstitutionEdit,
    options: {
        submenu: 'facilities'
    }
});

export default institutions;
