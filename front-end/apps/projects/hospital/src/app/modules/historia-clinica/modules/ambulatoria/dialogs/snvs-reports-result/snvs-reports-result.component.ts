import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnvsEventDto, SnvsReportDto, SnvsToReportDto } from '@api-rest/api-model';
import { SnvsService } from '@api-rest/services/snvs.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const MIN_SPINNER_DURATION = 3000;

@Component({
	selector: 'app-snvs-reports-result',
	templateUrl: './snvs-reports-result.component.html',
	styleUrls: ['./snvs-reports-result.component.scss']
})
export class SnvsReportsResultComponent implements OnInit {

	public loading = true;
	public reportsResult: SnvsReportDto[];

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { toReportList: SnvsToReportDto[], patientId: number, snvsEvent: SnvsEventDto[] },
		private dialogRef: MatDialogRef<SnvsReportsResultComponent>,
		private readonly snvsService: SnvsService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		const START_TIME = performance.now();
		this.snvsService.reportSnvs(this.data.patientId, this.data.toReportList).subscribe(
			(reportsResult: SnvsReportDto[]) => {
				this.reportsResult = reportsResult;
				this.stopLoadingConsideringTime(START_TIME, () => this.loading = false);
			},
			_ => {
				this.stopLoadingConsideringTime(START_TIME, () => this.closeWithError());
			}
		);
	}

	public getGroupEventDescription(groupEventId: number, eventId: number): string {
		let result = this.data.snvsEvent?.find((snvsEvent: SnvsEventDto) => (snvsEvent.eventId === eventId && snvsEvent.groupEventId === groupEventId));
		return result ? result.description : "";
	}

	public isTheLast(index: number): boolean {
		return (index + 1 === this.reportsResult?.length);
	}

	public successfullyReported(reportResult: SnvsReportDto): boolean {
		return (reportResult.sisaRegisteredId && reportResult.sisaRegisteredId !== 0)
	}

	private stopLoadingConsideringTime(startTime: number, callbackFn: Function): void {
		const TIME_UNTIL_NOW = performance.now() - startTime;
		if (TIME_UNTIL_NOW >= MIN_SPINNER_DURATION)
			callbackFn();
		else {
			const TIME_TO_MIN_DURATION = MIN_SPINNER_DURATION - TIME_UNTIL_NOW;
			setTimeout(callbackFn, TIME_TO_MIN_DURATION);
		}
	}

	private closeWithError(): void {
		this.dialogRef.close();
		this.snackBarService.showError('ambulatoria.paciente.problemas.snvs-reports-result.ERROR_FAIL_TO_REPORT');
	}

}
