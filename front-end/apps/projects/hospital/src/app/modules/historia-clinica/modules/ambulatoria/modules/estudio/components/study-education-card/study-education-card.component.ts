import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EDUCATION } from '../../constants/internment-studies';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';

@Component({
	selector: 'app-study-education-card',
	templateUrl: './study-education-card.component.html',
	styleUrls: ['./study-education-card.component.scss']
})
export class StudyEducationCardComponent {

	EDUCATION = EDUCATION;
	@Input() educations: DiagnosticReportInfoDto[];
	@Input() patientId: number;
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	notifyUpdateToParent() {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
