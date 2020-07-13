import { Component, Inject, OnInit } from '@angular/core';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';

@Component({
  selector: 'app-probable-discharge-dialog',
  templateUrl: './probable-discharge-dialog.component.html',
  styleUrls: ['./probable-discharge-dialog.component.scss']
})
export class ProbableDischargeDialogComponent implements OnInit {

	form: FormGroup;
	loading = false;

  	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { internmentEpisodeId: number, lastProbableDischargeDate: Date },
		public dialogRef: MatDialogRef<ProbableDischargeDialogComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		private readonly snackBarService: SnackBarService,
  	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			probableDischargeDate: [this.data.lastProbableDischargeDate]
		});

		this.form.controls.probableDischargeDate.setValue(this.data.lastProbableDischargeDate);
  	}

  	chosenYearHandler(newDate: Moment) {
		if (this.form.controls.probableDischargeDate.value !== null) {
			const ctrlDate: Moment = this.form.controls.probableDischargeDate.value;
			ctrlDate.year(newDate.year());
			this.form.controls.probableDischargeDate.setValue(ctrlDate);
		} else {
			this.form.controls.probableDischargeDate.setValue(newDate);
		}
	}

	chosenMonthHandler(newDate: Moment) {
		if (this.form.controls.probableDischargeDate.value !== null) {
			const ctrlDate: Moment = this.form.controls.probableDischargeDate.value;
			ctrlDate.month(newDate.month());
			this.form.controls.probableDischargeDate.setValue(ctrlDate);
		} else {
			this.form.controls.probableDischargeDate.setValue(newDate);
		}
	}

	submit(): void {
		if (this.form.controls.probableDischargeDate.value) {
			this.loading = true;
			this.internmentEpisodeService.updateProbableDischargeDate(this.form.value, this.data.internmentEpisodeId).subscribe(_ => {
					this.snackBarService.showSuccess('internaciones.internacion-paciente.card.probable_discharge_dialog.SUCCESS');
					this.dialogRef.close(true);
				}, response => {
					this.snackBarService.showError(response.errors[0]);
					this.loading = false;
				}
			);
		}
	}

}
