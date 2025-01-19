import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

import CareLineInstitutionList from './CareLineInstitutionList';
import CareLineInstitutionCreate from "./CareLineInstitutionCreate";
import CareLineInstitutionShow from "./CareLineInstitutionShow";
import CareLineInstitutionEdit from "./CareLineInstitutionEdit";

const careLineInstitution = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? CareLineInstitutionList : undefined,
    show: CareLineInstitutionShow,
    create: permissions?.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? CareLineInstitutionCreate : undefined,
    edit: permissions?.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ? CareLineInstitutionEdit : undefined,
    options: {
        submenu: 'facilities'
    }
});

export default careLineInstitution;