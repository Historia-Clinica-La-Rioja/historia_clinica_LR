import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AnthropometricGraphicEnablementDto } from '@api-rest/api-model';
import { AnthropometricGraphicService } from '@api-rest/services/anthropometric-graphic.service';
import { PatientEvolutionChartsData, PatientEvolutionChartsPopupComponent } from '@historia-clinica/dialogs/patient-evolution-charts-popup/patient-evolution-charts-popup.component';
import { BehaviorSubject } from 'rxjs';

const WIDTH = '90vw';
const HEIGHT = '90vh';

@Injectable()
export class PatientEvolutionChartsService {

	private anthropometricDataUploaded: AnthropometricData;
	private patientId: number;
	private anthropometricGraphicEnablement: AnthropometricGraphicEnablementDto;
	private isEnabledPatientEvolutionChartsSubject = new BehaviorSubject<boolean>(false);
	isEnabledPatientEvolutionCharts$ = this.isEnabledPatientEvolutionChartsSubject.asObservable();

	constructor(
		private readonly anthropometricGraphicService: AnthropometricGraphicService,
		private readonly dialog: MatDialog,
	) { }

	setPatientEvolutionChartsData(patientId: number) {
		this.patientId = patientId;
		this.anthropometricGraphicService.canShowPercentilesGraphic(this.patientId).subscribe(anthropometricGraphicEnablement => {
			this.anthropometricGraphicEnablement = anthropometricGraphicEnablement;
			this.checkButtonEnablement();			
		});
	}

	setAntropometricDataUploaded(anthropometricDataUploaded: AnthropometricData) {
		this.anthropometricDataUploaded = anthropometricDataUploaded;
		this.checkButtonEnablement();
	}

	checkButtonEnablement() {
		const hasCurrentValueOfAnthropometricData = this.hasAntropometricDataUploaded();
		const hasHistoricalAnthropometricData = this.anthropometricGraphicEnablement?.hasAnthropometricData;
		const hasValidAge = this.anthropometricGraphicEnablement?.hasValidAge;
		const hasValidGender = this.anthropometricGraphicEnablement?.hasValidGender;
		
		this.isEnabledPatientEvolutionChartsSubject.next(hasValidAge && (hasValidGender || hasCurrentValueOfAnthropometricData || hasHistoricalAnthropometricData));
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

	private hasAntropometricDataUploaded(): boolean {
		return !!(this.anthropometricDataUploaded?.headCircumference || this.anthropometricDataUploaded?.height || this.anthropometricDataUploaded?.weight)
	}

	private getDialogData(): PatientEvolutionChartsData {
		const anthropometricDataUploaded = this.hasAntropometricDataUploaded() ? this.anthropometricDataUploaded : null
		return {
			anthropometricDataUploaded,
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
