import { Component, Input } from '@angular/core';
import { SurgicalProcedures } from '@historia-clinica/utils/document-summary.model';

@Component({
	selector: 'app-surgical-procedures-summary',
	templateUrl: './surgical-procedures-summary.component.html',
	styleUrls: ['./surgical-procedures-summary.component.scss']
})
export class SurgicalProceduresSummaryComponent {

	@Input() surgicalProcedures: SurgicalProcedures;
	constructor() { }

}
