import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import icon from '@material-ui/icons/LocationCity';

import CityShow from './CityShow';
import CityList from './CityList';

import { ROOT, ADMINISTRADOR } from '../roles';

const cities = (permissions: SGXPermissions) => ({
    icon,
    show: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? CityShow : undefined,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? CityList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default cities;
