import icon from '@material-ui/icons/LocationCity';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import CityShow from './CityShow';
import CityList from './CityList';

const cities = (permissions: SGXPermissions) => ({
    icon,
    show: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? CityShow : undefined,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? CityList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default cities;
