import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ERole, ReferenceReportDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { PermissionsService } from '@core/services/permissions.service';
import { dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import differenceInDays from 'date-fns/differenceInDays';

const MAX_DAYS = 90;
const ADMINISTRATIVE = [ERole.ADMINISTRATIVO];

@Component({
	selector: 'app-reference-report',
	templateUrl: './reference-report.component.html',
	styleUrls: ['./reference-report.component.scss']
})
export class ReferenceReportComponent implements OnInit {

	view: ReferenceView;
	referenceView = ReferenceView;
	dateRange: DateRange;
	showValidation = false;
	disabled = false;
	readonly today = new Date();
	reports: ReferenceReportDto[] = [];

	constructor(
		private readonly referenceReportService: ReferenceReportService,
		private readonly permissionService: PermissionsService,
		private readonly changeDetectorRef: ChangeDetectorRef,
	) { }

	ngOnInit() {
		this.dateRange = {
			start: dateMinusDays(this.today, MAX_DAYS),
			end: this.today
		}

		this.permissionService.hasContextAssignments$(ADMINISTRATIVE).subscribe(hasRole => {
			this.view = !hasRole ? ReferenceView.REQUESTED : ReferenceView.RECEIVED;
		});
	}

	checkDays(dateRange: DateRange) {
		this.dateRange = dateRange;
		this.showValidation = false;
		if (differenceInDays(dateRange.end, dateRange.start) > MAX_DAYS) {
			this.showValidation = true;
			this.reports = [];
			return;
		}

		this.getDateRangeReports();
	}

	getReceived() {
		this.disabled = true;
		this.reports = [];
		this.view = ReferenceView.RECEIVED;
		this.referenceReportService.getAllReceivedReferences(this.dateRange.start, this.dateRange.end).subscribe( reports => {
			this.reports = reports;
			this.disabled = false;
		});
		this.changeDetectorRef.detectChanges();
	}

	getRequested() {
		this.disabled = true;
		this.reports = [];
		this.view = ReferenceView.REQUESTED;
		this.referenceReportService.getAllRequestedReferences(this.dateRange.start, this.dateRange.end).subscribe( reports => {
			this.reports = reports;
			this.disabled = false;
		});
		this.changeDetectorRef.detectChanges();
	}

	private getDateRangeReports() {
		if (this.view === ReferenceView.RECEIVED)
			this.getReceived();
		else
			this.getRequested();
	}

}

enum ReferenceView {
	REQUESTED,
	RECEIVED
}
