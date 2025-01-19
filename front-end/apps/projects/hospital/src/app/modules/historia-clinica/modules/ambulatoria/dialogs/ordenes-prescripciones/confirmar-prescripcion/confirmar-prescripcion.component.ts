import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { EnvironmentVariableService } from '@api-rest/services/environment-variable.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';
import { EnviarRecetaDigitalPorEmailComponent } from '../../enviar-receta-digital-por-email/enviar-receta-digital-por-email.component';
import { DocumentRequestDto } from '@api-rest/api-model';


@Component({
  selector: 'app-confirmar-prescripcion',
  templateUrl: './confirmar-prescripcion.component.html',
  styleUrls: ['./confirmar-prescripcion.component.scss']
})
export class ConfirmarPrescripcionComponent implements OnInit {

	prescriptionPdfInfo: any;
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
		const timeout = this.data.timeout ? this.data.timeout : 0;
		setTimeout(() => {
			this.snackBarService.showSuccess(this.data.successLabel);
		}, timeout);
		this.prescriptionPdfInfo = this.data.prescriptionRequest;
	}

	downloadPrescription() {
		const { patientId, prescriptionType, identificationNumber, prescriptionRequest } = this.data;
		if (this.isHabilitarRecetaDigital && prescriptionType === PrescriptionTypes.MEDICATION) {
			this.environmentVariableService.getDigitalRecipeDomainNumber().subscribe(result => {
				(prescriptionRequest as DocumentRequestDto[]).forEach(prescriptionRequest => {
					const fileName = `${identificationNumber}_${result}-${prescriptionRequest.requestId}`;
					this.prescripcionesService.downloadPrescriptionPdf(patientId, [prescriptionRequest.documentId], prescriptionType, fileName);
				});
			});
		}
		else
			this.prescripcionesService.downloadPrescriptionPdf(patientId, prescriptionType == PrescriptionTypes.MEDICATION ? [this.prescriptionPdfInfo[0].documentId] : [this.prescriptionPdfInfo[0]], prescriptionType);

		this.closeModal();
	}

	sendEmail() {
		this.dialog.open(EnviarRecetaDigitalPorEmailComponent, {
			width: '35%',
			data: {
				patientId: this.data.patientId,
				patientEmail: this.data.patientEmail,
				prescriptionRequest: this.data.prescriptionRequest as DocumentRequestDto[]
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
	sendEmail?: string;
	successLabel: string;
	prescriptionType: PrescriptionTypes;
	patientId: number;
	prescriptionRequest: DocumentRequestDto[] | number[];
	patientEmail?: string;
	identificationNumber: string;
	timeout?: number;
}
