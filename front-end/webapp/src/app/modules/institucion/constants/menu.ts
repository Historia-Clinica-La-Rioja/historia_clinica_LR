import { MenuItem } from '@core/core-model';

export const SIDEBAR_MENU: MenuItem[] = [
	{
		text: 'app.menu.INSTITUCIONES',
		icon: 'home',
		url: '/home',
		options: {exact: true},
	},
	{
		text: 'app.menu.PACIENTES',
		icon: 'person',
		url: './pacientes',
		permissions: [
			'ADMINISTRATIVO',
		],
	},
	{
		text: 'app.menu.INTERNACION',
		icon: 'assignment',
		url: './internaciones',
		permissions: [
			'ESPECIALISTA_MEDICO',
			'PROFESIONAL_DE_SALUD',
			'ENFERMERO_ADULTO_MAYOR',
			'ENFERMERO',
		],
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_circle',
		url: '/home/profile',
	},

];
