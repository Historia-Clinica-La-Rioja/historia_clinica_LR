import { MenuItem } from '@core/core-model';

export const SIDEBAR_MENU: MenuItem[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		url: '/home',
		options: {exact: true},
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_circle',
		url: '/home/profile',
	},

];
