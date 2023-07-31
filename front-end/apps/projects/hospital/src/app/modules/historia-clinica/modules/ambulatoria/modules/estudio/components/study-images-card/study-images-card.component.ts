import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IMAGES } from '../../constants/internment-studies';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { DiagnosticWithTypeReportInfoDto } from '../../model/ImageModel';


@Component({
	selector: 'app-study-images-card',
	templateUrl: './study-images-card.component.html',
	styleUrls: ['./study-images-card.component.scss']
})
export class StudyImagesCardComponent implements OnInit {


	IMAGES = IMAGES;
	@Input() images: DiagnosticReportInfoDto[] | DiagnosticWithTypeReportInfoDto[];
	@Input() patientId: number;
	@Input() categoryId: string;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<string>();

	constructor() { }

	ngOnInit(): void {
	}

	notifyUpdateToParent(): void {
		this.updateCurrentReportsEventEmitter.emit(this.categoryId);
	}

}
