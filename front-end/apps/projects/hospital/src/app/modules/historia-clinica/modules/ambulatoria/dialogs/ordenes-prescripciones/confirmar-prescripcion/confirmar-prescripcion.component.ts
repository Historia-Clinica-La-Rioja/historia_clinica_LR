import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { EnvironmentVariableService } from '@api-rest/services/environment-variable.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';
import { EnviarRecetaDigitalPorEmailComponent } from '../../enviar-receta-digital-por-email/enviar-receta-digital-por-email.component';

@Component({
  selector: 'app-confirmar-prescripcion',
  templateUrl: './confirmar-prescripcion.component.html',
  styleUrls: ['./confirmar-prescripcion.component.scss']
})
export class ConfirmarPrescripcionComponent implements OnInit {

	prescriptionPdfInfo: number[];
	isHabilitarRecetaDigital: boolean = false;

	constructor(
		private snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private prescripcionesService: PrescripcionesService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly environmentVariableService: EnvironmentVariableService,
		public dialogRef: MatDialogRef<ConfirmarPrescripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: ConfirmPrescriptionData) {
			this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
			.subscribe((result: boolean) => this.isHabilitarRecetaDigital = result);
		}

	ngOnInit(): void {
		this.snackBarService.showSuccess(this.data.successLabel);
		this.prescriptionPdfInfo = typeof this.data.prescriptionRequest === 'number' ? [this.data.prescriptionRequest] : this.data.prescriptionRequest;
	}

	downloadPrescription() {
		const { patientId, prescriptionType, identificationNumber, prescriptionRequest } = this.data;
		if (this.isHabilitarRecetaDigital) {
			this.environmentVariableService.getDigitalRecipeDomainNumber().subscribe(result => {
				const fileName = `${identificationNumber}_${result}-${prescriptionRequest[1]}`;
				this.prescripcionesService.downloadPrescriptionPdf(patientId, this.prescriptionPdfInfo, prescriptionType, fileName);
			});
		}
		else
			this.prescripcionesService.downloadPrescriptionPdf(patientId, this.prescriptionPdfInfo, prescriptionType);
		this.closeModal();
	}

	sendEmail() {
		this.dialog.open(EnviarRecetaDigitalPorEmailComponent, {
			width: '35%',
			data: {
				patientId: this.data.patientId,
				patientEmail: this.data.patientEmail,
				prescriptionRequest: this.data.prescriptionRequest[0]
			}
		}).afterClosed().subscribe(_ => this.closeModal());
	}

	closeModal() {
		this.dialogRef.close();
	}

}

export class ConfirmPrescriptionData {
	titleLabel: string;
	downloadButtonLabel: string;
	sendEmail: string;
	successLabel: string;
	prescriptionType: PrescriptionTypes;
	patientId: number;
	prescriptionRequest: number | number[];
	patientEmail?: string;
	identificationNumber: string;
}
