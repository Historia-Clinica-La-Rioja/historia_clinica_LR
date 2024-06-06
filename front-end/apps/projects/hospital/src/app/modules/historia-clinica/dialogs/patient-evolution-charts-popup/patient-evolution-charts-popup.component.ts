import { Component, Inject, OnInit, } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnthropometricData } from '@historia-clinica/services/patient-evolution-charts.service';
import { PatientGenderAgeDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-patient-evolution-charts-popup',
	templateUrl: './patient-evolution-charts-popup.component.html',
	styleUrls: ['./patient-evolution-charts-popup.component.scss']
})
export class PatientEvolutionChartsPopupComponent implements OnInit {

	patientData$: Observable<PatientGenderAgeDto>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: PatientEvolutionChartsData,
		private readonly patientService: PatientService,
	) { }

	ngOnInit(): void {
		this.patientData$ = this.patientService.getPatientGender(this.data.patientId);
	}

}

export interface PatientEvolutionChartsData {
	anthropometricDataUploaded: AnthropometricData;
	patientId: number;
}
