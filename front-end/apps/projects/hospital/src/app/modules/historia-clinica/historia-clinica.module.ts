import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { InstitucionModule } from '../institucion/institucion.module';
import { PresentationModule } from '@presentation/presentation.module';
// components
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { AntecedentesPersonalesSummaryComponent } from './components/antecedentes-personales-summary/antecedentes-personales-summary.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { ConceptTypeaheadSearchComponent } from './components/concept-typeahead-search/concept-typeahead-search.component';
import { DocumentsSummaryComponent } from './components/documents-summary/documents-summary.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
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

@NgModule({
	declarations: [
		// components
		AlergiasSummaryComponent,
		AntecedentesFamiliaresSummaryComponent,
		AntecedentesPersonalesSummaryComponent,
		AntropometricosSummaryComponent,
		ConceptsSearchComponent,
		DocumentsSummaryComponent,
		EffectiveTimeComponent,
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
		AntecedentesPersonalesSummaryComponent,
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
		// dialogs
		ConceptsSearchDialogComponent,
		ConceptsTypeaheadSearchDialogComponent,
		NewConsultationAddProblemFormComponent,
	],
	providers: [
		InternmentSummaryFacadeService
	]
})
export class HistoriaClinicaModule {
}
