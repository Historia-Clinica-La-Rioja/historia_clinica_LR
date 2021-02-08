import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { RoleGuard } from '@core/guards/RoleGuard';

import { HomeComponent } from './home.component';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SettingsComponent } from './routes/settings/settings.component';

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		children: [
			{ path: '', pathMatch: 'full', component: InstitucionesComponent },
			{ path: 'profile', component: ProfileComponent },
			{ 
				path: 'settings', 
				component: SettingsComponent,
				canActivate: [RoleGuard, FeatureFlagGuard],
				data: { allowedRoles: [ERole.ROOT],
						featureFlag: AppFeature.HABILITAR_CONFIGURACION },
			}
		]
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
