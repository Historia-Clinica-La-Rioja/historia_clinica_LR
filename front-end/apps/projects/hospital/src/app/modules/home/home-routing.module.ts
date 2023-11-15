import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { RoleGuard } from '@core/guards/RoleGuard';
import { SystemExtensionComponent } from '@extensions/routes/extension/extension.component';

import { HomeComponent } from './home.component';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SettingsComponent } from './routes/settings/settings.component';
import { ManageKeysComponent } from './routes/manage-keys/manage-keys.component';
import { UpdatePasswordComponent } from "../auth/components/update-password/update-password.component";
import {
	UpdatePasswordSuccessComponent
} from "../auth/components/update-password-success/update-password-success.component";
import { RoutedExternalComponent } from '@extensions/components/routed-external/routed-external.component';

export const PUBLIC_API_ROLES = [
	ERole.API_FACTURACION,
	ERole.API_TURNOS,
	ERole.API_PACIENTES,
	ERole.API_RECETAS,
	ERole.API_SIPPLUS,
	ERole.API_USERS,
	ERole.API_IMAGENES,
	ERole.API_ORQUESTADOR,
];

export enum HomeRoutes {
	Home = '',						// pantalla inicial
	Profile = 'profile',			// Perfil del usuario
	Settings = 'settings',			// Configuración
	Extension = 'extension', 		// Extensión
	UserKeys = 'user-keys', 		// API Keys del usuario
	Auditoria = 'auditoria',
	AccessManagement = 'gestion-de-accesos', // Gestion de accesos
}

const MANAGER_ROLES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_REGIONAL, ERole.GESTOR_DE_ACCESO_LOCAL];

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		children: [
			{ path: '', pathMatch: 'full', component: InstitucionesComponent },
			{ path: HomeRoutes.Profile, component: ProfileComponent },
			{
				path: HomeRoutes.Profile + '/' + HomeRoutes.UserKeys,
				component: ManageKeysComponent,
			},
			{ path: `${HomeRoutes.Extension}/:menuItemId`, component: SystemExtensionComponent },
			{
				path: HomeRoutes.Settings,
				component: SettingsComponent,
				canActivate: [FeatureFlagGuard, RoleGuard],
				data: {
					featureFlag: AppFeature.HABILITAR_CONFIGURACION,
					allowedRoles: [ERole.ROOT],
					needsRoot: true
				},
			},
			{ path: 'update-password', component: UpdatePasswordComponent },
			{ path: 'update-password-success', component: UpdatePasswordSuccessComponent },
			{ path: 'web-components/:wcId', component: RoutedExternalComponent },
			{
				path: HomeRoutes.Auditoria,
				loadChildren: () => import('../../modules/auditoria/auditoria.module').then(m => m.AuditoriaModule),
			},
			{
				path: HomeRoutes.AccessManagement,
				loadChildren: () => import('@access-management/access-management.module').then(m => m.AccessManagementModule),
				canActivate: [FeatureFlagGuard, RoleGuard],
				data: {
					featureFlag: AppFeature.HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO,
					allowedRoles: MANAGER_ROLES,
				},
			},
		]
	}
];



@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class HomeRoutingModule { }
