import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
	Observable,
	Subscription,
	filter,
	map,
	switchMap,
	tap,
	timer,
} from 'rxjs';
import {
	AddInstitutionReportToQueueDto,
	InstitutionReportQueryDto,
	InstitutionReportQueuedDto,
} from '@api-rest/api-model';

import { dateDtoFromISO } from '@api-rest/mapper/date-dto.parser';
import {
	InstitutionReportType,
	ReportInstitutionQueueService,
} from '@api-rest/services/report-institution-queue.service';
import { ReportStatus, initStatus, refreshStatus } from './report-queue.utils';

@Component({
	selector: 'app-monthly-queue',
	templateUrl: './monthly-queue.component.html',
	styleUrls: ['./monthly-queue.component.scss']
})
export class InstitutionReportQueueComponent implements OnInit, OnDestroy {
	dialogTitle = '';
	showStatus: ReportStatus = {stage: 'EMPTY'};

	private timerSubscription: Subscription;
	private addToQueue: () => void;
	private fetchQueueStatus$: (mapper: (list: InstitutionReportQueuedDto[]) => ReportStatus) => Observable<ReportStatus>;

	constructor(
		public dialogRef: MatDialogRef<InstitutionReportQueueComponent>,
		@Inject(MAT_DIALOG_DATA) private data: {
			reportToQuery: InstitutionReportQueryDto,
			reportType: InstitutionReportType,
			fileName: string,
		},
		reportInstitutionQueueService: ReportInstitutionQueueService,
	) {
		this.dialogTitle = data.fileName;
		const reportToAdd: AddInstitutionReportToQueueDto = {
			...data.reportToQuery,
			startDate: dateDtoFromISO(data.reportToQuery.startDate),
			endDate: dateDtoFromISO(data.reportToQuery.endDate),
		};

		this.fetchQueueStatus$ = (statusMapper): Observable<ReportStatus> => {
			return reportInstitutionQueueService.list(this.data.reportType, this.data.reportToQuery)
				.pipe(
					map(statusMapper),
					tap(showStatus => this.showStatus = showStatus)
				);
		}

		this.addToQueue = (): void => {
			reportInstitutionQueueService
				.addToQueue(this.data.reportType, reportToAdd)
				.pipe(
					switchMap(_ => this.fetchQueueStatus$(refreshStatus)),
				)
				.subscribe();
		};
	}

	ngOnInit(): void {
		this.fetchQueueStatus$(initStatus)
			.pipe(filter(_ => this.showStatus.stage === 'EMPTY'))
			// si está vacío agrega a la cola
			.subscribe(_ => this.addToQueue());

		this.timerSubscription = timer(0, 5000)
			.pipe(
				filter(_ => this.showStatus.stage === 'RELOAD'),
				// si hay que recargar, recarga
				switchMap(_ => this.fetchQueueStatus$(refreshStatus)),
			)
			.subscribe();
	}

	ngOnDestroy() {
		// Desuscribirse para evitar pérdidas de memoria
		if (this.timerSubscription) {
		  this.timerSubscription.unsubscribe();
		}
	}

	queueReport(): void {
		this.addToQueue();
	}

	get downloadButtonText(): string | undefined {
		if (!this.showStatus.toDownload) return undefined;
		if (this.showStatus.stage === 'ASK') return 'Ver último reporte';
		if (this.showStatus.stage === 'DOWNLOAD') return 'Ver reporte generado';
		if (this.showStatus.stage === 'RELOAD') return 'Ver reporte anterior';
		return undefined;
	}
}

