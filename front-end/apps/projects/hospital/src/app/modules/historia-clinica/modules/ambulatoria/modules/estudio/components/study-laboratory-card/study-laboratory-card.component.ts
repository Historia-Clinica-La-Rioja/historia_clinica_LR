import { Component, Input } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { LABORATORY } from '../../constants/internment-studies';

@Component({
	selector: 'app-study-laboratory-card',
	templateUrl: './study-laboratory-card.component.html',
	styleUrls: ['./study-laboratory-card.component.scss']
})
export class StudyLaboratoryCardComponent {

	LABORATORY = LABORATORY;
	@Input() laboratories: DiagnosticReportInfoDto[];
	@Input() patientId: number;

	constructor() { }

}
