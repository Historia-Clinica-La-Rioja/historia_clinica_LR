import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import { DEFAULT_BO_ROLES } from '../roles-set';

import CareLineInstitutionList from './CareLineInstitutionList';
import CareLineInstitutionCreate from "./CareLineInstitutionCreate";
import CareLineInstitutionShow from "./CareLineInstitutionShow";
import CareLineInstitutionEdit from "./CareLineInstitutionEdit";

const careLineInstitution = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? CareLineInstitutionList : undefined,
    show: CareLineInstitutionShow,
    create: CareLineInstitutionCreate,
    edit: CareLineInstitutionEdit,
    options: {
        submenu: 'facilities'
    }
});

export default careLineInstitution;