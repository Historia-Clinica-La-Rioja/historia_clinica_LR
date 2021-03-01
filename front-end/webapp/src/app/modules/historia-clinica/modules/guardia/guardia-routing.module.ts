import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { AppFeature, ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { NewEpisodeAdminTriageComponent } from './routes/new-episode-admin-triage/new-episode-admin-triage.component';
import { AdmisionAdministrativaComponent } from './routes/admision-administrativa/admision-administrativa.component';
import {
	NewEpisodeAdultGynecologicalTriageComponent
} from './routes/new-episode-adult-gynecological-triage/new-episode-adult-gynecological-triage.component';
import { NewEpisodePediatricTriageComponent } from './routes/new-episode-pediatric-triage/new-episode-pediatric-triage.component';
import { EpisodeDetailsComponent } from './routes/episode-details/episode-details.component';
import { MedicalDischargeComponent } from './routes/medical-discharge/medical-discharge.component';


const routes: Routes = [{
	path: '',
	children: [
		{
			path: '',
			component: HomeComponent
		},
		{
			path: 'nuevo-episodio/administrativa',
			component: AdmisionAdministrativaComponent
		},
		{
			path: 'nuevo-episodio/triage-administrativo',
			component: NewEpisodeAdminTriageComponent
		},
		{
			path: 'nuevo-episodio/triage-medico',
			component: NewEpisodeAdultGynecologicalTriageComponent
		},
		{
			path: 'nuevo-episodio/triage-pediatrico',
			component: NewEpisodePediatricTriageComponent
		},
		{
			path: 'episodio/:id',
			component: EpisodeDetailsComponent
		},
		{
			path: 'episodio/:id/alta-medica',
			component: MedicalDischargeComponent
		},
	],
	canActivate: [RoleGuard, FeatureFlagGuard],
	data: {
		allowedRoles: [ERole.ADMINISTRADOR, ERole.ADMINISTRATIVO, ERole.ENFERMERO, ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD, ERole.ROOT],
		featureFlag: AppFeature.HABILITAR_MODULO_GUARDIA
	}
}];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class GuardiaRoutingModule {
}
