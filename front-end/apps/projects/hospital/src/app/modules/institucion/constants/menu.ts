import { MenuItemDef } from '@core/core-model';
import { AppFeature, ERole } from '@api-rest/api-model';

export const SIDEBAR_MENU: MenuItemDef[] = [
	{
		text: 'app.menu.PACIENTES',
		icon: 'person',
		id: 'pacientes',
		url: './pacientes',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE
		],
	},
	{
		text: 'app.menu.AMBULATORIA',
		icon: 'person_search',
		id: 'ambulatoria',
		url: './ambulatoria',
		permissions: [
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA,
			ERole.PERSONAL_DE_LABORATORIO,
			ERole.PERSONAL_DE_IMAGENES,
			ERole.PERSONAL_DE_FARMACIA
		],
		featureFlag: AppFeature.HABILITAR_HISTORIA_CLINICA_AMBULATORIA
	},
	{
		text: 'app.menu.INTERNACION',
		icon: 'assignment',
		id: 'internaciones',
		url: './internaciones',
		permissions: [
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO_ADULTO_MAYOR,
			ERole.ENFERMERO,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA
		],
	},
	{
		text: 'app.menu.TURNOS',
		icon: 'calendar_today',
		id: 'turnos',
		url: './turnos',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ADMINISTRADOR_AGENDA,
			ERole.ENFERMERO,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA
		],
		featureFlag: AppFeature.HABILITAR_GESTION_DE_TURNOS
	},
	{
		text: 'app.menu.CAMAS',
		icon: 'single_bed',
		id: 'camas',
		url: './camas',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ENFERMERO,
			ERole.ADMINISTRADOR_DE_CAMAS
		],
	},
	{
		text: 'app.menu.GUARDIA',
		icon: 'local_hospital',
		id: 'guardia',
		url: './guardia',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ENFERMERO,
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
		],
		featureFlag: AppFeature.HABILITAR_MODULO_GUARDIA
	},
	{
		text: 'app.menu.REPORTES',
		icon: 'description',
		id: 'reportes',
		url: './reportes',
		permissions: [
			ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
			ERole.PERSONAL_DE_ESTADISTICA
		],
		featureFlag: AppFeature.HABILITAR_REPORTES
	},

];
