import { Component, EventEmitter, Output } from '@angular/core';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-emergency-care-temporary-patient-header',
	templateUrl: './emergency-care-temporary-patient-header.component.html',
	styleUrls: ['./emergency-care-temporary-patient-header.component.scss']
})
export class EmergencyCareTemporaryPatientHeader {

	@Output() patientSelectedEvent = new EventEmitter<Patient>();

	Color = Color;

	constructor(
	) { }

	patientSelected(patient: Patient) {
		this.patientSelectedEvent.next(patient);
	}

}
