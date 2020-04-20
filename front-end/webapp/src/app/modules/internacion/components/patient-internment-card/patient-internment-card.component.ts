import { Component, Input, OnInit } from '@angular/core';
import { PatientBasicData } from '../../../presentation/patient-card/patient-card.component';

@Component({
	selector: 'app-patient-internment-card',
	templateUrl: './patient-internment-card.component.html',
	styleUrls: ['./patient-internment-card.component.scss']
})
export class PatientInternmentCardComponent implements OnInit {

	@Input() patientInternment: PatientInternmentData = {
		patient: {
			firstName: 'Nicolino',
			lastName: 'Ayerdi',
			gender: 'Masculino',
			age: 24,
			id: 123
		},
		room: 404,
		bed: 600,
		floor: {
			number: '5° PISO',
			description: 'Terapia intensiva'
		},
		doctor: {
			firstName: 'Tomas',
			lastName: 'Lopez',
			license: '12345',
		},
		days_interned: 50,
		admission_datetime: '20/06/2020 - 8:26hs',
	};

	constructor() {
	}

	ngOnInit(): void {
	}

}

export interface PatientInternmentData {
	patient: PatientBasicData;
	room: number;
	bed: number;
	floor: {
		number: string;
		description: string;
	};
	doctor: {
		firstName: string;
		lastName: string;
		license: string;
	};
	days_interned: number;
	admission_datetime: string;
}
