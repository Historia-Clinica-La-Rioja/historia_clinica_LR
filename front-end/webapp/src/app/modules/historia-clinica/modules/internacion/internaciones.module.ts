import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

import { AnamnesisComponent } from './routes/anamnesis/anamnesis.component';
import { CambiarDiagnosticoPrincipalComponent } from './routes/cambiar-diagnostico-principal/cambiar-diagnostico-principal.component';
import { EpicrisisComponent } from './routes/epicrisis/epicrisis.component';
import { EvaluacionClinicaDiagnosticosComponent } from './routes/evaluacion-clinica-diagnosticos/evaluacion-clinica-diagnosticos.component';
import { InternacionesHomeComponent } from './routes/home/internaciones-home.component';
import { InternacionesRoutingModule } from './internaciones-routing.module';
import { InternacionPacienteComponent } from './routes/internacion-paciente/internacion-paciente.component';
import { NewInternmentComponent } from './routes/new-internment/new-internment.component';
import { NotaEvolucionComponent } from './routes/nota-evolucion/nota-evolucion.component';
import { PatientDischargeComponent } from './routes/patient-discharge/patient-discharge.component';

import { AlergiasComponent } from './components/alergias/alergias.component';
import { AnamnesisFormComponent } from './components/anamnesis-form/anamnesis-form.component';
import { AntecedentesFamiliaresComponent } from './components/antecedentes-familiares/antecedentes-familiares.component';
import { AntecedentesPersonalesComponent } from './components/antecedentes-personales/antecedentes-personales.component';
import { DiagnosticoPrincipalComponent } from './components/diagnostico-principal/diagnostico-principal.component';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { EpicrisisFormComponent } from './components/epicrisis-form/epicrisis-form.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { MedicacionComponent } from './components/medicacion/medicacion.component';
import { MedicalDischargeComponent } from './components/medical-discharge/medical-discharge.component';
import { NotaEvolucionFormComponent } from './components/nota-evolucion-form/nota-evolucion-form.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';

import { ApiRestModule } from '@api-rest/api-rest.module';
import { HistoriaClinicaModule } from '../../historia-clinica.module';

@NgModule({
	declarations: [
		AlergiasComponent,
		AnamnesisComponent,
		AnamnesisFormComponent,
		AntecedentesFamiliaresComponent,
		AntecedentesPersonalesComponent,
		CambiarDiagnosticoPrincipalComponent,
		DiagnosticoPrincipalComponent,
		DiagnosticosComponent,
		EpicrisisComponent,
		EpicrisisFormComponent,
		EvaluacionClinicaDiagnosticosComponent,
		InternacionesHomeComponent,
		InternacionesTableComponent,
		InternacionPacienteComponent,
		MedicacionComponent,
		MedicalDischargeComponent,
		NewInternmentComponent,
		NotaEvolucionComponent,
		NotaEvolucionFormComponent,
		PatientDischargeComponent,
		VacunasComponent,
	],
	imports: [

		CoreModule,
		ApiRestModule,
		CommonModule,
		FormsModule,
		PresentationModule,
		InternacionesRoutingModule,
		HistoriaClinicaModule
	]
})
export class InternacionesModule { }
