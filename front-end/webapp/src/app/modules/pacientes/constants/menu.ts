import { MenuItem } from '@core/core-model';

export const SIDEBAR_MENU: MenuItem[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		url: '/home',
		permissions: [],
		options: {exact: true},
	},
	{
		text: 'app.menu.PACIENTES',
		icon: 'person',
		url: '/pacientes',
		permissions: [],
	},
	{
		text: 'app.menu.INTERNACION',
		icon: 'assignment',
		url: '/internaciones',
		permissions: [],
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_circle',
		url: '/home/profile',
		permissions: [],
	},

];
