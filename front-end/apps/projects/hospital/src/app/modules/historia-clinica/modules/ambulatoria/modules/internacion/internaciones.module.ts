import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// deps
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
import { LazyMaterialModule } from 'projects/hospital/src/app/modules/lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { InternacionesRoutingModule } from './internaciones-routing.module';
import { InternacionesHomeComponent } from './routes/home/internaciones-home.component';
import { NewInternmentComponent } from './routes/new-internment/new-internment.component';
import { PatientBedRelocationComponent } from './routes/patient-bed-relocation/patient-bed-relocation.component';
import { PatientDischargeComponent } from './routes/patient-discharge/patient-discharge.component';
import { AlergiasComponent } from './components/alergias/alergias.component';
// components
import { AntecedentesFamiliaresComponent } from './components/antecedentes-familiares/antecedentes-familiares.component';
import { AntecedentesPersonalesComponent } from './components/antecedentes-personales/antecedentes-personales.component';
import { EventSerchComponent } from './components/event-serch/event-serch.component';
import { ExternalCauseComponent } from './components/external-cause/external-cause.component';
import { InternmentPatientTableComponent } from './components/internment-patient-table/internment-patient-table.component';
import { InternmentEpisodeSummaryComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";
import { MedicacionComponent } from './components/medicacion/medicacion.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { InternmentPatientCardComponent } from './components/internment-patient-card/internment-patient-card.component';
import { TableEventComponent } from './components/table-event/table-event.component';
import { TypeOfPregnancyComponent } from './components/type-of-pregnancy/type-of-pregnancy.component';
import { PregnancyFormComponent } from './components/pregnancy-form/pregnancy-form.component';
import { ObstetricComponent } from './components/obstetric/obstetric.component';
import { FormDynamicNewBornComponent } from './components/form-dynamic-new-born/form-dynamic-new-born.component';
import { ProcedureComponent } from './components/procedure/procedure.component';
import { ListConceptComponent } from './components/list-concept/list-concept.component';
import { ComponentEvaluationManagerService } from '../../services/component-evaluation-manager.service';
import { SearchCasesComponent } from 'projects/hospital/src/app/modules/hsi-components/search-cases/search-cases.component';
// dialogs
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import { ConceptDateFormComponent } from './dialogs/concept-date-form/concept-date-form.component';
import { ChangeMainDiagnosisDockPopupComponent } from './dialogs/change-main-diagnosis-dock-popup/change-main-diagnosis-dock-popup.component';
import { DiagnosisClinicalEvaluationDockPopupComponent } from './dialogs/diagnosis-clinical-evaluation-dock-popup/diagnosis-clinical-evaluation-dock-popup.component';
import { EpicrisisDockPopupComponent } from './dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component';
import { EvolutionNoteDockPopupComponent } from './dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component';
import { FormMedicationComponent } from './dialogs/form-medication/form-medication.component';
import { MedicalDischargeComponent } from './dialogs/medical-discharge/medical-discharge.component';
import { DiagnosisCreationEditionComponent } from './dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { SelectMainDiagnosisComponent } from './dialogs/select-main-diagnosis/select-main-diagnosis.component';
import { DocumentActionReasonComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-action-reason/document-action-reason.component";
import { TurnosModule } from '@turnos/turnos.module';
import { IntermentDocumentEpisodeComponent } from './components/interment-document-episode/interment-document-episode.component';
import { AttachDocumentPopupComponent } from './dialogs/attach-document-popup/attach-document-popup.component';
import { DeleteDocumentPopupComponent } from './dialogs/delete-document-popup/delete-document-popup.component';
import { MedicationComponent } from './components/medication/medication.component';
import { AnesthesicReportAddProposedSurgeryComponent } from './dialogs/anesthesic-report-add-proposed-surgery/anesthesic-report-add-proposed-surgery.component';

@NgModule({
	declarations: [
		// routing
		InternacionesHomeComponent,
		NewInternmentComponent,
		PatientBedRelocationComponent,
		PatientDischargeComponent,
		// components
		AlergiasComponent,
		AntecedentesFamiliaresComponent,
		AntecedentesPersonalesComponent,
		ExternalCauseComponent,
		EventSerchComponent,
		FormDynamicNewBornComponent,
		InternmentPatientTableComponent,
		InternmentPatientCardComponent,
		InternmentEpisodeSummaryComponent,
		MedicacionComponent,
		MedicationComponent,
		ObstetricComponent,
		PregnancyFormComponent,
		VacunasComponent,
		TableEventComponent,
		TypeOfPregnancyComponent,
		ListConceptComponent,
		ProcedureComponent,
		// dialogs
		AnamnesisDockPopupComponent,
		ConceptDateFormComponent,
		ChangeMainDiagnosisDockPopupComponent,
		DiagnosisClinicalEvaluationDockPopupComponent,
		EpicrisisDockPopupComponent,
		EvolutionNoteDockPopupComponent,
		FormMedicationComponent,
		MedicalDischargeComponent,
		DiagnosisCreationEditionComponent,
		SelectMainDiagnosisComponent,
		DocumentActionReasonComponent,
		IntermentDocumentEpisodeComponent,
		AttachDocumentPopupComponent,
		DeleteDocumentPopupComponent,
  AnesthesicReportAddProposedSurgeryComponent,
	],
	exports: [
		InternmentEpisodeSummaryComponent,
		InternmentPatientTableComponent,
		InternmentPatientCardComponent,
	],
	imports: [
		CommonModule,
		FormsModule,
		// routing
		InternacionesRoutingModule,
		// deps
		HistoriaClinicaModule,
		PresentationModule,
		LazyMaterialModule,
		TurnosModule,
		SearchCasesComponent,
	],
	providers: [
		ComponentEvaluationManagerService,
	]
})
export class InternacionesModule { }
