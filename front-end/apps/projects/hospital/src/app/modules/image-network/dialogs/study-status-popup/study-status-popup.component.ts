import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-study-status-popup',
	templateUrl: './study-status-popup.component.html',
	styleUrls: ['./study-status-popup.component.scss']
})
export class StudyStatusPopupComponent implements OnInit {

	constructor(
		@Inject(MAT_DIALOG_DATA) public readonly data: {
			icon: string, //material icon name
			iconColor: string, //icon color - defined on clases on scss file
			popUpMessage?: string, //A complementary message - not translate route
			popUpMessageTranslate: string, //The translate route for the message
			acceptBtn?: boolean  //If you want the 'Accept' button to be visible
			iconCircle?: boolean  //If the material icon doesnt have a circle around and you need one
		},
		public dialogRef: MatDialogRef<StudyStatusPopupComponent>
	) {
	}

	ngOnInit(): void {
	}

	closeDialog() {
		this.dialogRef.close()
	}
}
