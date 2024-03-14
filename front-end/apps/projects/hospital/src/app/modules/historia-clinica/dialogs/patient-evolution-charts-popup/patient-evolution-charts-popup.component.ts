import { Component, Inject, } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnthropometricData } from '@historia-clinica/services/patient-evolution-charts.service';
@Component({
	selector: 'app-patient-evolution-charts-popup',
	templateUrl: './patient-evolution-charts-popup.component.html',
	styleUrls: ['./patient-evolution-charts-popup.component.scss']
})
export class PatientEvolutionChartsPopupComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: PatientEvolutionChartsData,
	) { }

}

export interface PatientEvolutionChartsData {
	anthropometricDataUploaded: AnthropometricData;
}
