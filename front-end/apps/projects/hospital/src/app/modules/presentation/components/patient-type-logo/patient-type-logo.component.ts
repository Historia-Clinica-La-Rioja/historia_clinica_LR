import { Component, Input, OnChanges } from '@angular/core';

@Component({
	selector: 'app-patient-type-logo',
	templateUrl: './patient-type-logo.component.html',
	styleUrls: ['./patient-type-logo.component.scss']
})

export class PatientTypeLogoComponent implements OnChanges {


	@Input() patientTypeData: PatientTypeData;
	public color: string;
	constructor() { }

	ngOnChanges(): void {
		this.color = colorsById[this.patientTypeData?.id]?.color;
	}
}

enum PatientTypeColors {
	PERMANENTE = '#1976d2',
	ACTIVO = '#009B68',
	TEMPORARIO = '#D34444',
	HISTORICO = '#585E62',
	TELEFONICO = '#585E62',
	RECHAZADO = '#585E62',
	PERMANENTE_NO_VALIDO = '#33AFFF'
}

const colorsById = {
	1: { color: PatientTypeColors.PERMANENTE },
	2: { color: PatientTypeColors.ACTIVO },
	3: { color: PatientTypeColors.TEMPORARIO },
	4: { color: PatientTypeColors.HISTORICO },
	5: { color: PatientTypeColors.TELEFONICO },
	6: { color: PatientTypeColors.RECHAZADO },
	7: { color: PatientTypeColors.PERMANENTE_NO_VALIDO }
};

export class PatientTypeData {
	id: number;
	description: string;
}
