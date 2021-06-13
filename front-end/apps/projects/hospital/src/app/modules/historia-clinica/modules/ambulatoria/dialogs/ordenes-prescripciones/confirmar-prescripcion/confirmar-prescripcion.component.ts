import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';

@Component({
  selector: 'app-confirmar-prescripcion',
  templateUrl: './confirmar-prescripcion.component.html',
  styleUrls: ['./confirmar-prescripcion.component.scss']
})
export class ConfirmarPrescripcionComponent implements OnInit {

	prescriptionPdfInfo: number[];

	constructor(
		private snackBarService: SnackBarService,
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

	closeModal() {
		this.dialogRef.close();
	}

}

export class ConfirmPrescriptionData {
	titleLabel: string;
	downloadButtonLabel: string;
	successLabel: string;
	prescriptionType: PrescriptionTypes;
	patientId: number;
	prescriptionRequest: number | number[];
}
