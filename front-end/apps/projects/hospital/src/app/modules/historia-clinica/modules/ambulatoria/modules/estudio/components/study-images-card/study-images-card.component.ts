import { Component, Input } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { IMAGES } from '../../constants/internment-studies';

@Component({
	selector: 'app-study-images-card',
	templateUrl: './study-images-card.component.html',
	styleUrls: ['./study-images-card.component.scss']
})
export class StudyImagesCardComponent {

	IMAGES = IMAGES;
	@Input() images: DiagnosticReportInfoDto[];
	@Input() patientId: number;

	constructor() { }

}
