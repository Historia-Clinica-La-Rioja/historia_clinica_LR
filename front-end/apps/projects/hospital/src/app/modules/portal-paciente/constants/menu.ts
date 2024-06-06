import { AppFeature } from '@api-rest/api-model';
import { MenuItemDef } from '@core/core-model';
import { AppRoutes } from '../../../app-routing.module';

export const ROLES_USER_SIDEBAR_MENU: MenuItemDef[] = [
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
		options: {exact: true},
	},
	{
		text: 'app.menu.MIS_DATOS_PERSONALES',
		icon: 'person_outline',
		id: 'my_personal_data',
		url: '/paciente/perfil',
	},
];

export const NO_ROLES_USER_SIDEBAR_MENU: MenuItemDef[] = [
	{
		text: 'app.menu.MI_HISTORIA_CLINICA',
		icon: 'switch_account',
		id: 'my_hc',
		url: `/${AppRoutes.PortalPaciente}`,
		options: {exact: true},
		featureFlag: [AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE],
	},
	{
		text: 'app.menu.MIS_DATOS_PERSONALES',
		icon: 'person_outline',
		id: 'my_personal_data',
		url: `/${AppRoutes.PortalPaciente}/perfil`,
		featureFlag: [AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE],
	},
];
