import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ChangeEmergencyCareEpisodeAttentionPlaceDto, EmergencyCarePatientDto } from '@api-rest/api-model';
import { PlacePreview } from '../../components/emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-dialog',
	templateUrl: './emergency-care-change-attention-place-dialog.component.html',
	styleUrls: ['./emergency-care-change-attention-place-dialog.component.scss']
})
export class EmergencyCareChangeAttentionPlaceDialogComponent {

	constructor(
		private dialogRef: MatDialogRef<ChangeEmergencyCareEpisodeAttentionPlaceDto>,
		@Inject(MAT_DIALOG_DATA) public data: { patient: EmergencyCarePatientDto, lastPlacePreview: PlacePreview, episodeId: number }
	) { }

	saveNewSelectedSpace(newSelectedSpace: ChangeEmergencyCareEpisodeAttentionPlaceDto){
		if(newSelectedSpace)
			this.dialogRef.close(newSelectedSpace);
	}

}
