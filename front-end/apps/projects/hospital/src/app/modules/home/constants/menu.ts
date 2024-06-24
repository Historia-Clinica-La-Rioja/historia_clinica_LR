import { AppFeature, ERole } from '@api-rest/api-model';
import { MenuItemDef } from '@core/core-model';

export const MANAGER_ROLES = [
	ERole.GESTOR_DE_ACCESO_DE_DOMINIO,
	ERole.GESTOR_DE_ACCESO_REGIONAL,
	ERole.GESTOR_DE_ACCESO_LOCAL,
];

export enum HomeRoutes {
	Home = '',						// pantalla inicial
	Profile = 'profile',			// Perfil del usuario
	Settings = 'settings',			// Configuración
	Extension = 'extension', 		// Extensión
	UserKeys = 'user-keys', 		// API Keys del usuario
	Auditoria = 'auditoria',
	AccessManagement = 'gestion-de-accesos', // Gestion de accesos
	CallCenter = 'centro-de-llamadas', // Centro de llamadas
}

export const PUBLIC_API_ROLES = [
	ERole.API_FACTURACION,
	ERole.API_TURNOS,
	ERole.API_PACIENTES,
	ERole.API_RECETAS,
	ERole.API_SIPPLUS,
	ERole.API_USERS,
	ERole.API_IMAGENES,
	ERole.API_ORQUESTADOR,
];

export const ROLES_USER_SIDEBAR_MENU: MenuItemDef[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		id: 'home',
		url: '/home',
		options: { exact: true },
	},
	{
		text: 'app.menu.CONFIGURACION',
		icon: 'settings',
		id: 'home_settings',
		url: '/home/settings',
		permissions: [
			ERole.ROOT, ERole.ADMINISTRADOR
		],
	},
	{
		text: 'app.menu.AUDIT',
		icon: 'groups',
		id: 'auditoria',
		url: '/home/auditoria',
		permissions: [
			ERole.AUDITOR_MPI
		],
		featureFlag: [AppFeature.HABILITAR_MODULO_AUDITORIA]
	},
	{
		text: 'app.menu.ACCESS_MANAGEMENT',
		icon: 'swap_horizontal_circle',
		id: 'access-management',
		url: '/home/gestion-de-accesos',
		permissions: MANAGER_ROLES,
		featureFlag: [AppFeature.HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO]
	},
	{
		text: 'app.menu.CALL_CENTER',
		icon: 'calendar_today',
		id: 'call-center',
		url: '/home/centro-de-llamadas',
		permissions: [
			ERole.GESTOR_CENTRO_LLAMADO
		],
	},
	{
		text: 'app.menu.APPOINTMENTS_GIVEN',
		icon: 'calendar_today',
		id: 'appointments-given',
		url: '/home/get-call-center-appointments',
		permissions: [
			ERole.GESTOR_CENTRO_LLAMADO
		],
		featureFlag: [AppFeature.HABILITAR_REPORTE_CENTRO_LLAMADO_EN_DESARROLLO]
	},
	{
		text: 'app.menu.API_KEYS',
		icon: 'private_connectivity',
		id: 'user-keys',
		url: '/home/profile/user-keys',
		permissions: PUBLIC_API_ROLES,
	},
];

export const NO_ROLES_USER_SIDEBAR_MENU: MenuItemDef[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		id: 'home',
		url: '/home',
		options: { exact: true },
	},
];
