import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuardiaRoutingModule } from './guardia-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { AdmisionAdministrativaComponent } from './routes/admision-administrativa/admision-administrativa.component';
import { CoreModule } from '@core/core.module';
import { InstitucionModule } from '../../../institucion/institucion.module';
import { PresentationModule } from '@presentation/presentation.module';
import { TriageComponent } from './components/triage/triage.component';
import { AdministrativeTriageComponent } from './components/administrative-triage/administrative-triage.component';
import { NewEpisodeAdminTriageComponent } from './routes/new-episode-admin-triage/new-episode-admin-triage.component';
import { AdministrativeTriageDialogComponent } from './dialogs/administrative-triage-dialog/administrative-triage-dialog.component';

import { HistoriaClinicaModule } from '../../historia-clinica.module';
import { NewEpisodeService } from './services/new-episode.service';
import { EpisodeStateService } from './services/episode-state.service';
import { SelectConsultorioComponent } from './dialogs/select-consultorio/select-consultorio.component';
import { EpisodeDetailsComponent } from './routes/episode-details/episode-details.component';
import { AdultGynecologicalTriageComponent } from './components/adult-gynecological-triage/adult-gynecological-triage.component';
import { NewEpisodeAdultGynecologicalTriageComponent } from './routes/new-episode-adult-gynecological-triage/new-episode-adult-gynecological-triage.component';
import { PediatricTriageComponent } from './components/pediatric-triage/pediatric-triage.component';
import { NewEpisodePediatricTriageComponent } from './routes/new-episode-pediatric-triage/new-episode-pediatric-triage.component';
import { TriageDetailsComponent } from './components/triage-details/triage-details.component';
import { TriageChipComponent } from './components/triage-chip/triage-chip.component';
import { PediatricTriageDialogComponent } from './dialogs/pediatric-triage-dialog/pediatric-triage-dialog.component';
import { AdultGynecologicalTriageDialogComponent } from './dialogs/adult-gynecological-triage-dialog/adult-gynecological-triage-dialog.component';
import { MedicalDischargeComponent } from './routes/medical-discharge/medical-discharge.component';
import { AdministrativeDischargeComponent } from './routes/administrative-discharge/administrative-discharge.component';
import { NewEpisodeAdmissionComponent } from './routes/new-episode-admission/new-episode-admission.component';
import { EditEmergencyCareEpisodeComponent } from './routes/edit-emergency-care-episode/edit-emergency-care-episode.component';

@NgModule({
	declarations: [
		HomeComponent,
		TriageComponent,
		AdministrativeTriageComponent,
		NewEpisodeAdminTriageComponent,
		AdmisionAdministrativaComponent,
		AdministrativeTriageDialogComponent,
		AdultGynecologicalTriageComponent,
		NewEpisodeAdultGynecologicalTriageComponent,
		PediatricTriageComponent,
		NewEpisodePediatricTriageComponent,
		SelectConsultorioComponent,
		EpisodeDetailsComponent,
		TriageDetailsComponent,
		TriageChipComponent,
		PediatricTriageDialogComponent,
		AdultGynecologicalTriageDialogComponent,
		MedicalDischargeComponent,
		AdministrativeDischargeComponent,
		NewEpisodeAdmissionComponent,
		EditEmergencyCareEpisodeComponent,
	],
	imports: [
		CommonModule,
		GuardiaRoutingModule,
		CoreModule,
		PresentationModule,
		GuardiaRoutingModule,
		InstitucionModule,
		PresentationModule,
		HistoriaClinicaModule,
	],
	providers: [
		NewEpisodeService,
		EpisodeStateService
	]
})
export class GuardiaModule {
}
