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
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { InternmentPatientTableComponent } from './components/internment-patient-table/internment-patient-table.component';
import { InternmentEpisodeSummaryComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";
import { MedicacionComponent } from './components/medicacion/medicacion.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { ElementoDiagnosticoComponent } from './components/elemento-diagnostico/elemento-diagnostico.component';
import { InternmentPatientCardComponent } from './components/internment-patient-card/internment-patient-card.component';
// dialogs
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import { ChangeMainDiagnosisDockPopupComponent } from './dialogs/change-main-diagnosis-dock-popup/change-main-diagnosis-dock-popup.component';
import { DiagnosisClinicalEvaluationDockPopupComponent } from './dialogs/diagnosis-clinical-evaluation-dock-popup/diagnosis-clinical-evaluation-dock-popup.component';
import { EpicrisisDockPopupComponent } from './dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component';
import { EvolutionNoteDockPopupComponent } from './dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component';
import { MedicalDischargeComponent } from './dialogs/medical-discharge/medical-discharge.component';
import { DiagnosisCreationEditionComponent } from './dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { SelectMainDiagnosisComponent } from './dialogs/select-main-diagnosis/select-main-diagnosis.component';
import { DocumentActionReasonComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-action-reason/document-action-reason.component";
import { TurnosModule } from '@turnos/turnos.module';

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
		DiagnosticosComponent,
		InternmentPatientTableComponent,
		InternmentPatientCardComponent,
		InternmentEpisodeSummaryComponent,
		MedicacionComponent,
		VacunasComponent,
		// dialogs
		AnamnesisDockPopupComponent,
		ChangeMainDiagnosisDockPopupComponent,
		DiagnosisClinicalEvaluationDockPopupComponent,
		EpicrisisDockPopupComponent,
		EvolutionNoteDockPopupComponent,
		MedicalDischargeComponent,
		ElementoDiagnosticoComponent,
		DiagnosisCreationEditionComponent,
		SelectMainDiagnosisComponent,
		DocumentActionReasonComponent
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
		TurnosModule
	]
})
export class InternacionesModule { }
