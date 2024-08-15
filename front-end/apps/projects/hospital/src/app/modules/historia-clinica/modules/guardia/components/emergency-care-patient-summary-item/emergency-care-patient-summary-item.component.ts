import { Component, Input } from '@angular/core';
import { PatientType } from '@historia-clinica/constants/summaries';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { EmergencyCarePatientDto } from '@api-rest/api-model';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { toPatientSummary } from './emergency-care-patient-summary-item.mapper';

@Component({
	selector: 'app-emergency-care-patient-summary-item',
	templateUrl: './emergency-care-patient-summary-item.component.html',
	styleUrls: ['./emergency-care-patient-summary-item.component.scss']
})
export class EmergencyCarePatientSummaryItemComponent {

	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;
	readonly SMALL = Size.SMALL;
	readonly EMERGENCY_CARE_TYPE = IDENTIFIER_CASES.EMERGENCY_CARE_TYPE;

	emergencyCarePatientSummaryItem: EmergencyCarePatientSummaryItem;

	@Input() episodeTypeDescription: string;
	@Input() reason: string;
	@Input() set patient(patient: EmergencyCarePatientDto) {
		if (patient)
			this.emergencyCarePatientSummaryItem = {
				summary: patient.person ? toPatientSummary(patient) : null,
				typeId: patient.typeId,
				description: patient.patientDescription
			}
	};

	constructor() { }

}

export interface EmergencyCarePatientSummaryItem {
	summary: PatientSummary;
	typeId: number;
	description: string;
}