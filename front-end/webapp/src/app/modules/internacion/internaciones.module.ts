import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InternacionesRoutingModule } from './internaciones-routing.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '../presentation/presentation.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AnamnesisFormComponent } from './components/anamnesis-form/anamnesis-form.component';
import { InternacionesHomeComponent } from './routes/home/internaciones-home.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { InternacionPacienteComponent } from './routes/internacion-paciente/internacion-paciente.component';
import { AnamnesisComponent } from './routes/anamnesis/anamnesis.component';
import { ConceptsSearchDialogComponent } from './dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { FormsModule } from '@angular/forms';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { AlergiasComponent } from './components/alergias/alergias.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { MedicacionComponent } from './components/medicacion/medicacion.component';
import { AntecedentesFamiliaresComponent } from './components/antecedentes-familiares/antecedentes-familiares.component';
import { AntecedentesPersonalesComponent } from './components/antecedentes-personales/antecedentes-personales.component';
import { DiagnosisSummaryComponent } from './components/diagnosis-summary/diagnosis-summary.component';
import { SignosVitalesSummaryComponent } from './components/signos-vitales-summary/signos-vitales-summary.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { NewInternmentComponent } from './routes/new-internment/new-internment.component';
import { EpicrisisComponent } from './routes/epicrisis/epicrisis.component';
import { EpicrisisFormComponent } from './components/epicrisis-form/epicrisis-form.component';
import { NotaEvolucionComponent } from './routes/nota-evolucion/nota-evolucion.component';
import { NotaEvolucionFormComponent } from './components/nota-evolucion-form/nota-evolucion-form.component';
import { DiagnosticoPrincipalComponent } from './components/diagnostico-principal/diagnostico-principal.component';
import { MainDiagnosisSummaryComponent } from './components/main-diagnosis-summary/main-diagnosis-summary.component';
import { RemoveDiagnosisComponent } from './dialogs/remove-diagnosis/remove-diagnosis.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
import { EffectiveTimeDialogComponent } from './dialogs/effective-time-dialog/effective-time-dialog.component';
import { PatientDischargeComponent } from './routes/patient-discharge/patient-discharge.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { AntecedentesPersonalesSummaryComponent } from './components/antecedentes-personales-summary/antecedentes-personales-summary.component';
import { MedicacionSummaryComponent } from './components/medicacion-summary/medicacion-summary.component';
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { VacunasSummaryComponent } from './components/vacunas-summary/vacunas-summary.component';
import { EvaluacionClinicaDiagnosticosComponent } from './routes/evaluacion-clinica-diagnosticos/evaluacion-clinica-diagnosticos.component';

@NgModule({
	declarations: [
		AnamnesisFormComponent,
		InternacionesTableComponent,
		InternacionesHomeComponent,
		InternacionPacienteComponent,
		AnamnesisComponent,
		AntecedentesPersonalesComponent,
		ConceptsSearchDialogComponent,
		ConceptsSearchComponent,
		DiagnosticosComponent,
		DiagnosticoPrincipalComponent,
		AlergiasComponent,
		VacunasComponent,
		MedicacionComponent,
		AntecedentesFamiliaresComponent,
		DiagnosisSummaryComponent,
		SignosVitalesSummaryComponent,
		AntropometricosSummaryComponent,
		NewInternmentComponent,
		EpicrisisComponent,
		EpicrisisFormComponent,
		NotaEvolucionComponent,
		NotaEvolucionFormComponent,
		MainDiagnosisSummaryComponent,
		RemoveDiagnosisComponent,
		EffectiveTimeComponent,
		EffectiveTimeDialogComponent,
		PatientDischargeComponent,
		AntecedentesFamiliaresSummaryComponent,
		AntecedentesPersonalesSummaryComponent,
		MedicacionSummaryComponent,
		AlergiasSummaryComponent,
		VacunasSummaryComponent,
		EvaluacionClinicaDiagnosticosComponent,
	],
	imports: [
		CoreModule,
		CommonModule,
		FlexLayoutModule,
		FormsModule,
		PresentationModule,
		InternacionesRoutingModule,
	]
})
export class InternacionesModule { }
