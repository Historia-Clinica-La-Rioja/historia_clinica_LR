import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlergiasSummaryComponent } from './components/alergias-summary/alergias-summary.component';
import { AddAllergyComponent } from './dialogs/add-allergy/add-allergy.component';
import { CoreModule } from '@core/core.module';
import { ApiRestModule } from '@api-rest/api-rest.module';
import { PresentationModule } from '@presentation/presentation.module';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { AntecedentesFamiliaresSummaryComponent } from './components/antecedentes-familiares-summary/antecedentes-familiares-summary.component';
import { AntecedentesPersonalesSummaryComponent } from './components/antecedentes-personales-summary/antecedentes-personales-summary.component';
import { AntropometricosSummaryComponent } from './components/antropometricos-summary/antropometricos-summary.component';
import { DiagnosisSummaryComponent } from './components/diagnosis-summary/diagnosis-summary.component';
import { DocumentsSummaryComponent } from './components/documents-summary/documents-summary.component';
import { MainDiagnosisSummaryComponent } from './components/main-diagnosis-summary/main-diagnosis-summary.component';
import { MedicacionSummaryComponent } from './components/medicacion-summary/medicacion-summary.component';
import { SignosVitalesSummaryComponent } from './components/signos-vitales-summary/signos-vitales-summary.component';
import { VacunasSummaryComponent } from './components/vacunas-summary/vacunas-summary.component';
import { AddAnthropometricComponent } from './dialogs/add-anthropometric/add-anthropometric.component';
import { AddInmunizationComponent } from './dialogs/add-inmunization/add-inmunization.component';
import { AddVitalSignsComponent } from './dialogs/add-vital-signs/add-vital-signs.component';
import { ConceptsSearchDialogComponent } from './dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { EffectiveTimeComponent } from './components/effective-time/effective-time.component';
import { EffectiveTimeDialogComponent } from './dialogs/effective-time-dialog/effective-time-dialog.component';
import { RemoveDiagnosisComponent } from './dialogs/remove-diagnosis/remove-diagnosis.component';
import { ProbableDischargeDialogComponent } from './dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { DocumentSectionComponent } from '../presentation/components/document-section/document-section.component';
import { BedAssignmentComponent } from './dialogs/bed-assignment/bed-assignment.component';
import { InstitucionModule } from '../institucion/institucion.module';
import { InternacionAntecedentesPersonalesSummaryComponent } from './components/internacion-antecedentes-personales-summary/internacion-antecedentes-personales-summary.component';


@NgModule({
	declarations: [
		AddAllergyComponent,
		AddAnthropometricComponent,
		AddInmunizationComponent,
		AddVitalSignsComponent,
		AlergiasSummaryComponent,
		AntecedentesFamiliaresSummaryComponent,
		AntecedentesPersonalesSummaryComponent,
		AntropometricosSummaryComponent,
		ConceptsSearchComponent,
		ConceptsSearchDialogComponent,
		DiagnosisSummaryComponent,
		DocumentsSummaryComponent,
		EffectiveTimeComponent,
		EffectiveTimeDialogComponent,
		MainDiagnosisSummaryComponent,
		MedicacionSummaryComponent,
		RemoveDiagnosisComponent,
		SignosVitalesSummaryComponent,
		VacunasSummaryComponent,
		ProbableDischargeDialogComponent,
		DocumentSectionComponent,
		BedAssignmentComponent,
		InternacionAntecedentesPersonalesSummaryComponent
	],
	imports: [
		ApiRestModule,
		CommonModule,
		CoreModule,
		PresentationModule,
		InstitucionModule,
	],
	exports: [
		AlergiasSummaryComponent,
		AntecedentesFamiliaresSummaryComponent,
		AntecedentesPersonalesSummaryComponent,
		AntropometricosSummaryComponent,
		ConceptsSearchComponent,
		ConceptsSearchDialogComponent,
		DiagnosisSummaryComponent,
		DocumentsSummaryComponent,
		EffectiveTimeComponent,
		MainDiagnosisSummaryComponent,
		MedicacionSummaryComponent,
		SignosVitalesSummaryComponent,
		VacunasSummaryComponent,
		DocumentSectionComponent,
		InternacionAntecedentesPersonalesSummaryComponent
	]
})
export class HistoriaClinicaModule {
}
