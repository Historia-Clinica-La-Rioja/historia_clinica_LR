import { Injectable } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ERole, PageDto, ReferenceReportDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { DateFormat, dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { DashboardFilters, DashboardView } from '@turnos/components/report-filters/report-filters.component';
import format from 'date-fns/format';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { PermissionsService } from '@core/services/permissions.service';

const MAX_DAYS = 90;
const MIN_SIZE = 5;
const INITIAL_PAGE = 0;
@Injectable()
export class ReferenceReportFacadeService {

	private isAdministrative = false;
	private referencesReport = new BehaviorSubject<PageDto<ReferenceReportDto>>(null);
	readonly referencesReport$ = this.referencesReport.asObservable();

	dateRange: DateRange;
	dashboardView = DashboardView.RECEIVED;
	disabledDashboardActions = false;
	dashboardFilters: DashboardFilters;
	pageSize = MIN_SIZE;
	pageNumber = INITIAL_PAGE;

	constructor(
		private readonly referenceReportService: ReferenceReportService,
		private readonly permissionsService: PermissionsService,
	) {
		this.initializeFilters();
		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO]).subscribe(hasRole => this.isAdministrative = hasRole);
	}

	updateReports() {
		this.disabledDashboardActions = true;
		this.setRange();
		this.dashboardView == DashboardView.RECEIVED && this.isAdministrative ? this.updateReceivedReferences() : this.updateRequestedReferences()
	}

	updateDashboardView(dashboardView: DashboardView) {
		this.dashboardView = dashboardView;
		this.updateReports();
	}

	initializeFilters() {
		const today = new Date();
		this.dateRange = {
			start: dateMinusDays(today, MAX_DAYS),
			end: today
		}

		this.dashboardView = DashboardView.RECEIVED;
	}

	updatePaginator(pageInfo: PageEvent) {
		this.pageNumber = pageInfo.pageIndex;
		this.pageSize = pageInfo.pageSize;
		this.updateReports();
	}

	private updateReceivedReferences() {
		this.referenceReportService.getAllReceivedReferences(this.dashboardFilters, this.pageSize, this.pageNumber).subscribe(reports => {
			this.referencesReport.next(reports);
			this.disabledDashboardActions = false;
		});
	}

	private updateRequestedReferences() {
		this.referenceReportService.getAllRequestedReferences(this.dashboardFilters, this.pageSize, this.pageNumber).subscribe(reports => {
			this.referencesReport.next(reports);
			this.disabledDashboardActions = false;
		});
	}

	private setRange() {
		this.dashboardFilters = {
			...this.dashboardFilters,
			from: format(this.dateRange.start, DateFormat.API_DATE),
			to: format(this.dateRange.end, DateFormat.API_DATE)
		}
	}

}
