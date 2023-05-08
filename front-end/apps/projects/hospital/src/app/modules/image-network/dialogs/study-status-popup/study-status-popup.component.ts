import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-study-status-popup',
	templateUrl: './study-status-popup.component.html',
	styleUrls: ['./study-status-popup.component.scss']
})
export class StudyStatusPopupComponent implements OnInit {

	constructor(
		@Inject(MAT_DIALOG_DATA) public readonly data: { status: boolean },
		public dialogRef: MatDialogRef<StudyStatusPopupComponent>
	) {
	}

	ngOnInit(): void {
	}

	closeDialog() {
		this.dialogRef.close()
	}
}
