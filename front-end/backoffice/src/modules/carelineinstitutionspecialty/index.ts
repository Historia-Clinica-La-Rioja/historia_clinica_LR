import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import CareLineInstitutionSpecialtyCreate from "./CareLineInstitutionSpecialtyCreate";

const careLineInstitutionSpecialty = (permissions: SGXPermissions) => ({
    create: CareLineInstitutionSpecialtyCreate,
});

export default careLineInstitutionSpecialty;