import { Component, Inject } from '@angular/core';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TriagePediatricDto } from '@api-rest/api-model';

@Component({
	selector: 'app-pediatric-triage-dialog',
	templateUrl: './pediatric-triage-dialog.component.html',
	styleUrls: ['./pediatric-triage-dialog.component.scss']
})
export class PediatricTriageDialogComponent {

	private triage: TriagePediatricDto;
	requestPending = false;

	constructor(
		private triageService: TriageService,
		private readonly snackBarService: SnackBarService,
		public readonly dialogRef: MatDialogRef<PediatricTriageDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
	) {
	}

	setTriage(triage: TriagePediatricDto): void {
		this.requestPending = true;
		this.triage = triage;
		this.triageService.newPediatric(this.episodeId, this.triage)
			.subscribe(idReturned => {
				this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
				this.dialogRef.close(idReturned);
			}, _ => {
				this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
				this.requestPending = false;
			});
	}
}
