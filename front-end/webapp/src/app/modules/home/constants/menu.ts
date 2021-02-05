import { AppFeature, ERole } from '@api-rest/api-model';
import { MenuItem } from '@core/core-model';

export const SIDEBAR_MENU: MenuItem[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		id: 'home',
		url: '/home',
		options: {exact: true},
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_circle',
		id: 'home_profile',
		url: '/home/profile',
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

];
