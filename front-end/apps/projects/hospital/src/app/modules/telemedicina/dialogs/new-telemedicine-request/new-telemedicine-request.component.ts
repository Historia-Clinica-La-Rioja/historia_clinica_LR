import { Component, OnInit } from '@angular/core';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';

@Component({
	selector: 'app-new-telemedicine-request',
	templateUrl: './new-telemedicine-request.component.html',
	styleUrls: ['./new-telemedicine-request.component.scss']
})
export class NewTelemedicineRequestComponent implements OnInit {
	selectedPatient: Patient;

	constructor() { }

	ngOnInit(): void {
	}

	onSelectedPatient(selectedPatient: Patient){
		this.selectedPatient=selectedPatient;
	}
}
