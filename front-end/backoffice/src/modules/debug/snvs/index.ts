import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import SnvsShow from './SnvsShow';
import SnvsList from './SnvsList';

const snvs = (permissions: SGXPermissions) => ({
    show: SnvsShow,
    list: permissions.isOn('HABILITAR_REPORTE_EPIDEMIOLOGICO') && permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? SnvsList : undefined,
    options: {
         submenu: 'debug'
    }
});

export default snvs;