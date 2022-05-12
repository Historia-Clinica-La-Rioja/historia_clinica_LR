import { Component, EventEmitter, Input, Output } from '@angular/core';
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
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	notifyUpdateToParent() {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
