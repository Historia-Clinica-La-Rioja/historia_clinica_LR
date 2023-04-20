import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IMAGES } from '../../constants/internment-studies';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';

@Component({
	selector: 'app-study-images-card',
	templateUrl: './study-images-card.component.html',
	styleUrls: ['./study-images-card.component.scss']
})
export class StudyImagesCardComponent {

	IMAGES = IMAGES;
	@Input() images: DiagnosticReportInfoDto[];
	@Input() patientId: number;
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	notifyUpdateToParent() {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
