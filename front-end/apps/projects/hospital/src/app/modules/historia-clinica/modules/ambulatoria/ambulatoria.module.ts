import { TurnosModule } from './../../../turnos/turnos.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';

import { PresentationModule } from '@presentation/presentation.module';
import { ExtensionsModule } from '@extensions/extensions.module';
import { HistoriaClinicaModule } from '../../historia-clinica.module';
import { PacientesModule } from '../../../pacientes/pacientes.module';
import { OdontologiaModule } from '../odontologia/odontologia.module';
import { AmbulatoriaRoutingModule } from './ambulatoria-routing.module';
import { AccessManagementModule } from '@access-management/access-management.module';

import { HomeComponent } from './routes/home/home.component';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { ResumenComponent } from './components/resumen/resumen.component';
import { ProblemasComponent } from './components/problemas/problemas.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { SolveProblemComponent } from '../../dialogs/solve-problem/solve-problem.component';
import { HistoricalProblemsFiltersComponent } from './components/historical-problems-filters/historical-problems-filters.component';
import { NuevaConsultaDockPopupComponent } from './dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { ConfirmarPrescripcionComponent } from './dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { AgregarPrescripcionItemComponent } from './dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { CardEstudiosComponent } from './components/card-estudios/card-estudios.component';
import { CardIndicacionesComponent } from './components/card-indicaciones/card-indicaciones.component';
import { CompletarEstudioComponent } from './dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { VerResultadosEstudioComponent } from './dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { ExternalSummaryCardComponent } from '@presentation/components/external-summary-card/external-summary-card.component';
import { SuggestedFieldsPopupComponent } from '../../../presentation/components/suggested-fields-popup/suggested-fields-popup.component';

import { AgregarVacunaComponent } from './dialogs/agregar-vacuna/agregar-vacuna.component';
import { AgregarVacunasComponent } from './dialogs/agregar-vacunas/agregar-vacunas.component';
import { DetalleVacunaComponent } from './dialogs/detalle-vacuna/detalle-vacuna.component';
import { ExternalClinicalHistoriesFiltersComponent } from './components/external-clinical-histories-filters/external-clinical-histories-filters.component';
import { ExternalClinicalHistoryComponent } from './components/external-clinical-history/external-clinical-history.component';
import { NuevaConsultaDockPopupEnfermeriaComponent } from './dialogs/nueva-consulta-dock-popup-enfermeria/nueva-consulta-dock-popup-enfermeria.component';
import { EpidemiologicalReportComponent } from './dialogs/epidemiological-report/epidemiological-report.component';
import { PreviousDataComponent } from './dialogs/previous-data/previous-data.component';
import { ReferenceComponent } from './dialogs/reference/reference.component';
import { ReferenceNotificationComponent } from './dialogs/reference-notification/reference-notification.component';
import { CounterreferenceDockPopupComponent } from './dialogs/counterreference-dock-popup/counterreference-dock-popup.component';
import { SnvsReportsResultComponent } from './dialogs/snvs-reports-result/snvs-reports-result.component';
import { ClipboardModule } from "@angular/cdk/clipboard";
import { ShowAllergiesComponent } from './dialogs/show-allergies/show-allergies.component';
import { InternacionPacienteComponent } from './modules/internacion/routes/internacion-paciente/internacion-paciente.component';
import { InternmentSummaryComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/routes/internment-summary/internment-summary.component";
import { VacunasSummaryComponent } from './components/vacunas-summary/vacunas-summary.component';
import { MainDiagnosisSummaryComponent } from './components/main-diagnosis-summary/main-diagnosis-summary.component';
import { DiagnosisSummaryComponent } from './components/diagnosis-summary/diagnosis-summary.component';
import { IndicacionModule } from "@historia-clinica/modules/ambulatoria/modules/indicacion/indicacion.module";
import { MedicalCoverageSummaryViewComponent } from './components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { InternacionesModule } from "@historia-clinica/modules/ambulatoria/modules/internacion/internaciones.module";
import { CreateInternmentOrderComponent } from './dialogs/create-internment-order/create-internment-order.component';
import { EstudioModule } from './modules/estudio/estudio.module';
import { OperationDeniedComponent } from './dialogs/diagnosis-required/operation-denied.component';
import { CreateOutpatientOrderComponent } from './dialogs/create-outpatient-order/create-outpatient-order.component';
import { NewConsultationFamilyHistoryFormComponent } from './dialogs/new-consultation-family-history-form/new-consultation-family-history-form.component';
import { VaccineSearchComponent } from './dialogs/vaccine-search/vaccine-search.component';
import { ClinicalHistoryActionsComponent } from './components/clinical-history-actions/clinical-history-actions.component';
import { AmbulatoriaSummaryFacadeService } from './services/ambulatoria-summary-facade.service';
import { HistoricalProblemsFacadeService } from './services/historical-problems-facade.service';
import { ClapModule } from './modules/clap/clap.module';
import { GuardiaComponent } from './components/guardia/guardia.component';
import { GuardiaModule } from '../guardia/guardia.module';
import { EnviarRecetaDigitalPorEmailComponent } from './dialogs/enviar-receta-digital-por-email/enviar-receta-digital-por-email.component';
import { DestinationInstitutionReferenceComponent } from './components/destination-institution-reference/destination-institution-reference.component';
import { CarelinesAndSpecialtiesReferenceComponent } from './components/carelines-and-specialties-reference/carelines-and-specialties-reference.component';
import { OriginInstitutionReferenceComponent } from './components/origin-institution-reference/origin-institution-reference.component';
import { EpisodeSummaryComponent } from './routes/episode-summary/episode-summary.component';
import { EmergencyCareProblemsComponent } from './components/emergency-care-problems/emergency-care-problems.component';
import { PatientValidatorPopupComponent } from './dialogs/patient-validator-popup/patient-validator-popup.component';
import { PrintAmbulatoriaComponent } from './routes/print-ambulatoria/print-ambulatoria.component';
import { SearchSnomedConceptComponent } from './dialogs/search-snomed-concept/search-snomed-concept.component';
import { ProblemsOptionsMenuComponent } from './components/problems-options-menu/problems-options-menu.component';
import { AmendProblemComponent } from './dialogs/amend-problem/amend-problem.component';
import { AmendedProblemsComponent } from './components/amended-problems/amended-problems.component';
import { ViewDatailsBtnComponent } from './components/view-datails-btn/view-datails-btn.component';
import { AmendedProblemsInformationComponent } from './components/amended-problems-information/amended-problems-information.component';
import { ReferenceRequestDataComponent } from './components/reference-request-data/reference-request-data.component';
import { IdentifierCasesComponent } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { ReferenceStudyCloseComponent } from './components/reference-study-close/reference-study-close.component';
import { ReferenceCompleteStudyComponent } from './components/reference-complete-study/reference-complete-study.component';
import { ReferenceStudyClosureInformationComponent } from './components/reference-study-closure-information/reference-study-closure-information.component';
import { ReferenceStudyComponent } from './components/reference-study/reference-study.component';
import { NewConsultationPersonalHistoryFormComponent } from './dialogs/new-consultation-personal-history-form/new-consultation-personal-history-form.component';
import { PersonalHistoryViewDetailsComponent } from './dialogs/personal-history-view-details/personal-history-view-details.component';
import { ReferenceStateLabelComponent } from '../../../hsi-components/reference-state-label/reference-state-label.component';
import { ViolenceSituationsComponent } from './components/violence-situations/violence-situations.component';
import { ViolenceSituationDockPopupComponent } from './dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { ViolenceSituationPersonInformationComponent } from './components/violence-situation-person-information/violence-situation-person-information.component';
import { NewViolenceEpisodeSectionComponent } from './components/new-violence-episode-section/new-violence-episode-section.component';
import { ViolenceSituationRelevantInformationSectionComponent } from './components/violence-situation-relevant-information-section/violence-situation-relevant-information-section.component';
import { ViolenceSituationImplementedActionsComponent } from './components/violence-situation-implemented-actions/violence-situation-implemented-actions.component';
import { ViolenceSituationViolentPersonInformationComponent } from './components/violence-situation-violent-person-information/violence-situation-violent-person-information.component';
import { NewViolentPersonInfomationComponent } from './dialogs/new-violent-person-infomation/new-violent-person-infomation.component';
import { ViolenceSituationListComponent } from './components/violence-situation-list/violence-situation-list.component';
import { TablaSapComponent } from './components/tabla-sap/tabla-sap.component';
import { AdultoMayorComponent } from '@historia-clinica/components/emergente/inicio-estudio/inicio-estudio.component';
import { EstudiosPopupComponent } from '@historia-clinica/components/emergente/pop-up/estudios-popup.component';
import { FrailScaleComponent } from './components/cuestionarios/frail-scale/frail-scale.component';
import { EdmontonComponent } from './components/cuestionarios/edmonton/edmonton.component';
import { GetFrailComponent } from './components/cuestionarios/frail-scale/get-frail/get-frail.component';
import { LatestStudiesComponent } from '@historia-clinica/components/emergente/pop-up/latest-studies/latest-studies.component';
import { DesempenoFisicoComponent } from './components/cuestionarios/desempeno-fisico/desempeno-fisico.component';
import { InputDesempenoComponent } from './components/cuestionarios/desempeno-fisico/input-desempeno/input-desempeno.component';
import { GetEdmontonComponent } from './components/cuestionarios/edmonton/get-edmonton/get-edmonton.component';
import { GetPhysicalPerformanceComponent } from './components/cuestionarios/desempeno-fisico/get-physical-performance/get-physical-performance.component';

@NgModule({
	declarations: [
		HomeComponent,
		PatientProfileComponent,
		AmbulatoriaPacienteComponent,
		ResumenComponent,
		ProblemasComponent,
		VacunasComponent,
		SolveProblemComponent,
		HistoricalProblemsFiltersComponent,
		NuevaConsultaDockPopupComponent,
		ConfirmarPrescripcionComponent,
		AgregarPrescripcionItemComponent,
		CardEstudiosComponent,
		CardIndicacionesComponent,
		CompletarEstudioComponent,
		VerResultadosEstudioComponent,
		ExternalSummaryCardComponent,
		SuggestedFieldsPopupComponent,
		AgregarVacunasComponent,
		AgregarVacunaComponent,
		DetalleVacunaComponent,
		ExternalClinicalHistoriesFiltersComponent,
		NuevaConsultaDockPopupEnfermeriaComponent,
		ExternalClinicalHistoryComponent,
		PreviousDataComponent,
		EpidemiologicalReportComponent,
		ReferenceComponent,
		ReferenceNotificationComponent,
		CounterreferenceDockPopupComponent,
		SnvsReportsResultComponent,
		ShowAllergiesComponent,
		InternacionPacienteComponent,
		VacunasSummaryComponent,
		MainDiagnosisSummaryComponent,
		DiagnosisSummaryComponent,
		InternmentSummaryComponent,
		MedicalCoverageSummaryViewComponent,
		CreateInternmentOrderComponent,
		OperationDeniedComponent,
		CreateOutpatientOrderComponent,
		NewConsultationFamilyHistoryFormComponent,
		VaccineSearchComponent,
		ClinicalHistoryActionsComponent,
		GuardiaComponent,
		EnviarRecetaDigitalPorEmailComponent,
		DestinationInstitutionReferenceComponent,
		CarelinesAndSpecialtiesReferenceComponent,
		OriginInstitutionReferenceComponent,
		EpisodeSummaryComponent,
		EmergencyCareProblemsComponent,
		PatientValidatorPopupComponent,
		PrintAmbulatoriaComponent,
		SearchSnomedConceptComponent,
		ProblemsOptionsMenuComponent,
		AmendProblemComponent,
		AmendedProblemsComponent,
		ViewDatailsBtnComponent,
		AmendedProblemsInformationComponent,
		ReferenceRequestDataComponent,
		ReferenceStudyCloseComponent,
		ReferenceCompleteStudyComponent,
		ReferenceStudyClosureInformationComponent,
		ReferenceStudyComponent,
		NewConsultationPersonalHistoryFormComponent,
		PersonalHistoryViewDetailsComponent,
		ViolenceSituationsComponent,
		ViolenceSituationDockPopupComponent,
		ViolenceSituationPersonInformationComponent,
		NewViolenceEpisodeSectionComponent,
		ViolenceSituationRelevantInformationSectionComponent,
		ViolenceSituationImplementedActionsComponent,
		ViolenceSituationViolentPersonInformationComponent,
		NewViolentPersonInfomationComponent,
		ViolenceSituationImplementedActionsComponent,
		ViolenceSituationListComponent,
		TablaSapComponent,
		EstudiosPopupComponent,
		AdultoMayorComponent,
		EdmontonComponent,
		FrailScaleComponent,
		GetFrailComponent,
		LatestStudiesComponent,
		DesempenoFisicoComponent,
		InputDesempenoComponent,
		GetEdmontonComponent,
		GetPhysicalPerformanceComponent

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
	],
	providers: [
		AmbulatoriaSummaryFacadeService,
		HistoricalProblemsFacadeService,
	]
})
export class AmbulatoriaModule {
}
