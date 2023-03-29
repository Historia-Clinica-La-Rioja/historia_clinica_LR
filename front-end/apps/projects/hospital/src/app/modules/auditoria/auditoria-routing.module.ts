import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { RoleGuard } from '@core/guards/RoleGuard';
import { HomeComponent } from './routes/home/home.component';
import { PatientsFusionComponent } from './routes/patients-fusion/patients-fusion.component';

const routes: Routes = [{
	path: '',
	children: [
		{
			path: '',
			component: HomeComponent
		},
		{
			path:"fusion-pacientes",
			component: PatientsFusionComponent
		}
	],
	canActivate: [RoleGuard,FeatureFlagGuard],
		data: { allowedRoles: [ERole.AUDITOR_MPI] , featureFlag: AppFeature.HABILITAR_MODULO_AUDITORIA }
}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AuditoriaRoutingModule { }
