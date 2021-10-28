import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-epidemiological-report',
	templateUrl: './epidemiological-report.component.html',
	styleUrls: ['./epidemiological-report.component.scss']
})
export class EpidemiologicalReportComponent implements OnInit {

	isDengueProblem: boolean;

	constructor(@Inject(MAT_DIALOG_DATA) public data, private dialogRef: MatDialogRef<EpidemiologicalReportComponent>) { }

	ngOnInit(): void {
		this.data ? this.isDengueProblem = true : this.isDengueProblem = false;
	}

	reportProblem(): void {
		this.dialogRef.close(true);
	}

	doNotReportProblem(): void {
		this.dialogRef.close(false);
	}

}
