import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { HistoriaClinicaModule } from '../../historia-clinica.module';
import { InstitucionModule } from '../../../institucion/institucion.module';
import { LazyMaterialModule } from '../../../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { GuardiaRoutingModule } from './guardia-routing.module';
import { AdministrativeDischargeComponent } from './routes/administrative-discharge/administrative-discharge.component';
import { AdmisionAdministrativaComponent } from './routes/admision-administrativa/admision-administrativa.component';
import { EditEmergencyCareEpisodeComponent } from './routes/edit-emergency-care-episode/edit-emergency-care-episode.component';
import { EpisodeDetailsComponent } from './routes/episode-details/episode-details.component';
import { HomeComponent } from './routes/home/home.component';
import { MedicalDischargeComponent } from './routes/medical-discharge/medical-discharge.component';
import { NewEpisodeAdminTriageComponent } from './routes/new-episode-admin-triage/new-episode-admin-triage.component';
import { NewEpisodeAdmissionComponent } from './routes/new-episode-admission/new-episode-admission.component';
import { NewEpisodeAdultGynecologicalTriageComponent } from './routes/new-episode-adult-gynecological-triage/new-episode-adult-gynecological-triage.component';
import { NewEpisodePediatricTriageComponent } from './routes/new-episode-pediatric-triage/new-episode-pediatric-triage.component';
// components
import { AdministrativeTriageComponent } from './components/administrative-triage/administrative-triage.component';
import { AdultGynecologicalTriageComponent } from './components/adult-gynecological-triage/adult-gynecological-triage.component';
import { PediatricTriageComponent } from './components/pediatric-triage/pediatric-triage.component';
import { TriageChipComponent } from './components/triage-chip/triage-chip.component';
import { TriageComponent } from './components/triage/triage.component';
import { TriageDetailsComponent } from './components/triage-details/triage-details.component';
// dialogs
import { AdministrativeTriageDialogComponent } from './dialogs/administrative-triage-dialog/administrative-triage-dialog.component';
import { AdultGynecologicalTriageDialogComponent } from './dialogs/adult-gynecological-triage-dialog/adult-gynecological-triage-dialog.component';
import { PediatricTriageDialogComponent } from './dialogs/pediatric-triage-dialog/pediatric-triage-dialog.component';
import { SelectConsultorioComponent } from './dialogs/select-consultorio/select-consultorio.component';
// services
import { EpisodeStateService } from './services/episode-state.service';
import { NewEpisodeService } from './services/new-episode.service';

@NgModule({
	declarations: [
		// routing
		AdministrativeDischargeComponent,
		AdmisionAdministrativaComponent,
		EditEmergencyCareEpisodeComponent,
		EpisodeDetailsComponent,
		HomeComponent,
		MedicalDischargeComponent,
		NewEpisodeAdminTriageComponent,
		NewEpisodeAdmissionComponent,
		NewEpisodeAdultGynecologicalTriageComponent,
		NewEpisodePediatricTriageComponent,
		// components
		AdministrativeTriageComponent,
		AdultGynecologicalTriageComponent,
		PediatricTriageComponent,
		TriageChipComponent,
		TriageComponent,
		TriageDetailsComponent,
		// dialogs
		AdministrativeTriageDialogComponent,
		AdultGynecologicalTriageDialogComponent,
		PediatricTriageDialogComponent,
		SelectConsultorioComponent,
	],
	imports: [
		CommonModule,
		// routing
		GuardiaRoutingModule,
		// deps
		HistoriaClinicaModule,
		InstitucionModule,
		LazyMaterialModule,
		PresentationModule,
	],
	providers: [
		// services
		EpisodeStateService,
		NewEpisodeService,
	]
})
export class GuardiaModule {
}
