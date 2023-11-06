import { Injectable } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { ReferenceView } from '@turnos/components/reference-report/reference-report.component';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

const MAX_DAYS = 90;
@Injectable()
export class ReferenceReportFacadeService {

	private referencesReport = new BehaviorSubject<ReferenceReportDto[]>([]);
	readonly referencesReport$ = this.referencesReport.asObservable();

	dateRange: DateRange;
	dashboardView: ReferenceView;
	disabledDashboardActions = false;

	constructor(
		private readonly referenceReportService: ReferenceReportService,
	) {
		const today = new Date();
		this.dateRange = {
			start: dateMinusDays(today, MAX_DAYS),
			end: today
		}

		this.dashboardView = ReferenceView.RECEIVED;
	}

	updateReports() {
		this.disabledDashboardActions = true;
		this.dashboardView == ReferenceView.REQUESTED ? this.updateRequestedReferences() : this.updateReceivedReferences();
	}

	updateDashboardView(dashboardView: ReferenceView) {
		this.dashboardView = dashboardView;
		this.updateReports();
	}

	private updateReceivedReferences() {
		this.referencesReport.next(null);
		this.referenceReportService.getAllReceivedReferences(this.dateRange.start, this.dateRange.end).subscribe(reports => {
			this.referencesReport.next(reports);
			this.disabledDashboardActions = false;
		});
	}

	private updateRequestedReferences() {
		this.referencesReport.next(null);
		this.referenceReportService.getAllRequestedReferences(this.dateRange.start, this.dateRange.end).subscribe(reports => {
			this.referencesReport.next(reports);
			this.disabledDashboardActions = false;
		});
	}

}
