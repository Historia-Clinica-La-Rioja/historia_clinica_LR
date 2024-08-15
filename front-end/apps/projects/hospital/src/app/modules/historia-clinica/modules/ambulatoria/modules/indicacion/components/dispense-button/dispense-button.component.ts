import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ERole, MedicationInfoDto } from '@api-rest/api-model';
import { PRESCRIPTION_STATES } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { DialogConfiguration, DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { MedicationDispensePopupComponent } from '../../dialogs/medication-dispense-popup/medication-dispense-popup.component';

@Component({
	selector: 'app-dispense-button',
	templateUrl: './dispense-button.component.html',
	styleUrls: ['./dispense-button.component.scss']
})
export class DispenseButtonComponent {

	@Input() medicationInfo: MedicationInfoDto;
	@Input() canDispense;

	@Output() confirmed: EventEmitter<boolean> = new EventEmitter<boolean>();

	INDICATED_STATE_ID: number = PRESCRIPTION_STATES.INDICADA.id;
	PERSONAL_DE_FARMACIA = ERole.PERSONAL_DE_FARMACIA;

	constructor(private readonly dialog: DialogService<MedicationDispensePopupComponent>) {}

	openDispenseDialog = () => {
		const dialog = this.dialog.open(
			MedicationDispensePopupComponent, 
			this.setDialogConfiguration(), 
			this.medicationInfo
		);
		this.dialogAfterClose(dialog);
	}

	private dialogAfterClose = (dialog) => {
		dialog.afterClosed().subscribe((value: boolean) => {
			if (!value) return;

			this.confirmed.emit(value);
		});
	}

	private setDialogConfiguration = (): DialogConfiguration  => {
		return {
			dialogWidth: DialogWidth.SMALL
		}
	}
}
