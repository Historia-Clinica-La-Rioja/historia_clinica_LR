import { Component, OnInit } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import differenceInDays from 'date-fns/differenceInDays';
import { Observable } from 'rxjs';

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
	received$: Observable<ReferenceReportDto[]>;

	constructor(
		private readonly referenceReportService: ReferenceReportService
	) { }

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
			delete this.received$;
			return;
		}

		this.getReceived();
	}

	private getReceived() {
		this.received$ = this.referenceReportService.getAllReceivedReferences(this.dateRange.start, this.dateRange.end);
	}

}

enum ReferenceView {
	request,
	received
}
