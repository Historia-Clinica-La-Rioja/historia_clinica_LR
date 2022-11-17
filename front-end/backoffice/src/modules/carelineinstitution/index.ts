import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import CareLineInstitutionList from './CareLineInstitutionList';
import CareLineInstitutionCreate from "./CareLineInstitutionCreate";
import CareLineInstitutionShow from "./CareLineInstitutionShow";
import CareLineInstitutionEdit from "./CareLineInstitutionEdit";

const careLineInstitution = (permissions: SGXPermissions) => ({
    list: CareLineInstitutionList,
    show: CareLineInstitutionShow,
    create: CareLineInstitutionCreate,
    edit: CareLineInstitutionEdit,
    options: {
        submenu: 'facilities'
    }
});

export default careLineInstitution;