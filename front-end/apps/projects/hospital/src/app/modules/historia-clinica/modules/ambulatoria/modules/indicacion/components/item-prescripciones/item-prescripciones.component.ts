import { Component, Input } from '@angular/core';
import { DoctorInfoDto } from '@api-rest/api-model';
import { MEDICATION_STATUS, STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { PatientNameService } from "@core/services/patient-name.service";
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-item-prescripciones',
	templateUrl: './item-prescripciones.component.html',
	styleUrls: ['./item-prescripciones.component.scss']
})
export class ItemPrescripcionesComponent {
	medication_status = MEDICATION_STATUS;
	STUDY_STATUS = STUDY_STATUS;

	@Input() prescriptionItemData: PrescriptionItemData;
	Color = Color;

	constructor(private readonly patientNameService: PatientNameService) { }

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return `${this.patientNameService.getPatientName(firstName, nameSelfDetermination)}`;
	}

}

export class PrescriptionItemData {
	prescriptionStatus: string;
	prescriptionPt: string;
	problemPt: string;
	doctor: DoctorInfoDto;
	totalDays?: number | string;
}
