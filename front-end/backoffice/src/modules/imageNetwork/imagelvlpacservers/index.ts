
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_RDI_ROLES,
} from '../roles';
import ImageLvlPacCreate from './ImageLvlPacCreate';
import ImageLvlPacList from './ImageLvlPacList';
import ImageLvlPacShow from './ImageLvlPacShow';
import ImageLvlPacEdit from './ImageLvlPacEdit';

const imagelvlpacservers = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? ImageLvlPacList: undefined,
    create: ImageLvlPacCreate,
    show: ImageLvlPacShow,
    edit: ImageLvlPacEdit,
    options: {
        submenu: 'imageNetwork'
    }
});

export default imagelvlpacservers;