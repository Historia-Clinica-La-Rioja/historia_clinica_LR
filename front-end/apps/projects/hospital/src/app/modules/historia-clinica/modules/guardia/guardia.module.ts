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
import { TitledGridSummaryComponent } from './components/titled-grid-summary/titled-grid-summary.component';
import { EmergencyCareAttentionPlacesComponent } from './components/emergency-care-attention-places/emergency-care-attention-places.component';
import { EmergencyCareAttentionPlacesDashboardComponent } from './components/emergency-care-attention-places-dashboard/emergency-care-attention-places-dashboard.component';
import { EmergencyCareAttentionPlaceSectorComponent } from './components/emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { EmergencyCareDashboardActionsComponent } from './components/emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { EmergencyCareElapsedTimeStateComponent } from './components/emergency-care-elapsed-time-state/emergency-care-elapsed-time-state.component';
import { EmergencyCareEvolutionsComponent } from './components/emergency-care-evolutions/emergency-care-evolutions.component';
import { EmergencyCareEvolutionNoteComponent } from './components/emergency-care-evolution-note/emergency-care-evolution-note.component';
import { EmergencyCareInAttentionStateComponent } from './components/emergency-care-in-attention-state/emergency-care-in-attention-state.component';
import { EmergencyCarePatientComponent } from './components/emergency-care-patient/emergency-care-patient.component';
import { EmergencyCarePatientDischargeStateComponent } from './components/emergency-care-patient-discharge-state/emergency-care-patient-discharge-state.component';
import { EmergencyCarePatientsSummaryComponent } from './components/emergency-care-patients-summary/emergency-care-patients-summary.component';
import { EmergencyCareTabsComponent } from './components/emergency-care-tabs/emergency-care-tabs.component';
import { EmergencyCarePatientSummaryItemComponent } from './components/emergency-care-patient-summary-item/emergency-care-patient-summary-item.component';
import { EmergencyCareStateSummaryItemComponent } from './components/emergency-care-state-summary-item/emergency-care-state-summary-item.component';
import { EmergencyCareTriageSummaryItemComponent } from './components/emergency-care-triage-summary-item/emergency-care-triage-summary-item.component';
import { EmergencyCareTemporaryPatientComponent } from './components/emergency-care-temporary-patient/emergency-care-temporary-patient.component';

import { EvolutionsSummaryComponent } from './components/evolutions-summary/evolutions-summary.component';
import { LastTriageComponent } from './components/last-triage/last-triage.component';
import { MedicalDischargeByNurseComponent } from './components/medical-discharge-by-nurse/medical-discharge-by-nurse.component';
import { MedicalDischargeTypesComponent } from './components/medical-discharge-types/medical-discharge-types.component';
import { PatientBasicInformationComponent } from './components/patient-basic-information/patient-basic-information.component';
import { PediatricTriageComponent } from './components/pediatric-triage/pediatric-triage.component';
import { TriageChipComponent } from './components/triage-chip/triage-chip.component';
import { TriageComponent } from './components/triage/triage.component';
import { TriageDetailsComponent } from './components/triage-details/triage-details.component';
import { TriageLevelSummaryComponent } from './components/triage-level-summary/triage-level-summary.component';
import { TriageSummaryComponent } from './components/triage-summary/triage-summary.component';
import { ReasonsFormComponent } from './components/reasons-form/reasons-form.component';
import { SpecialtySectorFormComponent } from './components/specialty-sector-form/specialty-sector-form.component';
import { SpecialtySummaryComponent } from './components/specialty-summary/specialty-summary.component';
// dialogs
import { AdministrativeTriageDialogComponent } from './dialogs/administrative-triage-dialog/administrative-triage-dialog.component';
import { AdultGynecologicalTriageDialogComponent } from './dialogs/adult-gynecological-triage-dialog/adult-gynecological-triage-dialog.component';
import { AttentionPlaceDialogComponent } from './dialogs/attention-place-dialog/attention-place-dialog.component';
import { PediatricTriageDialogComponent } from './dialogs/pediatric-triage-dialog/pediatric-triage-dialog.component';
// services
import { EpisodeStateService } from './services/episode-state.service';
import { NewEpisodeService } from './services/new-episode.service';
import { SpecialtySectorFormValidityService } from './services/specialty-sector-form-validity.service';
// standalone
import { EmergencyCareStatusLabelsComponent } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';
import { IdentifierCasesComponent } from '@hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '@hsi-components/patient-summary/patient-summary.component';
import { TemporaryPatientComponent } from '@hsi-components/temporary-patient/temporary-patient.component';

@NgModule({
	declarations: [
		// routing
		AdministrativeDischargeComponent,
		AdmisionAdministrativaComponent,
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
		EmergencyCareAttentionPlacesComponent,
		EmergencyCareAttentionPlacesDashboardComponent,
		EmergencyCareAttentionPlaceSectorComponent,
		EmergencyCareDashboardActionsComponent,
		EmergencyCareElapsedTimeStateComponent,
		EmergencyCareInAttentionStateComponent,
		EmergencyCarePatientComponent,
		EmergencyCarePatientDischargeStateComponent,
		EmergencyCarePatientsSummaryComponent,
		EmergencyCarePatientSummaryItemComponent,
		EmergencyCareStateSummaryItemComponent,
		EmergencyCareTabsComponent,
		EmergencyCareTriageSummaryItemComponent,
		EmergencyCareTemporaryPatientComponent,
		EvolutionsSummaryComponent,
		MedicalDischargeByNurseComponent,
		MedicalDischargeTypesComponent,
		PatientBasicInformationComponent,
		PediatricTriageComponent,
		TitledGridSummaryComponent,
		TriageChipComponent,
		TriageComponent,
		TriageDetailsComponent,
		TriageLevelSummaryComponent,
		TriageSummaryComponent,
		ReasonsFormComponent,
		SpecialtySectorFormComponent,
        SpecialtySummaryComponent,
		// dialogs
		AdministrativeTriageDialogComponent,
		AdultGynecologicalTriageDialogComponent,
		PediatricTriageDialogComponent,
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
		EmergencyCareStatusLabelsComponent,
		IdentifierCasesComponent,
		PatientSummaryComponent,
		TemporaryPatientComponent,
	],
	exports: [
		TriageDetailsComponent,
		TriageChipComponent,
		EmergencyCareDashboardActionsComponent,
		EmergencyCareEvolutionsComponent,
		LastTriageComponent,
	],
	providers: [
		// services
		EpisodeStateService,
		NewEpisodeService,
		SpecialtySectorFormValidityService
	]
})
export class GuardiaModule {
}
