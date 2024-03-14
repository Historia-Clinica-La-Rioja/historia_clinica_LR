import { Component, Input, } from '@angular/core';

@Component({
	selector: 'app-patient-evolution-charts',
	templateUrl: './patient-evolution-charts.component.html',
	styleUrls: ['./patient-evolution-charts.component.scss']
})
export class PatientEvolutionChartsComponent {

	@Input() patientId: number;

	constructor() { }

}
