import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

import {
    BASIC_RDI_ROLES,
} from '../roles';

import AllMoveStudiesShow from './AllMoveStudiesShow';
import AllMoveStudiesList from './AllMoveStudiesList';
import AllMoveStudiesEdit from './AllMoveStudiesEdit';

const allMoveStudies = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ?AllMoveStudiesShow: undefined,
    list: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ? AllMoveStudiesList: undefined,
    edit: permissions.hasAnyAssignment(...BASIC_RDI_ROLES) ?AllMoveStudiesEdit: undefined,
    options: {
        submenu: 'imageNetwork'
    }
});

export default allMoveStudies;
