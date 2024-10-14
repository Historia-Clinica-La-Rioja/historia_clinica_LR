import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';

@Component({
	selector: 'app-indexing-image-status',
	templateUrl: './indexing-image-status.component.html',
	styleUrls: ['./indexing-image-status.component.scss']
})
export class IndexingImageStatusComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public readonly data: {
			icon: string,
			iconColor: string,
			popUpMessage: string,
			popUpTitle: string,
			acceptBtn?: boolean
		},
		public dialogRef: MatDialogRef<StudyStatusPopupComponent>
	) { }
}
