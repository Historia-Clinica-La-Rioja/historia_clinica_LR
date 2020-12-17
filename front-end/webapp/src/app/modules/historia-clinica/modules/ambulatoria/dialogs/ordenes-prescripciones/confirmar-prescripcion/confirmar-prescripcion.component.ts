import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-confirmar-prescripcion',
  templateUrl: './confirmar-prescripcion.component.html',
  styleUrls: ['./confirmar-prescripcion.component.scss']
})
export class ConfirmarPrescripcionComponent implements OnInit {

	loading = true;

	constructor(
		private snackBarService: SnackBarService,
		public dialogRef: MatDialogRef<ConfirmarPrescripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: ConfirmPrescriptionData) { }

	ngOnInit(): void {
		this.data.prescriptionRequest.subscribe((newRecipe: number) => {
			this.snackBarService.showSuccess(this.data.successLabel);
			this.loading = false;
		}, _ => {this.snackBarService.showError(this.data.errorLabel)})
	}

	downloadPrescription() {
		this.dialogRef.close();
	}

	closeModal() {
		this.dialogRef.close();
	}

}

export class ConfirmPrescriptionData {
	titleLabel: string;
	downloadButtonLabel: string;
	successLabel: string;
	errorLabel: string;
	prescriptionRequest: Observable<number>;
}
