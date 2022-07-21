import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";

import ClinicalSpecialtyList from "../clinicalspecialties/ClinicalSpecialtyList";
import ClinicalSpecialtyShow from "../clinicalspecialties/ClinicalSpecialtyShow";

import { ADMINISTRADOR, ROOT } from "../roles";

const clinicalservices = (permissions: SGXPermissions) => ({
    show: ClinicalSpecialtyShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? ClinicalSpecialtyList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default clinicalservices;