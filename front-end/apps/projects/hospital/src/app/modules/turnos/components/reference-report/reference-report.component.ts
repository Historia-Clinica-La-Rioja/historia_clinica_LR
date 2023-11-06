import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ERole, ReferenceReportDto } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { ReferenceReportFacadeService } from '@turnos/services/reference-report-facade.service';
import differenceInDays from 'date-fns/differenceInDays';
import { Observable } from 'rxjs';

const MAX_DAYS = 90;
const ADMINISTRATIVE = [ERole.ADMINISTRATIVO];

@Component({
	selector: 'app-reference-report',
	templateUrl: './reference-report.component.html',
	styleUrls: ['./reference-report.component.scss']
})
export class ReferenceReportComponent implements OnInit {

	referenceView = ReferenceView;
	showValidation = false;
	reports$: Observable<ReferenceReportDto[]>;

	constructor(
		private readonly permissionService: PermissionsService,
		private readonly changeDetectorRef: ChangeDetectorRef,
		readonly referenceReportFacade: ReferenceReportFacadeService,
	) { }

	ngOnInit() {

		this.permissionService.hasContextAssignments$(ADMINISTRATIVE).subscribe(hasRole => {
			this.referenceReportFacade.dashboardView = !hasRole ? ReferenceView.REQUESTED : ReferenceView.RECEIVED;
		});

		this.referenceReportFacade.updateReports();
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
