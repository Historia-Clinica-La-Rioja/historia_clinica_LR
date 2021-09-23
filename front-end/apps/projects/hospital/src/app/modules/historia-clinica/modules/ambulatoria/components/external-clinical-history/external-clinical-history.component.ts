import { Component, Input, OnInit } from '@angular/core';
import { ExternalClinicalHistoryDto } from '@api-rest/api-model';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';

@Component({
	selector: 'app-external-clinical-history',
	templateUrl: './external-clinical-history.component.html',
	styleUrls: ['./external-clinical-history.component.scss']
})
export class ExternalClinicalHistoryComponent implements OnInit {

	@Input() public readonly externalClinicalHistory: ExternalClinicalHistoryDto;
	public DATE_TO_VIEW: string;

	constructor() { }

	ngOnInit(): void {
		this.DATE_TO_VIEW = momentFormat(momentParseDate(this.externalClinicalHistory.consultationDate), DateFormat.VIEW_DATE);
	}

}
