import { Component, Input } from '@angular/core';
import { DoctorInfoDto } from '@api-rest/api-model';
import { MEDICATION_STATUS, STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';

@Component({
  selector: 'app-item-prescripciones',
  templateUrl: './item-prescripciones.component.html',
  styleUrls: ['./item-prescripciones.component.scss']
})
export class ItemPrescripcionesComponent {
	medication_status = MEDICATION_STATUS;
	STUDY_STATUS = STUDY_STATUS;

	@Input() prescriptionItemData: PrescriptionItemData;

	constructor() { }

}

export class PrescriptionItemData {
	prescriptionStatus: string;
	prescriptionPt: string;
	problemPt: string;
	doctor: DoctorInfoDto;
	totalDays: number;
}
