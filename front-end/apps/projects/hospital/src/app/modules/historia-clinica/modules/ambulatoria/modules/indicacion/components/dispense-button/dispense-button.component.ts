import { Component, Input } from '@angular/core';
import { ERole, MedicationInfoDto } from '@api-rest/api-model';
import { PRESCRIPTION_STATES } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';

@Component({
	selector: 'app-dispense-button',
	templateUrl: './dispense-button.component.html',
	styleUrls: ['./dispense-button.component.scss']
})
export class DispenseButtonComponent {

	@Input() medicationInfo: MedicationInfoDto;
	@Input() isHabilitarRecetaDigital;

	INDICATED_STATE_ID: number = PRESCRIPTION_STATES.INDICADA.id;
	PERSONAL_DE_FARMACIA = ERole.PERSONAL_DE_FARMACIA;

	openDispenseDialog = (medication: MedicationInfoDto) => {

	}
}
