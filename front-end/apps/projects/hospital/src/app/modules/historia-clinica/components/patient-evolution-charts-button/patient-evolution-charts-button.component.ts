import { Component, Input } from '@angular/core';
import { AnthropometricData, PatientEvolutionChartsService } from '@historia-clinica/services/patient-evolution-charts.service';

@Component({
	selector: 'app-patient-evolution-charts-button',
	templateUrl: './patient-evolution-charts-button.component.html',
	styleUrls: ['./patient-evolution-charts-button.component.scss']
})
export class PatientEvolutionChartsButtonComponent {

	@Input()
	set patientId(patientId: number) {
		this.patientEvolutionChartsService.patientId = patientId;
		this.patientEvolutionChartsService.updateButtonEnablementByPatientInfo();
	};

	@Input() set anthropometricData(anthropometricData: AnthropometricData) {
		this.patientEvolutionChartsService.setAntropometricDataUploaded(anthropometricData);
	};

	constructor(
		readonly patientEvolutionChartsService: PatientEvolutionChartsService,
	) { }

}
