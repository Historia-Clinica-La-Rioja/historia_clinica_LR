import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { RoleGuard } from '@core/guards/RoleGuard';
import { SystemExtensionComponent } from '@extensions/routes/extension/extension.component';

import { HomeComponent } from './home.component';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ManageKeysComponent } from './routes/manage-keys/manage-keys.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SETTINGS_ROUTES } from './routes/settings';
import { UpdatePasswordComponent } from "../auth/components/update-password/update-password.component";
import { UpdatePasswordSuccessComponent } from "../auth/components/update-password-success/update-password-success.component";
import { TemplateRenderComponent } from './routes/template-render/template-render.component';
import { RoutedExternalComponent } from '@extensions/components/routed-external/routed-external.component';
import { HomeRoutes, MANAGER_ROLES } from './constants/menu';
import { RouteMenuComponent } from '@presentation/components/route-menu/route-menu.component';
import { CubeReportComponent } from '../reportes/routes/cube-report/cube-report.component';


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
			{ path: `${HomeRoutes.Settings}/template/:templateId`, component: TemplateRenderComponent },
			{
				path: HomeRoutes.Settings,
				component: RouteMenuComponent,
				canActivate: [ FeatureFlagGuard, RoleGuard ],
				data: {
					featureFlag: AppFeature.HABILITAR_CONFIGURACION,
					allowedRoles: [ ERole.ROOT ],
					needsRoot: true,
					label: { key: 'app.menu.CONFIGURACION' },
					icon: 'settings',
				},
				children: SETTINGS_ROUTES,
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
					needsRoot: true
				},
			},
			{
				path: HomeRoutes.CallCenter,
				loadChildren: () => import('@call-center/call-center.module').then(m => m.CallCenterModule),
				canActivate: [RoleGuard],
				data: {
					allowedRoles: [ERole.GESTOR_CENTRO_LLAMADO],
					needsRoot: true,
				},
			},
            {   
                path: "get-call-center-appointments", component: CubeReportComponent,
				canActivate: [FeatureFlagGuard, RoleGuard],
				data: {
					featureFlag: AppFeature.HABILITAR_REPORTE_CENTRO_LLAMADO_EN_DESARROLLO,
					allowedRoles: [ERole.GESTOR_CENTRO_LLAMADO],
					needsRoot: true
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
