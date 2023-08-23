import { AppFeature, ERole } from '@api-rest/api-model';
import { MenuItemDef } from '@core/core-model';

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
