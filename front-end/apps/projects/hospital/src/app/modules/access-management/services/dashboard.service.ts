import { Injectable } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ERole, PageDto, ReferenceReportDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { DateFormat, dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { DashboardView, DashboardFilters } from '@shared-appointment-access-management/components/reference-dashboard-filters/reference-dashboard-filters.component';
import format from 'date-fns/format';
import { PermissionsService } from '@core/services/permissions.service';
import { ContextService } from '@core/services/context.service';
import { NO_INSTITUTION } from '../../home/home.component';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { BehaviorSubject, Observable, take } from 'rxjs';

const MAX_DAYS = 90;
const MIN_SIZE = 5;
const INITIAL_PAGE = 0;
@Injectable({
	providedIn: 'root'
})
export class DashboardService {

	private hasRolePermissionToReceive = false;
	private references = new BehaviorSubject<PageDto<ReferenceReportDto>>(null);
	readonly references$ = this.references.asObservable();

	dateRange: DateRange;
	dashboardView = DashboardView.RECEIVED;
	disabledDashboardActions = false;
	dashboardFilters: DashboardFilters;
	pageSize = MIN_SIZE;
	pageNumber = INITIAL_PAGE;

	constructor(
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly permissionsService: PermissionsService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly contextService: ContextService,
	) { }

	initializeService() {
		this.initializeFilters();
		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO,ERole.ABORDAJE_VIOLENCIAS]).pipe(take(1)).subscribe(hasRole => {
			this.hasRolePermissionToReceive = hasRole
			this.updateReports();
		});
	}

	updateReports() {
		this.disabledDashboardActions = true;
		this.setRange();
		this.updateReportByAccess();
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
		this.dashboardFilters = {} as DashboardFilters;
		this.pageSize = MIN_SIZE;
		this.pageNumber = INITIAL_PAGE;
	}

	updatePaginator(pageInfo: PageEvent) {
		this.pageNumber = pageInfo.pageIndex;
		this.pageSize = pageInfo.pageSize;
		this.updateReports();
	}

	private updateReportByAccess() {
		this.contextService.institutionId === NO_INSTITUTION ? this.updateInsitutionalNetworkReferences() : this.updateInsitutionalReferences();
	}

	private updateInsitutionalNetworkReferences() {
		this.callReferenceReportService(this.institutionalNetworkReferenceReportService, METHOD_NAMES.MANAGER);
	}

	private updateInsitutionalReferences() {
		const methodName = this.hasRolePermissionToReceive && this.dashboardView == DashboardView.RECEIVED ? METHOD_NAMES.RECEIVED : METHOD_NAMES.REQUESTED;
		this.callReferenceReportService(this.institutionalReferenceReportService, methodName);
	}

	private callReferenceReportService(service: Service, method: string) {
		const referenceReports$ = service[method](this.dashboardFilters, this.pageSize, this.pageNumber);
		this.subscribeAndEmit(referenceReports$);
	}

	private subscribeAndEmit(obs: Observable<PageDto<ReferenceReportDto>>) {
		obs.subscribe(reports => {
			this.references.next(reports);
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

enum METHOD_NAMES {
	RECEIVED = 'getAllReceivedReferences',
	REQUESTED = 'getAllRequestedReferences',
	MANAGER = 'getReferencesByManagerRole',
}

type Service = InstitutionalNetworkReferenceReportService | InstitutionalReferenceReportService;