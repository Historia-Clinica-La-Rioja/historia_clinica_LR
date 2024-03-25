import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PresentationModule } from '@presentation/presentation.module';

import { ObservationsFormComponent } from './components/observations-form/observations-form.component';
import { ObservationInputComponent } from './components/observation-input/observation-input.component';
import { DiagnosticReportLoincFormComponent } from './components/diagnostic-report-loinc-form/diagnostic-report-loinc-form.component';
import { ProcedureTemplateFormComponent } from './components/procedure-template-form/procedure-template-form.component';
import { ObservationInputUnitComponent } from './components/observation-input-unit/observation-input-unit.component';
import { LoincInputNumberComponent } from './components/loinc-input-number/loinc-input-number.component';

@NgModule({
	declarations: [
		ObservationsFormComponent,
		ObservationInputComponent,
		DiagnosticReportLoincFormComponent,
		ProcedureTemplateFormComponent,
  ObservationInputUnitComponent,
  LoincInputNumberComponent,
	],
	imports: [
		CommonModule,
		PresentationModule,
	],
	exports: [
		DiagnosticReportLoincFormComponent,
		ProcedureTemplateFormComponent,
	]
})
export class LoincFormModule { }
