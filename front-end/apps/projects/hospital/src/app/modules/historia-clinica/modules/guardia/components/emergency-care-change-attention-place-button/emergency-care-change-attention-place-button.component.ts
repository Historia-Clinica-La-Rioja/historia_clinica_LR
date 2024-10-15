import { Component, Input } from '@angular/core';
import { EmergencyCarePatientDto } from '@api-rest/api-model';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { EmergencyCareChangeAttentionPlaceDialogComponent } from '../../dialogs/emergency-care-change-attention-place-dialog/emergency-care-change-attention-place-dialog.component';
import { PlacePreview } from '../emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { AttentionPlaceUpdateService } from '../../services/attention-place-update.service';

@Component({
	selector: 'app-emergency-care-change-attention-place-button',
	templateUrl: './emergency-care-change-attention-place-button.component.html',
	styleUrls: ['./emergency-care-change-attention-place-button.component.scss']
})
export class EmergencyCareChangeAttentionPlaceButtonComponent {

	@Input() patient: EmergencyCarePatientDto;
	@Input() lastPlacePreview: PlacePreview;
	@Input() episodeId: number;

	constructor(
		private readonly dialogService: DialogService<EmergencyCareChangeAttentionPlaceDialogComponent>,
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
		private readonly snackBarService: SnackBarService,
		private attentionPlaceUpdateService: AttentionPlaceUpdateService
	) { }

	openChangeAttentionPlaceDialog(){
		const editDialogRef = this.dialogService.open(EmergencyCareChangeAttentionPlaceDialogComponent,
			{ dialogWidth: DialogWidth.LARGE },
			{
				patient: this.patient,
				lastPlacePreview: this.lastPlacePreview,
				episodeId: this.episodeId
			}
		)
		editDialogRef.afterClosed().subscribe(newAttentionPlace => {
			if (newAttentionPlace) {
				this.emergencyCareAttentionPlaceService.changeAttentionPlace(newAttentionPlace).subscribe(
					_ => {
						this.attentionPlaceUpdateService.notifyUpdate();
						this.snackBarService.showSuccess('guardia.home.attention_places.change-attention-place.SUCESS')
					},
					_ => this.snackBarService.showError('guardia.home.attention_places.change-attention-place.ERROR')
				);
            }
		});
	}
}
