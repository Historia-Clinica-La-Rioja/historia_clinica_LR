
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import ImageLvlPacCreate from './ImageLvlPacCreate';
import ImageLvlPacList from './ImageLvlPacList';
import ImageLvlPacShow from './ImageLvlPacShow';
import ImageLvlPacEdit from './ImageLvlPacEdit';

const imagelvlpacservers = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? ImageLvlPacList: undefined,
    create: ImageLvlPacCreate,
    show: ImageLvlPacShow,
    edit: ImageLvlPacEdit,
    options: {
        submenu: 'imageNetwork'
    }
});

export default imagelvlpacservers;