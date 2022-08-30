import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { OTHER_PROCEDURES_AND_PRACTICES } from '../../constants/internment-studies';

@Component({
	selector: 'app-study-other-procedures-and-practices-card',
	templateUrl: './study-other-procedures-and-practices-card.component.html',
	styleUrls: ['./study-other-procedures-and-practices-card.component.scss']
})
export class StudyOtherProceduresAndPracticesCardComponent {

	OTHER_PROCEDURES_AND_PRACTICES = OTHER_PROCEDURES_AND_PRACTICES;
	@Input() otherProceduresAndPractices: DiagnosticReportInfoDto[];
	@Input() patientId: number;
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	notifyUpdateToParent() {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
