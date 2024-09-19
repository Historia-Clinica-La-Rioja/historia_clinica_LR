import { Component, Input } from '@angular/core';
import { EmergencyCarePatientDto } from '@api-rest/api-model';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { EmergencyCareChangeAttentionPlaceDialogComponent } from '../../dialogs/emergency-care-change-attention-place-dialog/emergency-care-change-attention-place-dialog.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-button',
	templateUrl: './emergency-care-change-attention-place-button.component.html',
	styleUrls: ['./emergency-care-change-attention-place-button.component.scss']
})
export class EmergencyCareChangeAttentionPlaceButtonComponent {

	@Input() patient: EmergencyCarePatientDto;

	constructor(
		private readonly dialogService: DialogService<EmergencyCareChangeAttentionPlaceDialogComponent>,
		private readonly snackBarService: SnackBarService,
	) { }

	openChangeAttentionPlaceDialog(){
		const editDialogRef = this.dialogService.open(EmergencyCareChangeAttentionPlaceDialogComponent,
			{ dialogWidth: DialogWidth.MEDIUM },
            this.patient
		)
		editDialogRef.afterClosed().subscribe(edited => {
			if (edited) {
				//enviar datos al BE
				this.snackBarService.showSuccess('guardia.home.attention_places.change-attention-place.SUCESS');
            }
		});
	}

}
