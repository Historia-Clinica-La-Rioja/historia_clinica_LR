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
import { EmergencyCareAttentionPlaceAttentionStateComponent } from './components/emergency-care-attention-place-attention-state/emergency-care-attention-place-attention-state.component';
import { EmergencyCareAttentionPlaceDetailsComponent } from './components/emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { EmergencyCareAttentionPlaceEntryDetailsComponent } from './components/emergency-care-attention-place-entry-details/emergency-care-attention-place-entry-details.component';
import { EmergencyCareAttentionPlaceLastTriageComponent } from './components/emergency-care-attention-place-last-triage/emergency-care-attention-place-last-triage.component';
import { EmergencyCareAttentionPlacePatientComponent } from './components/emergency-care-attention-place-patient/emergency-care-attention-place-patient.component';
import { EmergencyCareAttentionPlacesComponent } from './components/emergency-care-attention-places/emergency-care-attention-places.component';
import { EmergencyCareAttentionPlacesDashboardComponent } from './components/emergency-care-attention-places-dashboard/emergency-care-attention-places-dashboard.component';
import { EmergencyCareAttentionPlaceSectorComponent } from './components/emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { EmergencyCareAttentionPlaceSpaceComponent } from './components/emergency-care-attention-place-space/emergency-care-attention-place-space.component';
import { EmergencyCareChangeAttentionPlaceButtonComponent } from './components/emergency-care-change-attention-place-button/emergency-care-change-attention-place-button.component';
import { EmergencyCareChangeAttentionPlacePatientComponent } from './components/emergency-care-change-attention-place-patient/emergency-care-change-attention-place-patient.component';
import { EmergencyCareChangeAttentionPlaceSelectPlaceTypeComponent } from './components/emergency-care-change-attention-place-select-place-type/emergency-care-change-attention-place-select-place-type.component';
import { EmergencyCareChangeAttentionPlaceSelectSectorComponent } from './components/emergency-care-change-attention-place-select-sector/emergency-care-change-attention-place-select-sector.component';
import { EmergencyCareChangeAttentionPlaceStepperComponent } from './components/emergency-care-change-attention-place-stepper/emergency-care-change-attention-place-stepper.component';
import { EmergencyCareDashboardActionsComponent } from './components/emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { EmergencyCareDetailPlaceTypeComponent } from './components/emergency-care-detail-place-type/emergency-care-detail-place-type.component';
import { EmergencyCareElapsedTimeStateComponent } from './components/emergency-care-elapsed-time-state/emergency-care-elapsed-time-state.component';
import { EmergencyCareEpisodeFiltersComponent } from './components/emergency-care-episode-filters/emergency-care-episode-filters.component';
import { EmergencyCareEvolutionsComponent } from './components/emergency-care-evolutions/emergency-care-evolutions.component';
import { EmergencyCareEvolutionNoteComponent } from './components/emergency-care-evolution-note/emergency-care-evolution-note.component';
import { EmergencyCareInAttentionStateComponent } from './components/emergency-care-in-attention-state/emergency-care-in-attention-state.component';
import { EmergencyCarePatientComponent } from './components/emergency-care-patient/emergency-care-patient.component';
import { EmergencyCarePatientDischargeStateComponent } from './components/emergency-care-patient-discharge-state/emergency-care-patient-discharge-state.component';
import { EmergencyCareEpisodesSummaryComponent } from './components/emergency-care-episodes-summary/emergency-care-episodes-summary.component';
import { EmergencyCareTabsComponent } from './components/emergency-care-tabs/emergency-care-tabs.component';
import { EmergencyCarePatientSummaryItemComponent } from './components/emergency-care-patient-summary-item/emergency-care-patient-summary-item.component';
import { EmergencyCareStateSummaryItemComponent } from './components/emergency-care-state-summary-item/emergency-care-state-summary-item.component';
import { EmergencyCareTriageSummaryItemComponent } from './components/emergency-care-triage-summary-item/emergency-care-triage-summary-item.component';
import { EmergencyCareTemporaryPatientComponent } from './components/emergency-care-temporary-patient/emergency-care-temporary-patient.component';
import { EmergencyCareTypeCheckboxComponent } from './components/emergency-care-type-checkbox/emergency-care-type-checkbox.component';
import { EvolutionsSummaryComponent } from './components/evolutions-summary/evolutions-summary.component';
import { LastTriageComponent } from './components/last-triage/last-triage.component';
import { MedicalDischargeByNurseComponent } from './components/medical-discharge-by-nurse/medical-discharge-by-nurse.component';
import { MedicalDischargeTypesComponent } from './components/medical-discharge-types/medical-discharge-types.component';
import { PatientBasicInformationComponent } from './components/patient-basic-information/patient-basic-information.component';
import { PediatricTriageComponent } from './components/pediatric-triage/pediatric-triage.component';
import { ServiceChipsAutocompleteComponent } from './components/service-chips-autocomplete/service-chips-autocomplete.component';
import { StatesCheckboxComponent } from './components/states-checkbox/states-checkbox.component';
import { TriageCategoryCheckboxComponent } from './components/triage-category-checkbox/triage-category-checkbox.component';
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
import { EmergencyCareChangeAttentionPlaceDialogComponent } from './dialogs/emergency-care-change-attention-place-dialog/emergency-care-change-attention-place-dialog.component';
import { PediatricTriageDialogComponent } from './dialogs/pediatric-triage-dialog/pediatric-triage-dialog.component';
// services
import { EmergencyCareAttentionPlaceAvailabilityButtonSelectionService } from './services/emergency-care-attention-place-availability-button-selection.service';
import { EpisodeFilterService } from './services/episode-filter.service';
import { EpisodeStateService } from './services/episode-state.service';
import { NewEpisodeService } from './services/new-episode.service';
import { SpecialtySectorFormValidityService } from './services/specialty-sector-form-validity.service';
// standalone
import { EmergencyCareStatusLabelsComponent } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';
import { IdentifierCasesComponent } from '@hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '@hsi-components/patient-summary/patient-summary.component';
import { TemporaryPatientComponent } from '@hsi-components/temporary-patient/temporary-patient.component';
// pipes
import { ShowTriageCategoryDescriptionPipe } from './pipes/show-triage-category-description.pipe';
import { ShowTypeDescriptionPipe } from './pipes/show-type-description.pipe';
import { ShowStateDescriptionPipe } from './pipes/show-state-description.pipe';

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
		EmergencyCareAttentionPlaceAttentionStateComponent,
		EmergencyCareAttentionPlaceDetailsComponent,
		EmergencyCareAttentionPlaceEntryDetailsComponent,
		EmergencyCareAttentionPlaceLastTriageComponent,
		EmergencyCareAttentionPlacePatientComponent,
		EmergencyCareAttentionPlacesComponent,
		EmergencyCareAttentionPlacesDashboardComponent,
		EmergencyCareAttentionPlaceSectorComponent,
		EmergencyCareAttentionPlaceSpaceComponent,
		EmergencyCareChangeAttentionPlaceButtonComponent,
		EmergencyCareChangeAttentionPlacePatientComponent,
		EmergencyCareChangeAttentionPlaceSelectPlaceTypeComponent,
		EmergencyCareChangeAttentionPlaceSelectSectorComponent,
		EmergencyCareChangeAttentionPlaceStepperComponent,
		EmergencyCareDashboardActionsComponent,
		EmergencyCareDetailPlaceTypeComponent,
		EmergencyCareElapsedTimeStateComponent,
		EmergencyCareInAttentionStateComponent,
		EmergencyCareEpisodeFiltersComponent,
		EmergencyCarePatientComponent,
		EmergencyCarePatientDischargeStateComponent,
		EmergencyCareEpisodesSummaryComponent,
		EmergencyCarePatientSummaryItemComponent,
		EmergencyCareStateSummaryItemComponent,
		EmergencyCareTabsComponent,
		EmergencyCareTriageSummaryItemComponent,
		EmergencyCareTemporaryPatientComponent,
		EmergencyCareTypeCheckboxComponent,
		EvolutionsSummaryComponent,
		MedicalDischargeByNurseComponent,
		MedicalDischargeTypesComponent,
		PatientBasicInformationComponent,
		PediatricTriageComponent,
		StatesCheckboxComponent,
		TitledGridSummaryComponent,
		TriageCategoryCheckboxComponent,
		TriageChipComponent,
		TriageComponent,
		TriageDetailsComponent,
		TriageLevelSummaryComponent,
		TriageSummaryComponent,
		ReasonsFormComponent,
		ServiceChipsAutocompleteComponent,
		SpecialtySectorFormComponent,
		SpecialtySummaryComponent,
		// dialogs
		AdministrativeTriageDialogComponent,
		AdultGynecologicalTriageDialogComponent,
		PediatricTriageDialogComponent,
		EmergencyCareChangeAttentionPlaceDialogComponent,
		EmergencyCareEvolutionsComponent,
		LastTriageComponent,
		EmergencyCareEvolutionNoteComponent,
		AttentionPlaceDialogComponent,
		//pipes
		ShowTriageCategoryDescriptionPipe,
		ShowTypeDescriptionPipe,
		ShowStateDescriptionPipe,
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
		EmergencyCareAttentionPlaceAvailabilityButtonSelectionService,
		EpisodeFilterService,
		EpisodeStateService,
		NewEpisodeService,
		SpecialtySectorFormValidityService,
	]
})
export class GuardiaModule {
}
