import { Component, Input, OnInit } from '@angular/core';
import { SurgicalProcedures } from '@historia-clinica/utils/document-summary.model';

@Component({
	selector: 'app-surgical-procedures-summary',
	templateUrl: './surgical-procedures-summary.component.html',
	styleUrls: ['./surgical-procedures-summary.component.scss']
})
export class SurgicalProceduresSummaryComponent implements OnInit {

	@Input() surgicalProcedures: SurgicalProcedures;
	constructor() { }

	ngOnInit(): void {
	}

}
