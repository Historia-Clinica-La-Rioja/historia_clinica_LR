import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
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

import { AddAllergyComponent } from './dialogs/add-allergy/add-allergy.component';
import { AddAnthropometricComponent } from './dialogs/add-anthropometric/add-anthropometric.component';
import { AddInmunizationComponent } from './dialogs/add-inmunization/add-inmunization.component';
import { AddVitalSignsComponent } from './dialogs/add-vital-signs/add-vital-signs.component';
import { ConceptsSearchDialogComponent } from './dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { EffectiveTimeDialogComponent } from './dialogs/effective-time-dialog/effective-time-dialog.component';
import { RemoveDiagnosisComponent } from './dialogs/remove-diagnosis/remove-diagnosis.component';

import { AlergiasComponent } from './components/alergias/alergias.component';
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { AnamnesisFormComponent } from './components/anamnesis-form/anamnesis-form.component';
import { AntecedentesFamiliaresComponent } from './components/antecedentes-familiares/antecedentes-familiares.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { AntecedentesPersonalesComponent } from './components/antecedentes-personales/antecedentes-personales.component';
import { AntecedentesPersonalesSummaryComponent } from './components/antecedentes-personales-summary/antecedentes-personales-summary.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { DiagnosisSummaryComponent } from './components/diagnosis-summary/diagnosis-summary.component';
import { DiagnosticoPrincipalComponent } from './components/diagnostico-principal/diagnostico-principal.component';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';
import { DocumentsSummaryComponent } from './components/documents-summary/documents-summary.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
import { EpicrisisFormComponent } from './components/epicrisis-form/epicrisis-form.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { MainDiagnosisSummaryComponent } from './components/main-diagnosis-summary/main-diagnosis-summary.component';
import { MedicacionComponent } from './components/medicacion/medicacion.component';
import { MedicacionSummaryComponent } from './components/medicacion-summary/medicacion-summary.component';
import { MedicalDischargeComponent } from './components/medical-discharge/medical-discharge.component';
import { NotaEvolucionFormComponent } from './components/nota-evolucion-form/nota-evolucion-form.component';
import { SignosVitalesSummaryComponent } from './components/signos-vitales-summary/signos-vitales-summary.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { VacunasSummaryComponent } from './components/vacunas-summary/vacunas-summary.component';

import { ApiRestModule } from '../api-rest/api-rest.module';

@NgModule({
	declarations: [
		AddAllergyComponent,
		AddAnthropometricComponent,
		AddInmunizationComponent,
		AddVitalSignsComponent,
		AlergiasComponent,
		AlergiasSummaryComponent,
		AnamnesisComponent,
		AnamnesisFormComponent,
		AntecedentesFamiliaresComponent,
		AntecedentesFamiliaresSummaryComponent,
		AntecedentesPersonalesComponent,
		AntecedentesPersonalesSummaryComponent,
		AntropometricosSummaryComponent,
		CambiarDiagnosticoPrincipalComponent,
		ConceptsSearchComponent,
		ConceptsSearchDialogComponent,
		DiagnosisSummaryComponent,
		DiagnosticoPrincipalComponent,
		DiagnosticosComponent,
		DocumentsSummaryComponent,
		EffectiveTimeComponent,
		EffectiveTimeDialogComponent,
		EpicrisisComponent,
		EpicrisisFormComponent,
		EvaluacionClinicaDiagnosticosComponent,
		InternacionesHomeComponent,
		InternacionesTableComponent,
		InternacionPacienteComponent,
		MainDiagnosisSummaryComponent,
		MedicacionComponent,
		MedicacionSummaryComponent,
		MedicalDischargeComponent,
		NewInternmentComponent,
		NotaEvolucionComponent,
		NotaEvolucionFormComponent,
		PatientDischargeComponent,
		RemoveDiagnosisComponent,
		SignosVitalesSummaryComponent,
		VacunasComponent,
		VacunasSummaryComponent,
	],
	imports: [

		CoreModule,
		ApiRestModule,
		CommonModule,
		FlexLayoutModule,
		FormsModule,
		PresentationModule,
		InternacionesRoutingModule,
	]
})
export class InternacionesModule { }
