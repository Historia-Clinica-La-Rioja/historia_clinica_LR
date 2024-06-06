import SettingsInputAntennaIcon from '@material-ui/icons/SettingsInputAntenna';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import RestClientMeasuresList from './RestClientMeasuresList';
import RestClientMeasureShow from './RestClientMeasureShow';

const restClientMeasures = (permissions: SGXPermissions) => ({
    icon: SettingsInputAntennaIcon,
    show: RestClientMeasureShow,
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? RestClientMeasuresList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default restClientMeasures;
