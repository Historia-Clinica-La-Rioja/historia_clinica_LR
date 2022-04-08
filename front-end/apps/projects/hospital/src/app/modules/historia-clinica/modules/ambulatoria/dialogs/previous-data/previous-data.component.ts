import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { FactoresDeRiesgoFormService } from '../../../../services/factores-de-riesgo-form.service';

export interface PreviousData {
	isRiskFactors: boolean;
	isAnthropometricData: boolean;
}
@Component({
	selector: 'app-previous-data',
	templateUrl: './previous-data.component.html',
	styleUrls: ['./previous-data.component.scss']
})
export class PreviousDataComponent implements OnInit {

	previousData: PreviousData;

	constructor(@Inject(MAT_DIALOG_DATA) public data: {
		factoresDeRiesgoFormService: FactoresDeRiesgoFormService,
		datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService
	}, private dialogRef: MatDialogRef<PreviousDataComponent>) { }

	ngOnInit(): void {
		this.previousData = {
			isRiskFactors: this.data.factoresDeRiesgoFormService.getShowPreloadedRiskFactorsData(),
			isAnthropometricData: this.data.datosAntropometricosNuevaConsultaService.getShowPreloadedAnthropometricData()
		}
	}

	discardData(): void {
		if ((this.previousData.isRiskFactors))
			this.data.factoresDeRiesgoFormService.discardPreloadedRiskFactorsData();
		if ((this.previousData.isAnthropometricData))
			this.data.datosAntropometricosNuevaConsultaService.discardPreloadedAnthropometricData();
		this.dialogRef.close(true);
	}

	addData(): void {
		if ((this.previousData.isRiskFactors))
			this.data.factoresDeRiesgoFormService.savePreloadedRiskFactorsData();
		if ((this.previousData.isAnthropometricData))
			this.data.datosAntropometricosNuevaConsultaService.savePreloadedAnthropometricData();
		this.dialogRef.close(true);
	}

	goBack(): void {
		this.dialogRef.close(false);
	}
}
