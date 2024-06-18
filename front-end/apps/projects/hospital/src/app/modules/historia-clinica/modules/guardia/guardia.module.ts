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
import { HomeComponent } from './routes/home/home.component';
import { MedicalDischargeComponent } from './routes/medical-discharge/medical-discharge.component';
import { NewEpisodeAdminTriageComponent } from './routes/new-episode-admin-triage/new-episode-admin-triage.component';
import { NewEpisodeAdmissionComponent } from './routes/new-episode-admission/new-episode-admission.component';
import { NewEpisodeAdultGynecologicalTriageComponent } from './routes/new-episode-adult-gynecological-triage/new-episode-adult-gynecological-triage.component';
import { NewEpisodePediatricTriageComponent } from './routes/new-episode-pediatric-triage/new-episode-pediatric-triage.component';
// components
import { AdministrativeTriageComponent } from './components/administrative-triage/administrative-triage.component';
import { AdultGynecologicalTriageComponent } from './components/adult-gynecological-triage/adult-gynecological-triage.component';
import { DocumentActionsComponent } from './components/document-actions/document-actions.component';
import { EmergencyCareEvolutionsComponent } from './components/emergency-care-evolutions/emergency-care-evolutions.component';
import { EmergencyCareEvolutionNoteComponent } from './components/emergency-care-evolution-note/emergency-care-evolution-note.component';
import { EmergencyCarePatientComponent } from './components/emergency-care-patient/emergency-care-patient.component';
import { EmergencyCareTemporaryPatientComponent } from './components/emergency-care-temporary-patient/emergency-care-temporary-patient.component';
import { LastTriageComponent } from './components/last-triage/last-triage.component';
import { MedicalDischargeByNurseComponent } from './components/medical-discharge-by-nurse/medical-discharge-by-nurse.component';
import { PediatricTriageComponent } from './components/pediatric-triage/pediatric-triage.component';
import { TriageChipComponent } from './components/triage-chip/triage-chip.component';
import { TriageComponent } from './components/triage/triage.component';
import { TriageDetailsComponent } from './components/triage-details/triage-details.component';
import { ReasonsFormComponent } from './components/reasons-form/reasons-form.component';
// dialogs
import { AdministrativeTriageDialogComponent } from './dialogs/administrative-triage-dialog/administrative-triage-dialog.component';
import { AdultGynecologicalTriageDialogComponent } from './dialogs/adult-gynecological-triage-dialog/adult-gynecological-triage-dialog.component';
import { AttentionPlaceDialogComponent } from './dialogs/attention-place-dialog/attention-place-dialog.component';
import { PediatricTriageDialogComponent } from './dialogs/pediatric-triage-dialog/pediatric-triage-dialog.component';
import { SelectConsultorioComponent } from './dialogs/select-consultorio/select-consultorio.component';
// services
import { EpisodeStateService } from './services/episode-state.service';
import { NewEpisodeService } from './services/new-episode.service';
// standalone
import { PatientSummaryComponent } from '@hsi-components/patient-summary/patient-summary.component';
import { TemporaryPatientComponent } from '@hsi-components/temporary-patient/temporary-patient.component';

@NgModule({
	declarations: [
		// routing
		AdministrativeDischargeComponent,
		AdmisionAdministrativaComponent,
		DocumentActionsComponent,
		EditEmergencyCareEpisodeComponent,
		HomeComponent,
		MedicalDischargeComponent,
		NewEpisodeAdminTriageComponent,
		NewEpisodeAdmissionComponent,
		NewEpisodeAdultGynecologicalTriageComponent,
		NewEpisodePediatricTriageComponent,
		// components
		AdministrativeTriageComponent,
		AdultGynecologicalTriageComponent,
		EmergencyCarePatientComponent,
		EmergencyCareTemporaryPatientComponent,
		MedicalDischargeByNurseComponent,
		PediatricTriageComponent,
		TriageChipComponent,
		TriageComponent,
		TriageDetailsComponent,
		ReasonsFormComponent,
		// dialogs
		AdministrativeTriageDialogComponent,
		AdultGynecologicalTriageDialogComponent,
		PediatricTriageDialogComponent,
		SelectConsultorioComponent,
		EmergencyCareEvolutionsComponent,
		LastTriageComponent,
		EmergencyCareEvolutionNoteComponent,
		AttentionPlaceDialogComponent,
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
		// standalone
		PatientSummaryComponent,
		TemporaryPatientComponent,
	],
	exports: [
		TriageDetailsComponent,
		TriageChipComponent,
		EmergencyCareEvolutionsComponent,
		LastTriageComponent,
	],
	providers: [
		// services
		EpisodeStateService,
		NewEpisodeService,
	]
})
export class GuardiaModule {
}
