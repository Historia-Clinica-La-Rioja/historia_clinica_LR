import { Component, Inject, OnInit } from '@angular/core';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import { MIN_DATE } from "@core/utils/date.utils";

@Component({
	selector: 'app-probable-discharge-dialog',
	templateUrl: './probable-discharge-dialog.component.html',
	styleUrls: ['./probable-discharge-dialog.component.scss']
})
export class ProbableDischargeDialogComponent implements OnInit {

	loading = false;
	minDate = MIN_DATE;
	selectedDate: Date;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { internmentEpisodeId: number, lastProbableDischargeDate: Date },
		public dialogRef: MatDialogRef<ProbableDischargeDialogComponent>,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.selectedDate = this.data.lastProbableDischargeDate;
	}

	dateChanged(selectedDate: Date) {
		this.selectedDate = selectedDate
	}

	submit(): void {
		if (this.selectedDate) {
			this.loading = true;
			this.internmentEpisodeService.updateProbableDischargeDate({ probableDischargeDate: this.selectedDate.toISOString() }, this.data.internmentEpisodeId).subscribe(_ => {
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
