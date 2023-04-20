import { Component, EventEmitter, Input, Output } from '@angular/core';
import { HEMOTHERAPY } from '../../constants/internment-studies';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';

@Component({
	selector: 'app-study-hemotherapy-card',
	templateUrl: './study-hemotherapy-card.component.html',
	styleUrls: ['./study-hemotherapy-card.component.scss']
})
export class StudyHemotherapyCardComponent {

	HEMOTHERAPY = HEMOTHERAPY;
	@Input() hemotherapies: DiagnosticReportInfoDto[];
	@Input() patientId: number;
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	notifyUpdateToParent() {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
