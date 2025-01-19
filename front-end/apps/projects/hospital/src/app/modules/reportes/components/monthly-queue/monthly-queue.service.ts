import { Injectable } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Observable, tap } from 'rxjs';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { InstitutionReportQueueComponent } from './monthly-queue.component';
import { ReportFilters } from '../../routes/home/home.component';
import { InstitutionReportQueryDto } from '@api-rest/api-model';
import { InstitutionReportType, ReportInstitutionQueueService } from '@api-rest/services/report-institution-queue.service';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';

@Injectable({
	providedIn: 'root'
})
export class MonthlyQueueService {
	private openDialog: MatDialogRef<any, void>;

	constructor(
		private reportInstitutionQueueService: ReportInstitutionQueueService,
		private readonly dialog: DialogService<InstitutionReportQueueComponent>,
	) {

	}

	showDialog(reportType: InstitutionReportType, reportFilters: ReportFilters, fileName: string): Observable<any> {
		const reportToQuery: InstitutionReportQueryDto = {
			startDate: toApiFormat(reportFilters.fromDate),
			endDate: toApiFormat(reportFilters.toDate),
			clinicalSpecialtyId: reportFilters.clinicalSpecialtyId,
			doctorId: reportFilters.doctorId,
			hierarchicalUnitTypeId: reportFilters.hierarchicalUnitTypeId,
			hierarchicalUnitId: reportFilters.hierarchicalUnitId,
		 	includeHierarchicalUnitDescendants: reportFilters.includeHierarchicalUnitDescendants,
			appointmentStateId: reportFilters.appointmentStateId,
			pageNumber: 0,
			pageSize: 3,
		};

		this.openDialog = this.dialog.open(InstitutionReportQueueComponent,
			{
				dialogWidth: DialogWidth.SMALL
			},{
				reportToQuery,
				reportType,
				fileName,
			});

		return this.openDialog.afterClosed().pipe(
			tap((toDownload) => {
				if (toDownload) {
					this.reportInstitutionQueueService.download(
						toDownload.id,
						fileName,
					);
				}
				this.openDialog = undefined;
			})
		);
	}
}

