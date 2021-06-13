import { Component, Input, OnInit } from '@angular/core';
import { DoctorInfoDto } from '@api-rest/api-model';

@Component({
  selector: 'app-item-prescripciones',
  templateUrl: './item-prescripciones.component.html',
  styleUrls: ['./item-prescripciones.component.scss']
})
export class ItemPrescripcionesComponent implements OnInit {

	@Input() prescriptionItemData: PrescriptionItemData;

	constructor() { }

	ngOnInit(): void {
	}

}

export class PrescriptionItemData {
	prescriptionStatus: string;
	prescriptionPt: string;
	problemPt: string;
	doctor: DoctorInfoDto;
	totalDays: number;
}
