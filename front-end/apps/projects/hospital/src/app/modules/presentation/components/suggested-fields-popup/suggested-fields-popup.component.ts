import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Component, Inject } from '@angular/core';

@Component({
	selector: 'app-suggested-fields-popup',
	templateUrl: './suggested-fields-popup.component.html',
	styleUrls: ['./suggested-fields-popup.component.scss']
})
export class SuggestedFieldsPopupComponent {

	constructor(
		public dialogRef: MatDialogRef<SuggestedFieldsPopupComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			nonCompletedFields: string[],
			presentFields: string[]
		}
	) { }

}

