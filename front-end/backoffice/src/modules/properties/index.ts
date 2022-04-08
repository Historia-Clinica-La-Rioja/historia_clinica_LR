import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import SettingsIcon from '@material-ui/icons/Settings';
import PropertyShow from './PropertyShow';
import PropertyList from './PropertyList';

import { ROOT, ADMINISTRADOR } from '../roles';

const check = (permissions: SGXPermissions) => 
    permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) &&
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
