
import { RoleAssignment } from '../../libs/sgx/api/model';

import {
    BASIC_BO_ROLES,
} from '../roles-set';

const API_IMAGENES = { role: 'API_IMAGENES', institutionId: -1 };

const BASIC_RDI_ROLES = [
    API_IMAGENES,
    ...BASIC_BO_ROLES,
] as RoleAssignment[];

export {
    BASIC_RDI_ROLES,
};
