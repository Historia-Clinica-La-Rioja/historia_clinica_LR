import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './routes/home/home.component';
import {AppFeature, ERole} from '@api-rest/api-model';
import {RoleGuard} from '@core/guards/RoleGuard';
import {FeatureFlagGuard} from '@core/guards/FeatureFlagGuard';


const routes: Routes = [
	{
		path: '',
		children: [
			{ path: '', component: HomeComponent },
			{ path: 'home', component: HomeComponent }
		],
		canActivate: [RoleGuard, FeatureFlagGuard],
		data: {
			allowedRoles: [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.PERSONAL_DE_ESTADISTICA, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR,
				ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA],
			featureFlag: AppFeature.HABILITAR_REPORTES
		}
	},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportesRoutingModule { }
