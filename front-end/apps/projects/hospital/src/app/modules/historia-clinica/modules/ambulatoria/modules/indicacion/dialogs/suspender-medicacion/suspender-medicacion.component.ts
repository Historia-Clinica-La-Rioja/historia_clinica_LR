import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MedicationInfoDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MedicationStatusChange } from '../../../../constants/prescripciones-masterdata';
import { PrescripcionesService } from '../../../../services/prescripciones.service';

@Component({
  selector: 'app-suspender-medicacion',
  templateUrl: './suspender-medicacion.component.html',
  styleUrls: ['./suspender-medicacion.component.scss']
})
export class SuspenderMedicacionComponent implements OnInit {

	medications: MedicationInfoDto[];
	suspendMedicationForm: FormGroup;
	hasError = hasError;

	public readonly medicationStatusChange = MedicationStatusChange;

	constructor(
		private prescripcionesService: PrescripcionesService,
		private snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder,
		public dialogRef: MatDialogRef<SuspenderMedicacionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			medications: MedicationInfoDto[],
			patientId: number,
		}
	) { }

	ngOnInit(): void {
		this.medications = this.data.medications;

		this.suspendMedicationForm = this.formBuilder.group({
			dayQuantity: [null, Validators.required],
			observations: [null],
		});
	}

	closeModal(): void {
		this.dialogRef.close();
	}

	suspendMedication() {
		this.prescripcionesService.changeMedicationStatus(
			this.medicationStatusChange.SUSPEND,
			this.data.patientId,
			this.medications.map(m => m.id),
			this.suspendMedicationForm.controls.dayQuantity.value, this.suspendMedicationForm.controls.observations.value
		).subscribe(
			() => {
				this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_SUCCESS');
				this.closeModal();
			},
			_ => {
				this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_ERROR');
			}
		);
	}

}
