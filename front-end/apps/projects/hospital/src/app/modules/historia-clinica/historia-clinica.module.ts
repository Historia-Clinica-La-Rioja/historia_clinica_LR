import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { InstitucionModule } from '../institucion/institucion.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// components
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { AntecedentesPersonalesSummaryComponent } from './components/antecedentes-personales-summary/antecedentes-personales-summary.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { DocumentsSummaryComponent } from './components/documents-summary/documents-summary.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
import { InternacionAntecedentesPersonalesSummaryComponent } from './components/internacion-antecedentes-personales-summary/internacion-antecedentes-personales-summary.component';
import { MedicacionSummaryComponent } from './components/medicacion-summary/medicacion-summary.component';
import { FactoresDeRiesgoSummaryComponent } from './components/factores-de-riesgo-summary/factores-de-riesgo-summary.component';
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
		// dialogs
		ConceptsSearchDialogComponent,
	]
})
export class HistoriaClinicaModule {
}
