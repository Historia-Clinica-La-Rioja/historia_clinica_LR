import { Component, Input } from '@angular/core';
import { EmergencyCareDetails, SpaceState } from '../emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { BlockUnblockAttentionPlaceData, BlockUnblockAttentionPlacePopupComponent } from '../../dialogs/block-unblock-attention-place-popup/block-unblock-attention-place-popup.component';
import { SelectedSpace } from '../emergency-care-attention-place-space/emergency-care-attention-place-space.component';
import { AttentionPlaceUpdateService } from '../../services/attention-place-update.service';

@Component({
	selector: 'app-block-unblock-attention-place',
	templateUrl: './block-unblock-attention-place.component.html',
	styleUrls: ['./block-unblock-attention-place.component.scss']
})
export class BlockUnblockAttentionPlaceComponent {

	isAttentionPlaceAvailable = false;
	attentionPlaceDetails: EmergencyCareDetails;

	@Input() set emergencyCareDetails(emergencyCareDetails: EmergencyCareDetails) {
		this.setAttentionPlaceData(emergencyCareDetails);
	};
	@Input() selectedSpace: SelectedSpace;

	constructor(
		private readonly dialogService: DialogService<BlockUnblockAttentionPlacePopupComponent>,
		private readonly attentionPlaceUpdateService: AttentionPlaceUpdateService,
	) { }

	openBlockUnblockPopup() {
		const blockUnblockData = this.buildBlockUnblockData();
		const dialogRef = this.dialogService.open(BlockUnblockAttentionPlacePopupComponent,
			{ dialogWidth: DialogWidth.MEDIUM },
			blockUnblockData,
		)

		dialogRef.afterClosed().subscribe(hasToUpdate => 
			hasToUpdate && this.attentionPlaceUpdateService.notifyUpdate()
		);
	}

	private buildBlockUnblockData(): BlockUnblockAttentionPlaceData {
		return {
			dialogType: this.isAttentionPlaceAvailable ? 'block' : 'unblock',
			attentionPlaceDetails: this.attentionPlaceDetails,
		}
	}

	private setAttentionPlaceData(emergencyCareDetails: EmergencyCareDetails) {
		this.attentionPlaceDetails = emergencyCareDetails;
		const { state } = this.attentionPlaceDetails. bed || this.attentionPlaceDetails.doctorsOffice || this.attentionPlaceDetails.shockroom;
		this.isAttentionPlaceAvailable = state === SpaceState.AVAILABLE;
	}

}
