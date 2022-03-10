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

export enum HomeRoutes {
	Home = '',						// pantalla inicial
	Profile = 'profile',			// Perfil del usuario
	Settings = 'settings',			// Configuración
	Extension = 'extension', 		// Extensión

}

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		children: [
			{ path: '', pathMatch: 'full', component: InstitucionesComponent },
			{ path: HomeRoutes.Profile, component: ProfileComponent },
			{ path: `${HomeRoutes.Extension}/:menuItemId`, component: SystemExtensionComponent },
			{
				path: HomeRoutes.Settings,
				component: SettingsComponent,
				canActivate: [FeatureFlagGuard, RoleGuard],
				data: { featureFlag: AppFeature.HABILITAR_CONFIGURACION,
						allowedRoles: [ERole.ROOT],
						needsRoot: true},
			}
		]
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
