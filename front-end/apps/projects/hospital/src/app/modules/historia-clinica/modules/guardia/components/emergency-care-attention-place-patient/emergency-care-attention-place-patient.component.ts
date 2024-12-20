import { Component, Input } from '@angular/core';
import { EmergencyCarePatientDto } from '@api-rest/api-model';
import { PatientType } from '@historia-clinica/constants/summaries';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';

@Component({
	selector: 'app-emergency-care-attention-place-patient',
	templateUrl: './emergency-care-attention-place-patient.component.html',
	styleUrls: ['./emergency-care-attention-place-patient.component.scss']
})
export class EmergencyCareAttentionPlacePatientComponent{

	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;
	readonly SMALL = Size.SMALL;

	emergencyCarePatientSummaryItem: EmergencyCarePatientSummaryItem;

	@Input() set patient(patient: EmergencyCarePatientDto) {
		if (patient)
			this.emergencyCarePatientSummaryItem = {
				summary: patient.person ? this.toPatientSummary(patient) : null,
				typeId: patient.typeId,
				description: patient.patientDescription
			}
	};

	constructor() { }

	toPatientSummary(patient: EmergencyCarePatientDto): PatientSummary {
		const { firstName, lastName } = patient.person;
		return {
			fullName: `${firstName} ${lastName}`,
			identification: {
				type: patient.person.identificationType,
				number: patient.person.identificationNumber
			},
			id: patient.id,
			gender: patient.person.gender,
			age: patient.person.age.years
		};
	}

}

export interface EmergencyCarePatientSummaryItem {
	summary: PatientSummary;
	typeId: number;
	description: string;
}
