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
			ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES,
			ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR,
			ERole.PRESCRIPTOR
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
			ERole.PERSONAL_DE_FARMACIA,
			ERole.PERSONAL_DE_LEGALES,
			ERole.ABORDAJE_VIOLENCIAS
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
			ERole.ESPECIALISTA_EN_ODONTOLOGIA,
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES,
			ERole.ABORDAJE_VIOLENCIAS
		],
		featureFlag: AppFeature.HABILITAR_GESTION_DE_TURNOS
	},
	{
		text: 'app.menu.CAMAS',
		icon: 'hotel',
		id: 'camas',
		url: './camas',
		permissions: [
			ERole.ADMINISTRATIVO,
			ERole.ENFERMERO,
			ERole.ADMINISTRADOR_DE_CAMAS,
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES
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
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA,
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
			ERole.PERSONAL_DE_ESTADISTICA,
			ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR,
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA
		],
		featureFlag: AppFeature.HABILITAR_REPORTES
	},
	{
		text: 'app.menu.RUTAS_PROVINCIALES',
		icon: 'swap_calls',
		id: 'rutas-larioja',
		url: './rutas-larioja',
		permissions: [
			ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
			ERole.ENFERMERO,
			ERole.ADMINISTRADOR,
			ERole.ESPECIALISTA_MEDICO,
			ERole.ADMINISTRATIVO,
			ERole.ADMINISTRADOR_AGENDA,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO_ADULTO_MAYOR,
			ERole.PERSONAL_DE_IMAGENES,
			ERole.PERSONAL_DE_LABORATORIO,
			ERole.PERSONAL_DE_FARMACIA,
			ERole.PERFIL_EPIDEMIO_INSTITUCION,
			ERole.PERFIL_EPIDEMIO_MESO,
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES,
			ERole.PRESCRIPTOR,
			ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR,
			ERole.TECNICO,
			ERole.VIRTUAL_CONSULTATION_PROFESSIONAL,
			ERole.VIRTUAL_CONSULTATION_RESPONSIBLE,
			ERole.ABORDAJE_VIOLENCIAS,
		],
	},
	{
		text: 'app.menu.REPORTES_PROVINCIALES',
		icon: 'description',
		id: 'reportes-larioja',
		url: './reportes-larioja',
		permissions: [
			ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
			ERole.PERSONAL_DE_ESTADISTICA,
		],
	},
	{
		text: 'app.menu.LISTADO_DE_TRABAJO',
		icon: 'assignment_ind',
		id: 'listadoTrabajo',
		url: './imagenes/lista-trabajos',
		permissions: [ERole.TECNICO, ERole.INFORMADOR],
		featureFlag: AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES
	},
	{
		text: 'app.menu.TELEMEDICINE',
		icon: 'calendar_today',
		id: 'telemedicina',
		url: './telesalud',
		permissions: [ERole.VIRTUAL_CONSULTATION_PROFESSIONAL, ERole.VIRTUAL_CONSULTATION_RESPONSIBLE],
		featureFlag: AppFeature.HABILITAR_TELEMEDICINA
	},
	{
		text: 'app.menu.DIGITAL_SIGNATURE',
		icon: 'border_color',
		id: 'digitalSignature',
		url: './firma-digital/documentos',
		permissions: [
			ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO_ADULTO_MAYOR,
			ERole.ENFERMERO,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA
		],
		featureFlag: AppFeature.HABILITAR_FIRMA_DIGITAL
	},
];
