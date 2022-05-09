import { Component, Input } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { SURGICAL_PROCEDURE } from '../../constants/internment-studies';

@Component({
	selector: 'app-study-surgical-procedure-card',
	templateUrl: './study-surgical-procedure-card.component.html',
	styleUrls: ['./study-surgical-procedure-card.component.scss']
})
export class StudySurgicalProcedureCardComponent {

	SURGICAL_PROCEDURE = SURGICAL_PROCEDURE;
	@Input() surgicalProcedures: DiagnosticReportInfoDto[];
	@Input() patientId: number;

	constructor() { }

}
