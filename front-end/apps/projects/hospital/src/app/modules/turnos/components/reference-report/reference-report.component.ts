import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { ReferenceReportFacadeService } from '@turnos/services/reference-report-facade.service';
import differenceInDays from 'date-fns/differenceInDays';

const MAX_DAYS = 90;

@Component({
	selector: 'app-reference-report',
	templateUrl: './reference-report.component.html',
	styleUrls: ['./reference-report.component.scss']
})
export class ReferenceReportComponent implements OnInit, OnDestroy {

	referenceView = ReferenceView;
	showValidation = false;

	constructor(
		private readonly changeDetectorRef: ChangeDetectorRef,
		readonly referenceReportFacade: ReferenceReportFacadeService,
	) { }

	ngOnInit() {
		this.referenceReportFacade.updateReports();
	}

	ngOnDestroy(): void {
		this.referenceReportFacade.initializeFilters();
	}

	checkDays(dateRange: DateRange) {
		this.showValidation = false;
		if (differenceInDays(dateRange.end, dateRange.start) > MAX_DAYS) {
			this.showValidation = true;
			return;
		}

		this.referenceReportFacade.dateRange = dateRange;
		this.referenceReportFacade.updateReports();
		this.changeDetectorRef.detectChanges();
	}
}

export enum ReferenceView {
	REQUESTED = 1,
	RECEIVED
}
