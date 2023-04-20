import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LABORATORY } from '../../constants/internment-studies';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';

@Component({
	selector: 'app-study-laboratory-card',
	templateUrl: './study-laboratory-card.component.html',
	styleUrls: ['./study-laboratory-card.component.scss']
})
export class StudyLaboratoryCardComponent {

	LABORATORY = LABORATORY;
	@Input() laboratories: DiagnosticReportInfoDto[];
	@Input() patientId: number;
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	notifyUpdateToParent() {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
