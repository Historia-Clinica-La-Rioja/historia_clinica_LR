import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import LocationCityIcon from '@material-ui/icons/LocationCity';

import InstitutionShow from './InstitutionShow';
import InstitutionList from './InstitutionList';
import InstitutionCreate from './InstitutionCreate';
import InstitutionEdit from './InstitutionEdit';

import { ROOT, ADMINISTRADOR } from '../roles';

const institutions = (permissions: SGXPermissions) => ({
    icon: LocationCityIcon,
    show: InstitutionShow,
    list: InstitutionList,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? InstitutionCreate : undefined,
    edit: InstitutionEdit,
    options: {
        submenu: 'facilities'
    }
});

export default institutions;
