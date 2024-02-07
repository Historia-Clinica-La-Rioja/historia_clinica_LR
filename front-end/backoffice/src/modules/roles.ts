import { RoleAssignment } from "../libs/sgx/api/model";

const ROOT = { role: 'ROOT', institutionId: -1 };
const ADMINISTRADOR = { role: 'ADMINISTRADOR', institutionId: -1 };
const ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE = { role: 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE' } as RoleAssignment;
const ADMINISTRADOR_DE_ACCESO_DOMINIO = { role: 'ADMINISTRADOR_DE_ACCESO_DOMINIO', institutionId: -1 };
const AUDITORIA_DE_ACCESO = { role: 'AUDITORIA_DE_ACCESO', institutionId: -1 };

export {
    ROOT,
    ADMINISTRADOR,
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
    ADMINISTRADOR_DE_ACCESO_DOMINIO,
    AUDITORIA_DE_ACCESO,
};
