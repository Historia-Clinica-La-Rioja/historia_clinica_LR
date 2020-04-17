import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-patient-card',
	templateUrl: './patient-card.component.html',
	styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent implements OnInit {

	@Input() patientReduced: PatientReduced;

	constructor() {
	}

	ngOnInit(): void {
	}

}

export class PatientReduced {
	id: string;
	firstname: string;
	lastname: string;
	gender: string;
	age: number;
}
