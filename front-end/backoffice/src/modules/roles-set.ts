import { RoleAssignment } from '../libs/sgx/api/model';
import {
    ADMINISTRADOR,
    ADMINISTRADOR_DE_ACCESO_DOMINIO,
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
    ROOT,
} from './roles';

// Roles en el WebSecurityConfiguration para /backoffice/**
const DEFAULT_BO_ROLES = [
    ROOT,
    ADMINISTRADOR,
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
    ADMINISTRADOR_DE_ACCESO_DOMINIO,
] as RoleAssignment[];

const BASIC_BO_ROLES = [
    ROOT,
    ADMINISTRADOR,
] as RoleAssignment[];


export {
    DEFAULT_BO_ROLES,
    BASIC_BO_ROLES,
};
