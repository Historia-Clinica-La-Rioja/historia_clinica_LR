import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-study-status-popup',
	templateUrl: './study-status-popup.component.html',
	styleUrls: ['./study-status-popup.component.scss']
})
export class StudyStatusPopupComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public readonly data: {
			icon: string,
			iconColor: string,
			popUpMessage?: string,
			popUpMessageTranslate: string,
			acceptBtn?: boolean
			iconCircle?: boolean
		},
		public dialogRef: MatDialogRef<StudyStatusPopupComponent>
	) {
	}
}
