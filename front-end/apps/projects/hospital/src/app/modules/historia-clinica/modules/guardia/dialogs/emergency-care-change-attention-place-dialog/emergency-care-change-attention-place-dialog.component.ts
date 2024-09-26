import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EmergencyCarePatientDto } from '@api-rest/api-model';
import { PlacePreview } from '../../components/emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-dialog',
	templateUrl: './emergency-care-change-attention-place-dialog.component.html',
	styleUrls: ['./emergency-care-change-attention-place-dialog.component.scss']
})
export class EmergencyCareChangeAttentionPlaceDialogComponent {

	constructor(
		private dialogRef: MatDialogRef<boolean>,
		@Inject(MAT_DIALOG_DATA) public data: { patient: EmergencyCarePatientDto, lastPlacePreview: PlacePreview }
	) { }

	//el dto del dato a enviar BE aun no esta listo, ChangeEmergencyCareEpisodeAttentionPlaceDto
	saveNewSelectedSpace(newSelectedSpace: boolean){
		if(newSelectedSpace)
			this.dialogRef.close(newSelectedSpace);
	}

}
