import { Component, OnInit } from '@angular/core';
import { dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import differenceInDays from 'date-fns/differenceInDays';

const MAX_DAYS = 90;

@Component({
	selector: 'app-reference-report',
	templateUrl: './reference-report.component.html',
	styleUrls: ['./reference-report.component.scss']
})
export class ReferenceReportComponent implements OnInit {

	view: ReferenceView = ReferenceView.received;
	referenceView = ReferenceView;
	dateRange: DateRange;
	showValidation = false;
	readonly today = new Date();

	constructor() { }

	ngOnInit() {
		this.dateRange = {
			start: dateMinusDays(this.today, MAX_DAYS),
			end: this.today
		}
	}

	checkDays(dateRange: DateRange) {
		this.dateRange = dateRange;
		this.showValidation = false;
		if (differenceInDays(dateRange.end, dateRange.start) > MAX_DAYS) {
			this.showValidation = true;
			return;
		}
	}

}

enum ReferenceView {
	request,
	received
}
