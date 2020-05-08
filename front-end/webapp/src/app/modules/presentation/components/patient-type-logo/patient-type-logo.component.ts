import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'app-patient-type-logo',
	templateUrl: './patient-type-logo.component.html',
	styleUrls: ['./patient-type-logo.component.scss']
})

export class PatientTypeLogoComponent implements OnInit {


	@Input() patientTypeData: PatientTypeData;
	public color: string;
	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(): void {
		this.color = colorsById[this.patientTypeData?.id]?.color;
	}
}

enum PatientTypeColors {
	ACTIVO = '#009B68',
	TEMPORARIO = '#D34444',
	PERMANENTE = '#1976d2'
}

const colorsById = {
	1: { color: PatientTypeColors.PERMANENTE },
	2: { color: PatientTypeColors.ACTIVO },
	3: { color: PatientTypeColors.TEMPORARIO },
};

export class PatientTypeData {
	id: number;
	description: string;
}
