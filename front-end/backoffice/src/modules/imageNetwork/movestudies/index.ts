import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_RDI_ROLES,
} from '../roles';
import MoveStudiesShow from './MoveStudiesShow';
import MoveStudiesList from './MoveStudiesList';
import MoveStudiesEdit from './MoveStudiesEdit';

const moveStudies = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ?MoveStudiesShow: undefined,
    list: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? MoveStudiesList: undefined,
    edit: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ?MoveStudiesEdit: undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default moveStudies;
