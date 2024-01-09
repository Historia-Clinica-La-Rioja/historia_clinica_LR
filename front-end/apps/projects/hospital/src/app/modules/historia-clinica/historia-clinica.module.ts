import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { InstitucionModule } from '../institucion/institucion.module';
import { PresentationModule } from '@presentation/presentation.module';
// components
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { PatientProblemsSummaryComponent } from './components/patient-problems-summary/patient-problems-summary.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { ConceptTypeaheadSearchComponent } from './components/concept-typeahead-search/concept-typeahead-search.component';
import { DocumentsSummaryComponent } from './components/documents-summary/documents-summary.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
import { HierarchicalUnitConsultationComponent } from './components/hierarchical-unit-consultation/hierarchical-unit-consultation.component';
import { InternacionAntecedentesPersonalesSummaryComponent } from './components/internacion-antecedentes-personales-summary/internacion-antecedentes-personales-summary.component';
import { MedicacionSummaryComponent } from './components/medicacion-summary/medicacion-summary.component';
import { FactoresDeRiesgoSummaryComponent } from './components/factores-de-riesgo-summary/factores-de-riesgo-summary.component';
import { FactoresDeRiesgoFormComponent } from './components/factores-de-riesgo-form/factores-de-riesgo-form.component';
import { NewConsultationExpansionSectionComponent } from './components/new-consultation-expansion-section/new-consultation-expansion-section.component';
import { ProcedureListComponent } from './components/procedure-list/procedure-list.component';
// dialogs
import { AddAllergyComponent } from './dialogs/add-allergy/add-allergy.component';
import { AddAnthropometricComponent } from './dialogs/add-anthropometric/add-anthropometric.component';
import { AddInmunizationComponent } from './dialogs/add-inmunization/add-inmunization.component';
import { AddRiskFactorsComponent } from './dialogs/add-risk-factors/add-risk-factors.component';
import { ConceptsSearchDialogComponent } from './dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { EffectiveTimeDialogComponent } from './dialogs/effective-time-dialog/effective-time-dialog.component';
import { ProbableDischargeDialogComponent } from './dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { RemoveDiagnosisComponent } from './dialogs/remove-diagnosis/remove-diagnosis.component';
import { BedAssignmentComponent } from './dialogs/bed-assignment/bed-assignment.component';
import { IncludePreviousDataQuestionComponent } from './components/include-previous-data-question/include-previous-data-question.component';
import { TemplateConceptTypeaheadSearchComponent } from './components/template-concept-typeahead-search/template-concept-typeahead-search.component';
import { ConceptsTypeaheadSearchDialogComponent } from './dialogs/concepts-typeahead-search-dialog/concepts-typeahead-search-dialog.component';
import { NewConsultationAddProblemFormComponent } from './dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { NewConsultationAddReasonFormComponent } from './dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { NewConsultationProcedureFormComponent } from './dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { NewConsultationAllergyFormComponent } from './dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { NewConsultationMedicationFormComponent } from './dialogs/new-consultation-medication-form/new-consultation-medication-form.component';
import { ReasonListComponent } from './components/reason-list/reason-list.component';
import { ProblemListComponent } from './components/problem-list/problem-list.component';
import { AllergyListComponent } from './components/allergy-list/allergy-list.component';
import { BackgroundListComponent } from './components/background-list/background-list.component';
import { MedicationListComponent } from './components/medication-list/medication-list.component';
import { ReferenceRequestListComponent } from './components/reference-request-list/reference-request-list.component';
import { InternmentSummaryFacadeService } from './modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { NotaDeEvolucionDockPopupComponent } from './components/nota-de-evolucion-dock-popup/nota-de-evolucion-dock-popup.component';
import { EspecialidadFormComponent } from './components/especialidad-form/especialidad-form.component';
import { MotivoFormComponent } from './components/motivo-form/motivo-form.component';
import { DiagnosticosFormComponent } from './components/diagnosticos-form/diagnosticos-form.component';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { ElementoDiagnosticoComponent } from './components/elemento-diagnostico/elemento-diagnostico.component';
import { EvolucionFormComponent } from './components/evolucion-form/evolucion-form.component';
import { DatosAntropometricosFormComponent } from './components/datos-antropometricos-form/datos-antropometricos-form.component';
import { DatosAntropometricosNuevaConsultaComponent } from './components/datos-antropometricos-nueva-consulta/datos-antropometricos-nueva-consulta.component';
import { AntecedentesFamiliaresFormComponent } from './components/antecedentes-familiares-form/antecedentes-familiares-form.component';
import { MedicacionesFormComponent } from './components/medicaciones-form/medicaciones-form.component';
import { ProcedimientosFormComponent } from './components/procedimientos-form/procedimientos-form.component';
import { FactoresDeRiesgoFormV2Component } from './components/factores-de-riesgo-form-v2/factores-de-riesgo-form-v2.component';
import { AlergiasFormComponent } from './components/alergias-form/alergias-form.component';
import { EmergencyCareEpisodeAttendService } from './services/emergency-care-episode-attend.service';
import { EpisodeDataComponent } from './components/episode-data/episode-data.component';
import { ProblemConceptSearchDialogComponent } from './dialogs/problem-concept-search-dialog/problem-concept-search-dialog.component';
import { SurgicalReportDockPopupComponent } from './components/surgical-report-dock-popup/surgical-report-dock-popup.component';
import { PersonalHistoriesBackgroundListComponent } from './components/personal-histories-background-list/personal-histories-background-list.component';
import { PersonalHistoriesSummaryComponent } from './components/personal-histories-summary/personal-histories-summary.component';
import { SurgicalReportDiagnosisComponent } from './components/surgical-report-diagnosis/surgical-report-diagnosis.component';
import { SurgicalReportProfessionalTeamComponent } from './components/surgical-report-professional-team/surgical-report-professional-team.component';
import { SurgicalReportProfessionalInfoComponent } from './components/surgical-report-professional-info/surgical-report-professional-info.component';
import { SurgicalReportProceduresComponent } from './components/surgical-report-procedures/surgical-report-procedures.component';
import { ProfessionalAndDescriptionComponent } from './components/professional-and-description/professional-and-description.component';
import { ProcedureAndDescriptionComponent } from './components/procedure-and-description/procedure-and-description.component';
import { SurgicalReportProsthesisComponent } from './components/surgical-report-prosthesis/surgical-report-prosthesis.component';
import { ViolenceSituationsListComponent } from './components/violence-situations-list/violence-situations-list.component';
import { ViolenceModalitiesListComponent } from './components/violence-modalities-list/violence-modalities-list.component';
import { ViolentPersonListComponent } from './components/violent-person-list/violent-person-list.component';
import { AuditAccessRegisterComponent } from './dialogs/audit-access-register/audit-access-register.component';

@NgModule({
	declarations: [
		// components
		AlergiasSummaryComponent,
		AntecedentesFamiliaresSummaryComponent,
		PatientProblemsSummaryComponent,
		AntropometricosSummaryComponent,
		ConceptsSearchComponent,
		DocumentsSummaryComponent,
		EffectiveTimeComponent,
		HierarchicalUnitConsultationComponent,
		InternacionAntecedentesPersonalesSummaryComponent,
		MedicacionSummaryComponent,
		FactoresDeRiesgoSummaryComponent,
		FactoresDeRiesgoFormComponent,
		IncludePreviousDataQuestionComponent,
		ConceptTypeaheadSearchComponent,
		TemplateConceptTypeaheadSearchComponent,
		NewConsultationExpansionSectionComponent,
		ProcedureListComponent,
		ReasonListComponent,
		ProblemListComponent,
		AllergyListComponent,
		BackgroundListComponent,
		MedicationListComponent,
		ReferenceRequestListComponent,
		EpisodeDataComponent,
		// dialogs
		AddAllergyComponent,
		AddAnthropometricComponent,
		AddInmunizationComponent,
		AddRiskFactorsComponent,
		ConceptsSearchDialogComponent,
		EffectiveTimeDialogComponent,
		RemoveDiagnosisComponent,
		ProbableDischargeDialogComponent,
		BedAssignmentComponent,
		ConceptsTypeaheadSearchDialogComponent,
		NewConsultationAddProblemFormComponent,
		NewConsultationAddReasonFormComponent,
		NewConsultationProcedureFormComponent,
		NewConsultationAllergyFormComponent,
		NewConsultationMedicationFormComponent,
		NotaDeEvolucionDockPopupComponent,
		EspecialidadFormComponent,
		MotivoFormComponent,
		DiagnosticosFormComponent,
		DiagnosticosComponent,
		ElementoDiagnosticoComponent,
		EvolucionFormComponent,
		DatosAntropometricosFormComponent,
		DatosAntropometricosNuevaConsultaComponent,
		AntecedentesFamiliaresFormComponent,
		MedicacionesFormComponent,
		ProcedimientosFormComponent,
		FactoresDeRiesgoFormV2Component,
		AlergiasFormComponent,
		ProblemConceptSearchDialogComponent,
		PersonalHistoriesBackgroundListComponent,
		PersonalHistoriesSummaryComponent,
		SurgicalReportDockPopupComponent,
		SurgicalReportDiagnosisComponent,
		SurgicalReportProfessionalTeamComponent,
		SurgicalReportProfessionalInfoComponent,
		SurgicalReportProceduresComponent,
		ProfessionalAndDescriptionComponent,
		ProcedureAndDescriptionComponent,
		SurgicalReportProsthesisComponent,
  		ViolenceSituationsListComponent,
    	ViolenceModalitiesListComponent,
     	ViolentPersonListComponent,
  		AuditAccessRegisterComponent,
	],
	imports: [
		CommonModule,
		// deps
		InstitucionModule,
		LazyMaterialModule,
		PresentationModule,
	],
	exports: [
		// components
		AlergiasSummaryComponent,
		AntecedentesFamiliaresSummaryComponent,
		PatientProblemsSummaryComponent,
		AntropometricosSummaryComponent,
		ConceptsSearchComponent,
		DocumentsSummaryComponent,
		EffectiveTimeComponent,

		MedicacionSummaryComponent,
		FactoresDeRiesgoSummaryComponent,
		InternacionAntecedentesPersonalesSummaryComponent,
		FactoresDeRiesgoFormComponent,
		IncludePreviousDataQuestionComponent,
		ConceptTypeaheadSearchComponent,
		TemplateConceptTypeaheadSearchComponent,
		NewConsultationExpansionSectionComponent,
		ProcedureListComponent,
		ReasonListComponent,
		ProblemListComponent,
		AllergyListComponent,
		BackgroundListComponent,
		MedicationListComponent,
		ReferenceRequestListComponent,
		BedAssignmentComponent,
		EpisodeDataComponent,
		PersonalHistoriesBackgroundListComponent,
		PersonalHistoriesSummaryComponent,
		ViolenceSituationsListComponent,
		ViolenceModalitiesListComponent,
		// dialogs
		ConceptsSearchDialogComponent,
		ConceptsTypeaheadSearchDialogComponent,
		NewConsultationAddProblemFormComponent,
		DiagnosticosComponent,
		DatosAntropometricosNuevaConsultaComponent,
		ProblemListComponent,
		ViolentPersonListComponent,
	],
	providers: [
		InternmentSummaryFacadeService,
		EmergencyCareEpisodeAttendService
	]
})
export class HistoriaClinicaModule {
}
