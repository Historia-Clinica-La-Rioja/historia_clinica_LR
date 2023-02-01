import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
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

	constructor(
		private snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<ConfirmarPrescripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: ConfirmPrescriptionData) { }

	ngOnInit(): void {
		this.snackBarService.showSuccess(this.data.successLabel);
		this.prescriptionPdfInfo = typeof this.data.prescriptionRequest === 'number' ? [this.data.prescriptionRequest] : this.data.prescriptionRequest;
	}

	downloadPrescription() {
		const { patientId, prescriptionType } = this.data;
		this.prescripcionesService.downloadPrescriptionPdf(patientId, this.prescriptionPdfInfo, prescriptionType);
		this.closeModal();
	}

	openDialog() {
		this.dialog.open(EnviarRecetaDigitalPorEmailComponent, {
			width: '35%',
			data: {
				patientEmail: this.data.patientEmail
			}
		})
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
}
