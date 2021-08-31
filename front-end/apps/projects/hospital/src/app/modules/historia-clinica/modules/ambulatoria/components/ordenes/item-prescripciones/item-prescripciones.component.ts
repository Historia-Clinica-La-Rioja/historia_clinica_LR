import { Component, Input } from '@angular/core';
import { DoctorInfoDto } from '@api-rest/api-model';

@Component({
  selector: 'app-item-prescripciones',
  templateUrl: './item-prescripciones.component.html',
  styleUrls: ['./item-prescripciones.component.scss']
})
export class ItemPrescripcionesComponent {

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
