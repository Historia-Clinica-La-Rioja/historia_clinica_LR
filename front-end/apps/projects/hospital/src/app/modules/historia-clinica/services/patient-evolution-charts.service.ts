import { Injectable } from '@angular/core';
import { AnthropometricGraphicService } from '@api-rest/services/anthropometric-graphic.service';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class PatientEvolutionChartsService {

	private anthropometricDataUploaded: AnthropometricData;
	private patientId: number;
	private hasEvolutionToEnabledButton = false;
	private isEnabledPatientEvolutionChartsSubject = new BehaviorSubject<boolean>(false);
	isEnabledPatientEvolutionCharts$ = this.isEnabledPatientEvolutionChartsSubject.asObservable();

	constructor(
		private readonly anthropometricGraphicService: AnthropometricGraphicService,
	) { }

	setPatientEvolutionChartsData(patientId: number) {
		this.patientId = patientId;
		this.anthropometricGraphicService.canShowPercentilesGraphic(this.patientId).subscribe(canShowGraphic => {
			this.hasEvolutionToEnabledButton = canShowGraphic;
			this.isEnabledPatientEvolutionChartsSubject.next(this.hasEvolutionToEnabledButton);
		});
	}

	setAntropometricDataUploaded(anthropometricDataUploaded: AnthropometricData) {
		this.anthropometricDataUploaded = anthropometricDataUploaded;
		this.checkButtonEnablement();
	}

	checkButtonEnablement() {
		const hasAnthropometricDataUploaded = this.hasAntropometricDataUploaded(this.anthropometricDataUploaded);
		this.isEnabledPatientEvolutionChartsSubject.next(this.hasEvolutionToEnabledButton || hasAnthropometricDataUploaded);
	}

	private hasAntropometricDataUploaded(antropometricData: AnthropometricData): boolean {
		return !!(antropometricData.headCircumference || antropometricData.height || antropometricData.weight)
	}

}

export interface AnthropometricData {
	bmi?: string;
	height?: string;
	weight?: string;
	headCircumference?: string;
}
