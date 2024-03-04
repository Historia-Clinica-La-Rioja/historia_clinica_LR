import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ROOT,
    ADMINISTRADOR,
} from '../../roles';
import MoveStudiesShow from './MoveStudiesShow';
import MoveStudiesList from './MoveStudiesList';
import MoveStudiesEdit from './MoveStudiesEdit';

const moveStudies = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(ROOT,ADMINISTRADOR) ?MoveStudiesShow: undefined,
    list: permissions.hasAnyAssignment(ROOT,ADMINISTRADOR) ? MoveStudiesList: undefined,
    edit: permissions.hasAnyAssignment(ROOT,ADMINISTRADOR) ?MoveStudiesEdit: undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default moveStudies;
