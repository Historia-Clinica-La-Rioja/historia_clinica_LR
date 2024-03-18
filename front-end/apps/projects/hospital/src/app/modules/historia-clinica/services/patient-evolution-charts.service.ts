import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AnthropometricGraphicService } from '@api-rest/services/anthropometric-graphic.service';
import { PatientEvolutionChartsData, PatientEvolutionChartsPopupComponent } from '@historia-clinica/dialogs/patient-evolution-charts-popup/patient-evolution-charts-popup.component';
import { BehaviorSubject } from 'rxjs';

const WIDTH = '80vw';
const HEIGHT = '90vh';

@Injectable()
export class PatientEvolutionChartsService {

	private anthropometricDataUploaded: AnthropometricData;
	private patientId: number;
	private hasEvolutionToEnabledButton = false;
	private isEnabledPatientEvolutionChartsSubject = new BehaviorSubject<boolean>(false);
	isEnabledPatientEvolutionCharts$ = this.isEnabledPatientEvolutionChartsSubject.asObservable();

	constructor(
		private readonly anthropometricGraphicService: AnthropometricGraphicService,
		private readonly dialog: MatDialog,
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

	openEvolutionChartDialog() {

		this.dialog.open(PatientEvolutionChartsPopupComponent, {
			width: WIDTH,
			height: HEIGHT,
			maxWidth: WIDTH,
			maxHeight: HEIGHT,
			autoFocus: false,
			disableClose: true,
			data: this.getDialogData()
		});
	}

	private hasAntropometricDataUploaded(antropometricData: AnthropometricData): boolean {
		return !!(antropometricData.headCircumference || antropometricData.height || antropometricData.weight)
	}

	private getDialogData(): PatientEvolutionChartsData {
		return {
			anthropometricDataUploaded: this.anthropometricDataUploaded,
			patientId: this.patientId,
		}
	}

}

export interface AnthropometricData {
	bmi?: string;
	height?: string;
	weight?: string;
	headCircumference?: string;
}
