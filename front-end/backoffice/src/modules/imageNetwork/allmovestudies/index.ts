import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ROOT,
    ADMINISTRADOR,
} from '../../roles';
import AllMoveStudiesShow from './AllMoveStudiesShow';
import AllMoveStudiesList from './AllMoveStudiesList';
import AllMoveStudiesEdit from './AllMoveStudiesEdit';

const allMoveStudies = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(ROOT,ADMINISTRADOR) ?AllMoveStudiesShow: undefined,
    list: permissions.hasAnyAssignment(ROOT,ADMINISTRADOR) ? AllMoveStudiesList: undefined,
    edit: permissions.hasAnyAssignment(ROOT,ADMINISTRADOR) ?AllMoveStudiesEdit: undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default allMoveStudies;
