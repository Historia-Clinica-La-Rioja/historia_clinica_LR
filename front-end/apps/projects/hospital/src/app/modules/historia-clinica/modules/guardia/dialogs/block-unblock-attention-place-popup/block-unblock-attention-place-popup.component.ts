import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EmergencyCareDetails, SpaceTypeDetails } from '../../components/emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { getTemplateData } from './block-unblock-attention-place-popup.constants';
import { FormControl, FormGroup } from '@angular/forms';
import { BlockUnblockAttentionPlaceService } from '../../services/block-unblock-attention-place.service';
import { SpaceType } from '../../components/emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-block-unblock-attention-place-popup',
	templateUrl: './block-unblock-attention-place-popup.component.html',
	styleUrls: ['./block-unblock-attention-place-popup.component.scss'],
	providers: [BlockUnblockAttentionPlaceService],
})
export class BlockUnblockAttentionPlacePopupComponent implements OnInit {

	readonly RAISED = ButtonType.RAISED;
	template: BlockUnblockTemplateData;
	dialogType: DialogType;
	submitFormEvent = new EventEmitter<void>();
	isLoadingRequest = false;
	blockReasonForm = new FormGroup({
		reason: new FormControl(null)
	});

	constructor(
		private readonly blockUnblockAttentionPlaceService: BlockUnblockAttentionPlaceService,
		private readonly snackBarService: SnackBarService,
		private dialogRef: MatDialogRef<BlockUnblockAttentionPlacePopupComponent>,
		@Inject(MAT_DIALOG_DATA) public data: BlockUnblockAttentionPlaceData,
	) { }

	ngOnInit(): void {
		this.dialogType = this.data.dialogType;
		this.template = getTemplateData(this.dialogType);
	}

	save() {
		this.isLoadingRequest = true;
		this.submitFormEvent.emit();

		if (!this.blockReasonForm.valid) {
			this.isLoadingRequest = false;
			return;
		}

		this.persitBlockUnblock();
		
	}

	closeDialog() {
		this.dialogRef.close();
	}

	private persitBlockUnblock() {
		const attentionPlace = this.getAttentionPlaceDetails();
		const attentionPlaceType = this.data.attentionPlaceDetails.type;
		const blockReason = this.blockReasonForm.value.reason;
		const persistAction$ = this.dialogType === 'block' ? this.blockUnblockAttentionPlaceService.blockAttentionPlace(attentionPlace.id, attentionPlaceType, blockReason) : this.blockUnblockAttentionPlaceService.unblockAttentionPlace(attentionPlace.id, attentionPlaceType);
		persistAction$.subscribe({
			next: (response) => {
				const message = this.dialogType === 'block' ? 'guardia.block_unblock_attention_place.dialog.SNACKBAR_SUCCESS_BLOCK' : 'guardia.block_unblock_attention_place.dialog.SNACKBAR_SUCCESS_UNBLOCK';
				this.snackBarService.showSuccess(message);
				this.isLoadingRequest = false;
				this.dialogRef.close(true);
			},
			error: () => {
				const message = this.dialogType === 'block' ? 'guardia.block_unblock_attention_place.dialog.SNACKBAR_ERROR_BLOCK' : 'guardia.block_unblock_attention_place.dialog.SNACKBAR_ERROR_UNBLOCK';
				this.snackBarService.showError(message);
				this.isLoadingRequest = false;
			}
		});
	}

	private getAttentionPlaceDetails(): SpaceTypeDetails {
		const attentionPlaceDetails = {
			[SpaceType.DoctorsOffices]: this.data.attentionPlaceDetails.doctorsOffice,
			[SpaceType.ShockRooms]: this.data.attentionPlaceDetails.shockroom,
			[SpaceType.Beds]: this.data.attentionPlaceDetails.bed,
		}

		return attentionPlaceDetails[this.data.attentionPlaceDetails.type]
	}

}

export interface BlockUnblockAttentionPlaceData {
	dialogType: DialogType;
	attentionPlaceDetails: EmergencyCareDetails;
}

export interface BlockUnblockTemplateData {
	title: string;
	confirmationButtonDescription: string;
	confirmationButtonColor: ThemePalette;
	hasCancelButton: boolean;
	cancelButtonDescription: string;
}

export type DialogType = 'block' | 'unblock';