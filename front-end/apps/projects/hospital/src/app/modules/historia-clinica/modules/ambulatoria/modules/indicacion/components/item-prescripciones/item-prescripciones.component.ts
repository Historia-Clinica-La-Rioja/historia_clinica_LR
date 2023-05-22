import { Component, Input } from '@angular/core';
import { AppFeature, DoctorInfoDto } from '@api-rest/api-model.d';
import { MEDICATION_STATUS, STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { PatientNameService } from "@core/services/patient-name.service";
import { Color } from '@presentation/colored-label/colored-label.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-item-prescripciones',
	templateUrl: './item-prescripciones.component.html',
	styleUrls: ['./item-prescripciones.component.scss']
})
export class ItemPrescripcionesComponent {
	medication_status = MEDICATION_STATUS;
	STUDY_STATUS = STUDY_STATUS;
	isRecetaDigital: boolean = false;

	@Input() prescriptionItemData: PrescriptionItemData;
	Color = Color;

	constructor(private readonly patientNameService: PatientNameService,
				private readonly featureFlagService: FeatureFlagService) 
	{
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL).subscribe((isOn: boolean) => this.isRecetaDigital = isOn);
	}

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
	observation?: string;
	prescriptionLineState?: PrescriptionLineState;
}

export interface PrescriptionLineState {
	description: string,
	color: string
}