import { Component, Input } from '@angular/core';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-patient-location',
	templateUrl: './patient-location.component.html',
	styleUrls: ['./patient-location.component.scss']
})
export class PatientLocationComponent {
	identiferCases = IDENTIFIER_CASES;

	@Input() patientLocation: PatientLocation;
	@Input() professionalFullName: string;
}

export interface PatientLocation {
	bedNumber: string;
	roomNumber: string;
	sector: string;
}
