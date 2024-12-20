import { Component, Input } from '@angular/core';
import { EmergencyCarePatientDto } from '@api-rest/api-model';
import { PatientType } from '@historia-clinica/constants/summaries';
import { Size } from '@presentation/components/item-summary/item-summary.component';

@Component({
  selector: 'app-emergency-care-change-attention-place-patient',
  templateUrl: './emergency-care-change-attention-place-patient.component.html',
  styleUrls: ['./emergency-care-change-attention-place-patient.component.scss']
})
export class EmergencyCareChangeAttentionPlacePatientComponent {

	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;
	readonly SMALL = Size.SMALL;

	emergencyCarePatientSummaryItem: EmergencyCarePatientSummaryItem;

	@Input() set patient(patient: EmergencyCarePatientDto) {
		if (patient) {
		this.emergencyCarePatientSummaryItem = {
			person: patient.person ? { fullName: `${patient?.person.firstName} ${patient?.person.lastName}` } : null,
			typeId: patient.typeId,
			description: patient.patientDescription
		};
		}
	}

	constructor() { }

}

interface EmergencyCarePatientSummaryItem {
	person: { fullName: string };
	typeId: number;
	description: string;
}
