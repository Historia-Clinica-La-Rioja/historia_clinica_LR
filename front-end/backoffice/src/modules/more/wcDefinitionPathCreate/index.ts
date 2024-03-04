import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

import WcDefinitionPathCreate from './WcDefinitionPathCreate';
import WcDefinitionPathEdit from './WcDefinitionPathEdit';
import WcDefinitionPathList from './WcDefinitionPathList';
import WcDefinitionPathShow from './WcDefinitionPathShow';

const wcDefinitionPath = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? WcDefinitionPathList : undefined,
    create: WcDefinitionPathCreate,
    show: WcDefinitionPathShow,
    edit: WcDefinitionPathEdit,
    options: {
        submenu: 'more'
    },
});


export default wcDefinitionPath;
