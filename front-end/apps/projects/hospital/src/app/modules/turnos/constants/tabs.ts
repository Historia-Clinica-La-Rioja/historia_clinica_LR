import { AppFeature, ERole } from "@api-rest/api-model";
import { Tabs } from "@turnos/services/tabs.service";

export enum TabsLabel {
	PROFESSIONAL = 'OFERTA POR PROFESIONAL',
	INSTITUTION = 'OFERTA EN INSTITUCIÓN',
	CARE_NETWORK = 'OFERTA EN RED DE ATENCIÓN',
	IMAGE_NETWORK = 'DIAGNÓSTICO POR IMÁGENES',
	REQUESTS = 'SOLICITUDES'
}

const allowedRolesForProfessional = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD, ERole.ADMINISTRADOR_AGENDA, ERole.ENFERMERO];
const allowedRolesForInstitution = [ERole.ADMINISTRATIVO, ERole.GESTOR_DE_ACCESO_INSTITUCIONAL];
const allowedRolesForCareNetwork = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD, ERole.GESTOR_DE_ACCESO_INSTITUCIONAL];
const allowedRolesForReport = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ABORDAJE_VIOLENCIAS, ERole.GESTOR_DE_ACCESO_INSTITUCIONAL];
const allowedRolesForImageNetwork = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES, ERole.ADMINISTRADOR_AGENDA];

export const ALL_TABS: Tabs[] = [
	{ rules: { roles: allowedRolesForProfessional }, label: TabsLabel.PROFESSIONAL },
	{ rules: { roles: allowedRolesForInstitution }, label: TabsLabel.INSTITUTION },
	{ rules: { roles: allowedRolesForCareNetwork , featureFlag: AppFeature.HABILITAR_SOLICITUD_REFERENCIA}, label: TabsLabel.CARE_NETWORK },
	{ rules: { roles: allowedRolesForReport , featureFlag: AppFeature.HABILITAR_SOLICITUD_REFERENCIA}, label: TabsLabel.REQUESTS },
	{ rules: { roles: allowedRolesForImageNetwork, featureFlag: AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES }, label: TabsLabel.IMAGE_NETWORK },
];

export const FF_TABS = [{ featureFlag: [AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES, AppFeature.HABILITAR_SOLICITUD_REFERENCIA] }];