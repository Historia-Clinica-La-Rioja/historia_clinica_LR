import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import SettingsInputAntennaIcon from '@material-ui/icons/SettingsInputAntenna';
import RestClientMeasuresList from './RestClientMeasuresList';
import RestClientMeasureShow from "./RestClientMeasureShow";

import { ROOT, ADMINISTRADOR } from '../roles';

const restClientMeasures = (permissions: SGXPermissions) => ({
    icon: SettingsInputAntennaIcon,
    show: RestClientMeasureShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? RestClientMeasuresList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default restClientMeasures;
