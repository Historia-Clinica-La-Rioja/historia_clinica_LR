import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-triage-details',
	templateUrl: './triage-details.component.html',
	styleUrls: ['./triage-details.component.scss']
})

export class TriageDetailsComponent implements OnInit {

	@Input() triage: Triage;

	constructor() {
	}

	ngOnInit(): void {
	}

}

export interface Triage {
	creationDate: Date;
	category: {
		name: string;
		colorHex: string;
	};
	professional: {
		firstName: string,
		lastName: string
	};
	doctorsOfficeDescription?: string;
	vitalSigns?: {
		bloodOxygenSaturation: {
			value: number,
			effectiveTime: Date
		},
		diastolicBloodPressure?: {
			value: number,
			effectiveTime: Date
		},
		heartRate: {
			value: number,
			effectiveTime: Date
		},
		respiratoryRate: {
			value: number,
			effectiveTime: Date
		},
		systolicBloodPressure?: {
			value: number,
			effectiveTime: Date
		},
		temperature?: {
			value: number,
			effectiveTime: Date
		}
	};
	appearance?: {
		bodyTemperatureDescription?: string,
		cryingExcessive?: boolean,
		muscleHypertoniaDescription?: string
	};
	notes?: string;

}
