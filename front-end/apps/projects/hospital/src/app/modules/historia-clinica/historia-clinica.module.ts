import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { InstitucionModule } from '../institucion/institucion.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// components
import { AlergiasFormComponent } from './components/alergias-form/alergias-form.component';
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { AllergyListComponent } from './components/allergy-list/allergy-list.component';
import { AnestheticReportDockPopupComponent } from './components/anesthetic-report-dock-popup/anesthetic-report-dock-popup.component';
import { AntecedentesFamiliaresFormComponent } from './components/antecedentes-familiares-form/antecedentes-familiares-form.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { BackgroundListComponent } from './components/background-list/background-list.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { ConceptTypeaheadSearchComponent } from './components/concept-typeahead-search/concept-typeahead-search.component';
import { DatosAntropometricosFormComponent } from './components/datos-antropometricos-form/datos-antropometricos-form.component';
import { DatosAntropometricosNuevaConsultaComponent } from './components/datos-antropometricos-nueva-consulta/datos-antropometricos-nueva-consulta.component';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { DiagnosticosFormComponent } from './components/diagnosticos-form/diagnosticos-form.component';
import { DocumentsSummaryComponent } from './components/documents-summary/documents-summary.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
import { ElementoDiagnosticoComponent } from './components/elemento-diagnostico/elemento-diagnostico.component';
import { EmergencyCareEpisodeAttendService } from './services/emergency-care-episode-attend.service';
import { EpisodeDataComponent } from './components/episode-data/episode-data.component';
import { EspecialidadFormComponent } from './components/especialidad-form/especialidad-form.component';
import { EvolucionFormComponent } from './components/evolucion-form/evolucion-form.component';
import { FactoresDeRiesgoFormComponent } from './components/factores-de-riesgo-form/factores-de-riesgo-form.component';
import { FactoresDeRiesgoFormV2Component } from './components/factores-de-riesgo-form-v2/factores-de-riesgo-form-v2.component';
import { FactoresDeRiesgoSummaryComponent } from './components/factores-de-riesgo-summary/factores-de-riesgo-summary.component';
import { HierarchicalUnitConsultationComponent } from './components/hierarchical-unit-consultation/hierarchical-unit-consultation.component';
import { BoxMessageComponent } from './components/box-message/box-message.component';
import { InternacionAntecedentesPersonalesSummaryComponent } from './components/internacion-antecedentes-personales-summary/internacion-antecedentes-personales-summary.component';
import { MedicacionSummaryComponent } from './components/medicacion-summary/medicacion-summary.component';
import { MedicacionesFormComponent } from './components/medicaciones-form/medicaciones-form.component';
import { MedicationListComponent } from './components/medication-list/medication-list.component';
import { MotivoFormComponent } from './components/motivo-form/motivo-form.component';
import { NewConsultationExpansionSectionComponent } from './components/new-consultation-expansion-section/new-consultation-expansion-section.component';
import { NotaDeEvolucionDockPopupComponent } from './components/nota-de-evolucion-dock-popup/nota-de-evolucion-dock-popup.component';
import { PersonalHistoriesBackgroundListComponent } from './components/personal-histories-background-list/personal-histories-background-list.component';
import { PersonalHistoriesSummaryComponent } from './components/personal-histories-summary/personal-histories-summary.component';
import { PatientProblemsSummaryComponent } from './components/patient-problems-summary/patient-problems-summary.component';
import { ProblemListComponent } from './components/problem-list/problem-list.component';
import { ProcedureAndDescriptionComponent } from './components/procedure-and-description/procedure-and-description.component';
import { ProcedureListComponent } from './components/procedure-list/procedure-list.component';
import { ProcedimientosFormComponent } from './components/procedimientos-form/procedimientos-form.component';
import { ProfessionalAndDescriptionComponent } from './components/professional-and-description/professional-and-description.component';
import { ReferenceRequestListComponent } from './components/reference-request-list/reference-request-list.component';
import { ReasonListComponent } from './components/reason-list/reason-list.component';
import { SurgicalReportDiagnosisComponent } from './components/surgical-report-diagnosis/surgical-report-diagnosis.component';
import { SurgicalReportDockPopupComponent } from './components/surgical-report-dock-popup/surgical-report-dock-popup.component';
import { SurgicalReportProceduresComponent } from './components/surgical-report-procedures/surgical-report-procedures.component';
import { SurgicalReportProfessionalInfoComponent } from './components/surgical-report-professional-info/surgical-report-professional-info.component';
import { SurgicalReportProfessionalTeamComponent } from './components/surgical-report-professional-team/surgical-report-professional-team.component';
import { SurgicalReportProsthesisComponent } from './components/surgical-report-prosthesis/surgical-report-prosthesis.component';
import { TemplateConceptTypeaheadSearchComponent } from './components/template-concept-typeahead-search/template-concept-typeahead-search.component';
import { ViolenceModalitiesListComponent } from './components/violence-modalities-list/violence-modalities-list.component';
import { ViolenceSituationsListComponent } from './components/violence-situations-list/violence-situations-list.component';
import { ViolentPersonListComponent } from './components/violent-person-list/violent-person-list.component';
// dialogs
import { AddAllergyComponent } from './dialogs/add-allergy/add-allergy.component';
import { AddAnthropometricComponent } from './dialogs/add-anthropometric/add-anthropometric.component';
import { AddInmunizationComponent } from './dialogs/add-inmunization/add-inmunization.component';
import { AddRiskFactorsComponent } from './dialogs/add-risk-factors/add-risk-factors.component';
import { AuditAccessRegisterComponent } from './dialogs/audit-access-register/audit-access-register.component';
import { BedAssignmentComponent } from './dialogs/bed-assignment/bed-assignment.component';
import { ConceptsSearchDialogComponent } from './dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { ConceptsTypeaheadSearchDialogComponent } from './dialogs/concepts-typeahead-search-dialog/concepts-typeahead-search-dialog.component';
import { EffectiveTimeDialogComponent } from './dialogs/effective-time-dialog/effective-time-dialog.component';
import { NewConsultationAddProblemFormComponent } from './dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { NewConsultationAddReasonFormComponent } from './dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { NewConsultationAllergyFormComponent } from './dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { NewConsultationMedicationFormComponent } from './dialogs/new-consultation-medication-form/new-consultation-medication-form.component';
import { NewConsultationProcedureFormComponent } from './dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { ProbableDischargeDialogComponent } from './dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { ProblemConceptSearchDialogComponent } from './dialogs/problem-concept-search-dialog/problem-concept-search-dialog.component';
import { RemoveDiagnosisComponent } from './dialogs/remove-diagnosis/remove-diagnosis.component';
//Porque estan aca?
import { InternmentSummaryFacadeService } from './modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { ProposedSurgeryComponent } from './modules/ambulatoria/modules/internacion/components/proposed-surgery/proposed-surgery.component';
import { ProposedSurgeryBackgroundListComponent } from './modules/ambulatoria/modules/internacion/components/proposed-surgery-background-list/proposed-surgery-background-list.component';
import { AnestheticReportAnthropometricDataComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-anthropometric-data/anesthetic-report-anthropometric-data.component';
import { AnestheticReportClinicalEvaluationComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-clinical-evaluation/anesthetic-report-clinical-evaluation.component';
import { AnestheticReportAnestheticHistoryComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-anesthetic-history/anesthetic-report-anesthetic-history.component';
import { AnestheticReportUsualMedicationComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-usual-medication/anesthetic-report-usual-medication.component';
import { AnestheticReportPremedicationAndFoodIntakeComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-premedication-and-food-intake/anesthetic-report-premedication-and-food-intake.component';
import { MedicationBackgroundListComponent } from './modules/ambulatoria/modules/internacion/components/medication-background-list/medication-background-list.component';
import { AnestheticReportPersonRecordComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-person-record/anesthetic-report-person-record.component';
import { AnestheticPlanComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-plan/anesthetic-plan.component';
import { AnalgesicTechniqueBackgroundListComponent } from './modules/ambulatoria/modules/internacion/components/analgesic-technique-background-list/analgesic-technique-background-list.component';
import { AnalgesicTechniquePopupComponent } from './modules/ambulatoria/modules/internacion/dialogs/analgesic-technique-popup/analgesic-technique-popup.component';
import { AnalgesicTechniqueComponent } from './modules/ambulatoria/modules/internacion/components/analgesic-technique/analgesic-technique.component';
import { AnestheticTechniquePopupComponent } from './modules/ambulatoria/modules/internacion/dialogs/anesthetic-technique-popup/anesthetic-technique-popup.component';
import { AnestheticTechniqueComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-technique/anesthetic-technique.component';
import { AnestheticTechniqueListComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-technique-list/anesthetic-technique-list.component';
import { FluidAdministrationComponent } from './modules/ambulatoria/modules/internacion/fluid-administration/fluid-administration.component';
import { FluidAdministrationPopupComponent } from './modules/ambulatoria/modules/internacion/fluid-administration-popup/fluid-administration-popup.component';
import { FluidAdministrationListComponent } from './modules/ambulatoria/modules/internacion/fluid-administration-list/fluid-administration-list.component';
import { AnestheticReportIntrasurgicalAnestheticProceduresComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-intrasurgical-anesthetic-procedures/anesthetic-report-intrasurgical-anesthetic-procedures.component';
import { AnestheticReportAnestheticAgentsComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-anesthetic-agents/anesthetic-report-anesthetic-agents.component';
import { AnestheticReportNonAnestheticDrugsComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-non-anesthetic-drugs/anesthetic-report-non-anesthetic-drugs.component';
import { AnestheticReportAntibioticProphylaxisComponent } from './modules/ambulatoria/modules/internacion/components/anesthetic-report-antibiotic-prophylaxis/anesthetic-report-antibiotic-prophylaxis.component';

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
		BoxMessageComponent,
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
		ProposedSurgeryComponent,
		ProposedSurgeryBackgroundListComponent,
		AnestheticReportAnthropometricDataComponent,
		AnestheticReportClinicalEvaluationComponent,
		AnestheticReportAnestheticHistoryComponent,
		AnestheticReportUsualMedicationComponent,
		AnestheticReportPremedicationAndFoodIntakeComponent,
		MedicationBackgroundListComponent,
		AnestheticReportPersonRecordComponent,
		AnestheticPlanComponent,
		AnalgesicTechniqueBackgroundListComponent,
		AnalgesicTechniqueComponent,
		AnestheticTechniqueComponent,
		AnestheticTechniqueListComponent,
		FluidAdministrationListComponent,
		FluidAdministrationComponent,
		AnestheticReportIntrasurgicalAnestheticProceduresComponent,
		AnestheticReportAnestheticAgentsComponent,
		AnestheticReportNonAnestheticDrugsComponent,
		AnestheticReportAntibioticProphylaxisComponent,
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
 		AnestheticReportDockPopupComponent,
  		AnestheticReportDockPopupComponent,
		AnestheticTechniquePopupComponent,
		AnalgesicTechniquePopupComponent,
		FluidAdministrationPopupComponent
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
		BoxMessageComponent,
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
