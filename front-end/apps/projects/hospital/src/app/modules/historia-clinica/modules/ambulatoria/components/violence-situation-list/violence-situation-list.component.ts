import { Component, Input } from '@angular/core';
import { EViolenceEvaluationRiskLevel, ViolenceReportSituationDto } from '@api-rest/api-model';
import { DateFormat } from '@core/utils/date.utils';

@Component({
	selector: 'app-violence-situation-list',
	templateUrl: './violence-situation-list.component.html',
	styleUrls: ['./violence-situation-list.component.scss']
})
export class ViolenceSituationListComponent {

	@Input() violenceSituations: ViolenceReportSituationDto[] = [];

	DateFormat = DateFormat;

	LOW = EViolenceEvaluationRiskLevel.LOW;
	MEDIUM = EViolenceEvaluationRiskLevel.MEDIUM;
	HIGH = EViolenceEvaluationRiskLevel.HIGH;
}
