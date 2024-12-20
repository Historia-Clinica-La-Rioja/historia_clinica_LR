import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PresentationModule } from '@presentation/presentation.module';

import { ObservationsFormComponent } from './components/observations-form/observations-form.component';
import { ObservationInputComponent } from './components/observation-input/observation-input.component';
import { DiagnosticReportLoincFormComponent } from './components/diagnostic-report-loinc-form/diagnostic-report-loinc-form.component';
import { ProcedureTemplateFormComponent } from './components/procedure-template-form/procedure-template-form.component';
import { LoincInputNumberComponent } from './components/loinc-input-number/loinc-input-number.component';
import { LoincInputSnomedComponent } from './components/loinc-input-snomed/loinc-input-snomed.component';
import { LoincInputTextBoxComponent } from './components/loinc-input-text-box/loinc-input-text-box.component';
import { LoincInputDropdownComponent } from './components/loinc-input-dropdown/loinc-input-dropdown.component';
//standalone
import { ConceptTypeaheadSearchComponent } from '../concept-typeahead-search/concept-typeahead-search.component';
import { ConceptTypeaheadSearchV2Component } from '@hsi-components/concept-typeahead-search-v2/concept-typeahead-search-v2.component';

@NgModule({
	declarations: [
		ObservationsFormComponent,
		ObservationInputComponent,
		DiagnosticReportLoincFormComponent,
		ProcedureTemplateFormComponent,
		LoincInputNumberComponent,
		LoincInputSnomedComponent,
  		LoincInputTextBoxComponent,
    	LoincInputDropdownComponent,
	],
	imports: [
		CommonModule,
		PresentationModule,
		//standalone
		ConceptTypeaheadSearchComponent,
		ConceptTypeaheadSearchV2Component,
	],
	exports: [
		DiagnosticReportLoincFormComponent,
		ProcedureTemplateFormComponent,
		ObservationsFormComponent,
		ObservationInputComponent,
	]
})
export class LoincFormModule { }
