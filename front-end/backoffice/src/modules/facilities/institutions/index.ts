import LocationCityIcon from '@material-ui/icons/LocationCity';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

import InstitutionShow from './InstitutionShow';
import InstitutionList from './InstitutionList';
import InstitutionCreate from './InstitutionCreate';
import InstitutionEdit from './InstitutionEdit';

const institutions = (permissions: SGXPermissions) => ({
    icon: LocationCityIcon,
    show: InstitutionShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? InstitutionList : undefined,
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? InstitutionCreate : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? InstitutionEdit : undefined,
    options: {
        submenu: 'facilities'
    }
});

export default institutions;
