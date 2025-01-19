import SettingsIcon from '@material-ui/icons/Settings';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import PropertyShow from './PropertyShow';
import PropertyList from './PropertyList';

const check = (permissions: SGXPermissions) => 
    permissions.hasAnyAssignment(...BASIC_BO_ROLES) &&
    permissions.isOn('HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA');

const properties = (permissions: SGXPermissions) => ({
    icon: SettingsIcon,
    show: PropertyShow,
    list: check(permissions) ? PropertyList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default properties;
