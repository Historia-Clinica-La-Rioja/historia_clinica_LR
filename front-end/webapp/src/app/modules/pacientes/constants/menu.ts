import { MenuItem } from '@core/core-model';

export const SIDEBAR_MENU: MenuItem[] = [
	{
		text: 'app.menu.PACIENTES',
		icon: 'person',
		url: '/pacientes',
	},
	{
		text: 'app.menu.INTERNACION',
		icon: 'assignment',
		url: '/internaciones',
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_box',
		url: '/auth/profile',
	},
];
