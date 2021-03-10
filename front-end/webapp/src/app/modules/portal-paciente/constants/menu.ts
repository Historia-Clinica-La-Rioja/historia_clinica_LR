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
		text: 'app.menu.MI_HISTORIA_CLINICA',
		icon: 'switch_account',
		id: 'my_hc',
		url: '/paciente',
	},
	{
		text: 'app.menu.MIS_DATOS_PERSONALES',
		icon: 'person_outline',
		id: 'my_personal_data',
		url: '/paciente/perfil',
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_circle',
		id: 'home_profile',
		url: '/home/profile',
	},
];
