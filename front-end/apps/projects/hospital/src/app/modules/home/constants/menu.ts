import { AppFeature, ERole } from '@api-rest/api-model';
import { MenuItemDef } from '@core/core-model';

const MANAGER_ROLES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_REGIONAL, ERole.GESTOR_DE_ACCESO_LOCAL];

export const ROLES_USER_SIDEBAR_MENU: MenuItemDef[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		id: 'home',
		url: '/home',
		options: {exact: true},
	},
	{
		text: 'app.menu.CONFIGURACION',
		icon: 'settings',
		id: 'home_settings',
		url: '/home/settings',
		permissions: [
			ERole.ROOT,
		],
		featureFlag: AppFeature.HABILITAR_CONFIGURACION
	},
	{
		text: 'app.menu.AUDIT',
		icon: 'groups',
		id: 'auditoria',
		url: '/home/auditoria',
		permissions: [
			ERole.AUDITOR_MPI
		],
		featureFlag: AppFeature.HABILITAR_MODULO_AUDITORIA
	},
	{
		text: 'app.menu.ACCESS_MANAGEMENT',
		icon: 'swap_horizontal_circle',
		id: 'access-management',
		url: '/home/gestion-de-accesos',
		permissions: MANAGER_ROLES,
		featureFlag: AppFeature.HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO
	},
];

export const NO_ROLES_USER_SIDEBAR_MENU: MenuItemDef[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		id: 'home',
		url: '/home',
		options: {exact: true},
	},
];
