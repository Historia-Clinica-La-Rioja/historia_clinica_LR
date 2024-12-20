import { Component, Input, ViewChild } from '@angular/core';
import { PageDto, ReferenceReportDto } from '@api-rest/api-model';
import { ReferenceReport } from '../reference-summary/reference-summary.component';
import { ReportCompleteDataPopupComponent } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { toReferenceReport } from '@access-management/utils/mapper.utils';
import { DashboardService } from '@access-management/services/dashboard.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

const PAGE_SIZE_OPTIONS = [5, 10, 25];

@Component({
	selector: 'app-reference-dashboard',
	templateUrl: './reference-dashboard.component.html',
	styleUrls: ['./reference-dashboard.component.scss']
})
export class ReferenceDashboardComponent {

	referenceReports: ReferenceReport[] = [];
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	totalElementsAmount: number;
	hasToResetToFirstPage = false;

	@ViewChild('paginator') paginator: MatPaginator;

	@Input()
	set references(list: PageDto<ReferenceReportDto>) {
		this.referenceReports = this.mapToReferencesReport(list);
		this.updatePaginator(list?.totalElementsAmount);
	};

	constructor(
		private readonly dialog: MatDialog,
		readonly dashboardService: DashboardService,
	) { }

	openRequest(referenceId: number) {
		const dialogRef = this.dialog.open(ReportCompleteDataPopupComponent, {
			data: {
				referenceId
			},
			autoFocus: false,
			disableClose: true,
			width: '60%',
		});
		dialogRef.afterClosed().subscribe(() => {
			this.dashboardService.updateReports();
		});
	}

	updatePage(pageInfo: PageEvent) {
		this.hasToResetToFirstPage = false;
		this.dashboardService.updatePaginator(pageInfo);
	}

	private mapToReferencesReport(list): ReferenceReport[] {
		return (list?.content) ? list?.content.map(report => toReferenceReport(report)) : [];
	}

	private updatePaginator(totalElementsAmount: number) {
		if (this.hasToResetToFirstPage && this.paginator)
			this.paginator.pageIndex = 0;
		this.totalElementsAmount = totalElementsAmount;
	}

}
