import { NgModule } from '@angular/core';
import { ClipboardModule } from "@angular/cdk/clipboard";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';
//deps
import { AccessManagementModule } from '@access-management/access-management.module';
import { AmbulatoriaRoutingModule } from './ambulatoria-routing.module';
import { ClapModule } from './modules/clap/clap.module';
import { EstudioModule } from './modules/estudio/estudio.module';
import { ExtensionsModule } from '@extensions/extensions.module';
import { GuardiaModule } from '../guardia/guardia.module';
import { HistoriaClinicaModule } from '../../historia-clinica.module';
import { IndicacionModule } from "@historia-clinica/modules/ambulatoria/modules/indicacion/indicacion.module";
import { InternacionesModule } from "@historia-clinica/modules/ambulatoria/modules/internacion/internaciones.module";
import { OdontologiaModule } from '../odontologia/odontologia.module';
import { PacientesModule } from '../../../pacientes/pacientes.module';
import { PresentationModule } from '@presentation/presentation.module';
import { TurnosModule } from './../../../turnos/turnos.module';
//routes
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { EpisodeSummaryComponent } from './routes/episode-summary/episode-summary.component';
import { HomeComponent } from './routes/home/home.component';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { PrintAmbulatoriaComponent } from './routes/print-ambulatoria/print-ambulatoria.component';
//declarations
import { AmendedProblemsComponent } from './components/amended-problems/amended-problems.component';
import { AmendedProblemsInformationComponent } from './components/amended-problems-information/amended-problems-information.component';
import { AssociatedParameterizedFormInformationComponent } from './components/associated-parameterized-form-information/associated-parameterized-form-information.component';
import { AssociatedParameterizedFormListComponent } from './components/associated-parameterized-form-list/associated-parameterized-form-list.component';
import { CardEstudiosComponent } from './components/card-estudios/card-estudios.component';
import { CardIndicacionesComponent } from './components/card-indicaciones/card-indicaciones.component';
import { CarelinesAndSpecialtiesReferenceComponent } from './components/carelines-and-specialties-reference/carelines-and-specialties-reference.component';
import { ClinicalHistoryActionsComponent } from './components/clinical-history-actions/clinical-history-actions.component';
import { CompleteStudyComponent } from './dialogs/complete-study/complete-study.component';
import { CompleteStudyInformationComponent } from './components/complete-study-information/complete-study-information.component';
import { CompleteInfoComponent } from './components/complete-info/complete-info.component';
import { DestinationInstitutionReferenceComponent } from './components/destination-institution-reference/destination-institution-reference.component';
import { DeferredDateSelectorComponent } from './components/deferred-date-selector/deferred-date-selector.component';
import { DiagnosisSummaryComponent } from './components/diagnosis-summary/diagnosis-summary.component';
import { EmergencyCareProblemsComponent } from './components/emergency-care-problems/emergency-care-problems.component';
import { ExternalClinicalHistoryComponent } from './components/external-clinical-history/external-clinical-history.component';
import { GenericFinancedPharmacoSearchComponent } from './components/generic-financed-pharmaco-search/generic-financed-pharmaco-search.component';
import { GenericPharmacoItemComponent } from './components/generic-pharmaco-item/generic-pharmaco-item.component';
import { GuardiaComponent } from './components/guardia/guardia.component';
import { HistoricalProblemsFiltersComponent } from './components/historical-problems-filters/historical-problems-filters.component';
import { IsolationAlertHeaderComponent } from './components/isolation-alert-header/isolation-alert-header.component';
import { MainDiagnosisSummaryComponent } from './components/main-diagnosis-summary/main-diagnosis-summary.component';
import { MedicalCoverageSummaryViewComponent } from './components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { NewViolenceEpisodeSectionComponent } from './components/new-violence-episode-section/new-violence-episode-section.component';
import { OriginInstitutionReferenceComponent } from './components/origin-institution-reference/origin-institution-reference.component';
import { ParameterizedFormSectionComponent } from './components/parameterized-form-section/parameterized-form-section.component';
import { ParameterizedFormParametersSectionComponent } from './components/parameterized-form-parameters-section/parameterized-form-parameters-section.component';
import { ProblemasComponent } from './components/problemas/problemas.component';
import { ProblemsOptionsMenuComponent } from './components/problems-options-menu/problems-options-menu.component';
import { ReferenceCompleteStudyComponent } from './components/reference-complete-study/reference-complete-study.component';
import { ReferenceRequestDataComponent } from './components/reference-request-data/reference-request-data.component';
import { ReferenceStudyCloseComponent } from './components/reference-study-close/reference-study-close.component';
import { ReferenceStudyClosureInformationComponent } from './components/reference-study-closure-information/reference-study-closure-information.component';
import { ReferenceStudyComponent } from './components/reference-study/reference-study.component';
import { ResumenComponent } from './components/resumen/resumen.component';
import { ControlSelectTemplateComponent } from './components/control-select-template/control-select-template.component';
import { SuggestedFieldsPopupComponent } from '../../../presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { StudyInformationComponent } from './components/study-information/study-information.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { VacunasSummaryComponent } from './components/vacunas-summary/vacunas-summary.component';
import { ViewDatailsBtnComponent } from './components/view-datails-btn/view-datails-btn.component';
import { ViolenceSituationHistoryFiltersComponent } from './components/violence-situation-history-filters/violence-situation-history-filters.component';
import { ViolenceSituationImplementedActionsComponent } from './components/violence-situation-implemented-actions/violence-situation-implemented-actions.component';
import { ViolenceSituationListComponent } from './components/violence-situation-list/violence-situation-list.component';
import { ViolenceSituationPersonInformationComponent } from './components/violence-situation-person-information/violence-situation-person-information.component';
import { ViolenceSituationRelevantInformationSectionComponent } from './components/violence-situation-relevant-information-section/violence-situation-relevant-information-section.component';
import { ViolenceSituationsComponent } from './components/violence-situations/violence-situations.component';
import { ViolenceSituationViolentPersonInformationComponent } from './components/violence-situation-violent-person-information/violence-situation-violent-person-information.component';
import { AuditRequiredMedicationComponent } from './dialogs/ordenes-prescripciones/audit-required-medication/audit-required-medicine.component';
//pipes
import { ShowMissingAlertsPipe } from './pipes/show-missing-alerts.pipe';
import { MedicationPresentationPipe } from './pipes/medication-presentation.pipe';
import { TranslateDeviceTextPipe } from './pipes/translate-device-text';
//standalone componentes
import { ReferenceStateLabelComponent } from '../../../hsi-components/reference-state-label/reference-state-label.component';
import { IdentifierCasesComponent } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { ConceptTypeaheadSearchComponent } from '../../../hsi-components/concept-typeahead-search/concept-typeahead-search.component';
import { PatientSummaryComponent } from '@hsi-components/patient-summary/patient-summary.component';
//dialog
import { AgregarPrescripcionItemComponent } from './dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { AgregarVacunaComponent } from './dialogs/agregar-vacuna/agregar-vacuna.component';
import { AgregarVacunasComponent } from './dialogs/agregar-vacunas/agregar-vacunas.component';
import { AmendProblemComponent } from './dialogs/amend-problem/amend-problem.component';
import { CompletarEstudioComponent } from './dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { ConfirmarPrescripcionComponent } from './dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { CounterreferenceDockPopupComponent } from './dialogs/counterreference-dock-popup/counterreference-dock-popup.component';
import { CreateInternmentOrderComponent } from './dialogs/create-internment-order/create-internment-order.component';
import { CreateOutpatientOrderComponent } from './dialogs/create-outpatient-order/create-outpatient-order.component';
import { DetalleVacunaComponent } from './dialogs/detalle-vacuna/detalle-vacuna.component';
import { EnviarRecetaDigitalPorEmailComponent } from './dialogs/enviar-receta-digital-por-email/enviar-receta-digital-por-email.component';
import { EpidemiologicalReportComponent } from './dialogs/epidemiological-report/epidemiological-report.component';
import { ExternalClinicalHistoriesFiltersComponent } from './components/external-clinical-histories-filters/external-clinical-histories-filters.component';
import { FreeTextParameterComponent } from './components/free-text-parameter/free-text-parameter.component';
import { NewConsultationFamilyHistoryFormComponent } from './dialogs/new-consultation-family-history-form/new-consultation-family-history-form.component';
import { NewConsultationPersonalHistoryFormComponent } from './dialogs/new-consultation-personal-history-form/new-consultation-personal-history-form.component';
import { NewViolentPersonInfomationComponent } from './dialogs/new-violent-person-infomation/new-violent-person-infomation.component';
import { NuevaConsultaDockPopupComponent } from './dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { NuevaConsultaDockPopupEnfermeriaComponent } from './dialogs/nueva-consulta-dock-popup-enfermeria/nueva-consulta-dock-popup-enfermeria.component';
import { NumericalParameterComponent } from './components/numerical-parameter/numerical-parameter.component';
import { OldDigitalPrescriptionItemComponent } from './dialogs/old-digital-prescription-item/old-digital-prescription-item.component';
import { OperationDeniedComponent } from './dialogs/diagnosis-required/operation-denied.component';
import { OptionListParameterComponent } from './components/option-list-parameter/option-list-parameter.component';
import { ParameterizedFormDialogComponent } from './dialogs/parameterized-form-dialog/parameterized-form-dialog.component';
import { PatientValidatorPopupComponent } from './dialogs/patient-validator-popup/patient-validator-popup.component';
import { PersonalHistoryViewDetailsComponent } from './dialogs/personal-history-view-details/personal-history-view-details.component';
import { PreviousDataComponent } from './dialogs/previous-data/previous-data.component';
import { ReferenceComponent } from './dialogs/reference/reference.component';
import { ReferenceNotificationComponent } from './dialogs/reference-notification/reference-notification.component';
import { SearchSnomedConceptComponent } from './dialogs/search-snomed-concept/search-snomed-concept.component';
import { ShowAllergiesComponent } from './dialogs/show-allergies/show-allergies.component';
import { SnvsReportsResultComponent } from './dialogs/snvs-reports-result/snvs-reports-result.component';
import { SolveProblemComponent } from '../../dialogs/solve-problem/solve-problem.component';
import { VaccineSearchComponent } from './dialogs/vaccine-search/vaccine-search.component';
import { VerResultadosEstudioComponent } from './dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { ViolenceSituationDockPopupComponent } from './dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { AddDigitalPrescriptionItemComponent } from './dialogs/add-digital-prescription-item/add-digital-prescription-item.component';
//providers
import { HistoricalProblemsFacadeService } from './services/historical-problems-facade.service';
//review
import { ExternalSummaryCardComponent } from '@presentation/components/external-summary-card/external-summary-card.component';
import { InternacionPacienteComponent } from './modules/internacion/routes/internacion-paciente/internacion-paciente.component';
import { InternmentSummaryComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/routes/internment-summary/internment-summary.component";
import { ShowClosedFormsTemplateComponent } from './components/show-closed-forms-template/show-closed-forms-template.component';

@NgModule({
	declarations: [
		AddDigitalPrescriptionItemComponent,
		AgregarPrescripcionItemComponent,
		AgregarVacunaComponent,
		AgregarVacunasComponent,
		AmbulatoriaPacienteComponent,
		AmendProblemComponent,
		AmendedProblemsComponent,
		AmendedProblemsInformationComponent,
		AssociatedParameterizedFormInformationComponent,
		AssociatedParameterizedFormListComponent,
		AuditRequiredMedicationComponent,
		CarelinesAndSpecialtiesReferenceComponent,
		CardEstudiosComponent,
		CardIndicacionesComponent,
		ClinicalHistoryActionsComponent,
		CompletarEstudioComponent,
		CompleteInfoComponent,
		CompleteStudyComponent,
		CompleteStudyInformationComponent,
		ConfirmarPrescripcionComponent,
		ControlSelectTemplateComponent,
		CounterreferenceDockPopupComponent,
		CreateInternmentOrderComponent,
		CreateOutpatientOrderComponent,
		DeferredDateSelectorComponent,
		DestinationInstitutionReferenceComponent,
		DetalleVacunaComponent,
		DiagnosisSummaryComponent,
		EpidemiologicalReportComponent,
		EpisodeSummaryComponent,
		EmergencyCareProblemsComponent,
		EnviarRecetaDigitalPorEmailComponent,
		ExternalClinicalHistoriesFiltersComponent,
		ExternalClinicalHistoryComponent,
		ExternalSummaryCardComponent,
		FreeTextParameterComponent,
		GenericFinancedPharmacoSearchComponent,
		GenericPharmacoItemComponent,
		GuardiaComponent,
		HistoricalProblemsFiltersComponent,
		HomeComponent,
		InternacionPacienteComponent,
		InternmentSummaryComponent,
		IsolationAlertHeaderComponent,
		MainDiagnosisSummaryComponent,
		MedicalCoverageSummaryViewComponent,
		NewConsultationFamilyHistoryFormComponent,
		NewConsultationPersonalHistoryFormComponent,
		NewViolenceEpisodeSectionComponent,
		NewViolentPersonInfomationComponent,
		NuevaConsultaDockPopupComponent,
		NuevaConsultaDockPopupEnfermeriaComponent,
		NumericalParameterComponent,
		OldDigitalPrescriptionItemComponent,
		OperationDeniedComponent,
		OptionListParameterComponent,
		OriginInstitutionReferenceComponent,
		ParameterizedFormDialogComponent,
		ParameterizedFormParametersSectionComponent,
		ParameterizedFormSectionComponent,
		PatientProfileComponent,
		PatientValidatorPopupComponent,
		PersonalHistoryViewDetailsComponent,
		PreviousDataComponent,
		PrintAmbulatoriaComponent,
		ProblemasComponent,
		ProblemsOptionsMenuComponent,
		ReferenceComponent,
		ReferenceCompleteStudyComponent,
		ReferenceNotificationComponent,
		ReferenceRequestDataComponent,
		ReferenceStudyCloseComponent,
		ReferenceStudyClosureInformationComponent,
		ReferenceStudyComponent,
		ResumenComponent,
		SearchSnomedConceptComponent,
		ShowAllergiesComponent,
		ShowClosedFormsTemplateComponent,
		SnvsReportsResultComponent,
		SolveProblemComponent,
		SuggestedFieldsPopupComponent,
		StudyInformationComponent,
		VacunasComponent,
		VacunasSummaryComponent,
		VaccineSearchComponent,
		VerResultadosEstudioComponent,
		ViewDatailsBtnComponent,
		ViolenceSituationDockPopupComponent,
		ViolenceSituationHistoryFiltersComponent,
		ViolenceSituationImplementedActionsComponent,
		ViolenceSituationListComponent,
		ViolenceSituationPersonInformationComponent,
		ViolenceSituationRelevantInformationSectionComponent,
		ViolenceSituationViolentPersonInformationComponent,
		ViolenceSituationsComponent,
		//pipe
		MedicationPresentationPipe,
		TranslateDeviceTextPipe,
		ShowMissingAlertsPipe,
	],
	imports: [
		CommonModule,
		FormsModule,
		OverlayModule,
		PortalModule,
		PresentationModule,
		ExtensionsModule,
		HistoriaClinicaModule,
		AmbulatoriaRoutingModule,
		AccessManagementModule,
		PacientesModule,
		OdontologiaModule,
		ClipboardModule,
		IndicacionModule,
		InternacionesModule,
		EstudioModule,
		TurnosModule,
		ClapModule,
		GuardiaModule,
		//Standalone Component
		IdentifierCasesComponent,
		ReferenceStateLabelComponent,
		ConceptTypeaheadSearchComponent,
		PatientSummaryComponent,
	],
	providers: [
		HistoricalProblemsFacadeService,
	]
})
export class AmbulatoriaModule {
}
