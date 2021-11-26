import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-epidemiological-report',
	templateUrl: './epidemiological-report.component.html',
	styleUrls: ['./epidemiological-report.component.scss']
})
export class EpidemiologicalReportComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { problemName: string },
		private dialogRef: MatDialogRef<EpidemiologicalReportComponent>
	) { }

	reportProblem(): void {
		this.dialogRef.close(true);
	}

	doNotReportProblem(): void {
		this.dialogRef.close(false);
	}

	goBack(): void {
		this.dialogRef.close(null);
	}

}
