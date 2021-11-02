import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-previous-data',
  templateUrl: './previous-data.component.html',
  styleUrls: ['./previous-data.component.scss']
})
export class PreviousDataComponent implements OnInit {

	constructor(@Inject(MAT_DIALOG_DATA) public data, private dialogRef: MatDialogRef<PreviousDataComponent>) { }

	ngOnInit(): void {

	}

	discardData(): void {
		this.dialogRef.close(true);
	}

	addData(): void {
		this.dialogRef.close(false);
	}

	goBack(): void {
		this.dialogRef.close(null);
	}
}
