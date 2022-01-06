import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { SignosVitalesNuevaConsultaService } from '../../services/signos-vitales-nueva-consulta.service';

export interface PreviousData {
	isVitalSigns: boolean;
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
		signosVitalesNuevaConsultaService: SignosVitalesNuevaConsultaService,
		datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService
	}, private dialogRef: MatDialogRef<PreviousDataComponent>) { }

	ngOnInit(): void {
		this.previousData = {
			isVitalSigns: this.data.signosVitalesNuevaConsultaService.getShowPreloadedVitalSignsData(),
			isAnthropometricData: this.data.datosAntropometricosNuevaConsultaService.getShowPreloadedAnthropometricData()
		}
	}

	discardData(): void {
		if ((this.previousData.isVitalSigns))
			this.data.signosVitalesNuevaConsultaService.discardPreloadedVitalSignsData();
		if ((this.previousData.isAnthropometricData))
			this.data.datosAntropometricosNuevaConsultaService.discardPreloadedAnthropometricData();
		this.dialogRef.close(true);
	}

	addData(): void {
		if ((this.previousData.isVitalSigns))
			this.data.signosVitalesNuevaConsultaService.savePreloadedVitalSignsData();
		if ((this.previousData.isAnthropometricData))
			this.data.datosAntropometricosNuevaConsultaService.savePreloadedAnthropometricData();
		this.dialogRef.close(true);
	}

	goBack(): void {
		this.dialogRef.close(false);
	}
}
