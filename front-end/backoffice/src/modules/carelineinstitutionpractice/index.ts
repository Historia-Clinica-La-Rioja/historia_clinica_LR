import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import CareLineInstitutionPracticeCreate from "./CareLineInstitutionPracticeCreate";

const careLineInstitutionPractice = (permissions: SGXPermissions) => ({
    create: CareLineInstitutionPracticeCreate,
});

export default careLineInstitutionPractice;