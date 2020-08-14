import { MenuItem } from '@core/core-model';
import { AppFeature, ERole } from '@api-rest/api-model';

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
			ERole.ADMINISTRATIVO,
		],
	},
	{
		text: 'app.menu.AMBULATORIA',
		icon: 'person_search',
		url: './ambulatoria',
		permissions: [
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO,
		],
		featureFlag: AppFeature.HABILITAR_HISTORIA_CLINICA_AMBULATORIA
	},
	{
		text: 'app.menu.INTERNACION',
		icon: 'assignment',
		url: './internaciones',
		permissions: [
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO_ADULTO_MAYOR,
			ERole.ENFERMERO,
		],
	},
	{
		text: 'app.menu.TURNOS',
		icon: 'calendar_today',
		url: './turnos',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ADMINISTRADOR_AGENDA
		],
		featureFlag: AppFeature.HABILITAR_GESTION_DE_TURNOS
	},
	{
		text: 'app.menu.CAMAS',
		icon: 'single_bed',
		url: './camas',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ENFERMERO,
		],
	},
	{
		text: 'app.menu.PERFIL',
		icon: 'account_circle',
		url: '/home/profile',
	},

];
