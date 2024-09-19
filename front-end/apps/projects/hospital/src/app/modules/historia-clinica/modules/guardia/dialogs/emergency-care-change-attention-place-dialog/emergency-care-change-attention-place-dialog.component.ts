import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-emergency-care-change-attention-place-dialog',
	templateUrl: './emergency-care-change-attention-place-dialog.component.html',
	styleUrls: ['./emergency-care-change-attention-place-dialog.component.scss']
})
export class EmergencyCareChangeAttentionPlaceDialogComponent {

	constructor(
		private dialogRef: MatDialogRef<boolean>,
	) { }

	save(){
		//aca enviar el formulario con sus datos
		this.dialogRef.close(true);
	}

}
