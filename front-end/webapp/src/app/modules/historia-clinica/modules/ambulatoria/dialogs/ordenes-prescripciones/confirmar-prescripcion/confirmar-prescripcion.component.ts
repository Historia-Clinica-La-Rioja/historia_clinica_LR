import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';

@Component({
  selector: 'app-confirmar-prescripcion',
  templateUrl: './confirmar-prescripcion.component.html',
  styleUrls: ['./confirmar-prescripcion.component.scss']
})
export class ConfirmarPrescripcionComponent implements OnInit {

	loading = true;
	hasError: boolean;
	prescriptionId: number;

	constructor(
		private snackBarService: SnackBarService,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<ConfirmarPrescripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: ConfirmPrescriptionData) { }

	ngOnInit(): void {
		this.data.prescriptionRequest.subscribe((prescriptionId: number) => {
			this.snackBarService.showSuccess(this.data.successLabel);
			this.loading = false;
			this.hasError = false;
			this.prescriptionId = prescriptionId;
		}, _ => {
			this.snackBarService.showError(this.data.errorLabel);
			 this.hasError = true;
			});
	}

	downloadPrescription() {
		const { patientId, prescriptionType } = this.data;
		this.prescripcionesService.downloadPrescriptionPdf(patientId, this.prescriptionId, prescriptionType);
		this.closeModal();
	}

	closeModal() {
		this.dialogRef.close(this.hasError);
	}

}

export class ConfirmPrescriptionData {
	titleLabel: string;
	downloadButtonLabel: string;
	successLabel: string;
	errorLabel: string;
	prescriptionType: PrescriptionTypes;
	patientId: number;
	prescriptionRequest: Observable<number>;
}
