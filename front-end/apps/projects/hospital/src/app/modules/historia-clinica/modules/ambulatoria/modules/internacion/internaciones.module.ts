import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// deps
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { InternacionesRoutingModule } from './internaciones-routing.module';
import { EvaluacionClinicaDiagnosticosComponent } from './routes/evaluacion-clinica-diagnosticos/evaluacion-clinica-diagnosticos.component';
import { InternacionesHomeComponent } from './routes/home/internaciones-home.component';
import { NewInternmentComponent } from './routes/new-internment/new-internment.component';
import { PatientBedRelocationComponent } from './routes/patient-bed-relocation/patient-bed-relocation.component';
import { PatientDischargeComponent } from './routes/patient-discharge/patient-discharge.component';
import { AlergiasComponent } from './components/alergias/alergias.component';
// components
import { AntecedentesFamiliaresComponent } from './components/antecedentes-familiares/antecedentes-familiares.component';
import { AntecedentesPersonalesComponent } from './components/antecedentes-personales/antecedentes-personales.component';
import { DiagnosticoPrincipalComponent } from './components/diagnostico-principal/diagnostico-principal.component';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { EpicrisisDockPopupComponent } from './dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { MedicacionComponent } from './components/medicacion/medicacion.component';
import { MedicalDischargeComponent } from './dialogs/medical-discharge/medical-discharge.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { EvolutionNoteDockPopupComponent } from './dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component';
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import { ChangeMainDiagnosisDockPopupComponent } from './dialogs/change-main-diagnosis-dock-popup/change-main-diagnosis-dock-popup.component';

@NgModule({
	declarations: [
		// routing
		EvaluacionClinicaDiagnosticosComponent,
		InternacionesHomeComponent,
		NewInternmentComponent,
		PatientBedRelocationComponent,
		PatientDischargeComponent,
		// components
		AlergiasComponent,
		AnamnesisDockPopupComponent,
		AntecedentesFamiliaresComponent,
		AntecedentesPersonalesComponent,
		DiagnosticoPrincipalComponent,
		DiagnosticosComponent,
		EpicrisisDockPopupComponent,
		InternacionesTableComponent,
		MedicacionComponent,
		MedicalDischargeComponent,
		VacunasComponent,
		EvolutionNoteDockPopupComponent,
		ChangeMainDiagnosisDockPopupComponent,
	],
	imports: [
		CommonModule,
		FormsModule,
		// routing
		InternacionesRoutingModule,
		// deps
		HistoriaClinicaModule,
		PresentationModule,
	]
})
export class InternacionesModule { }
