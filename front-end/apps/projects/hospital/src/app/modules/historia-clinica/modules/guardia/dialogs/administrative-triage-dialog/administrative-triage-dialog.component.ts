import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TriageService } from '@api-rest/services/triage.service';
import { TriageAdministrativeDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-administrative-triage-dialog',
	templateUrl: './administrative-triage-dialog.component.html',
	styleUrls: ['./administrative-triage-dialog.component.scss']
})
export class AdministrativeTriageDialogComponent {

	private triage: TriageAdministrativeDto;
	requestPending = false;
	constructor(
		private triageService: TriageService,
		private readonly snackBarService: SnackBarService,
		public readonly dialogRef: MatDialogRef<AdministrativeTriageDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
	) {}

	setTriage(triage: TriageAdministrativeDto): void {
		this.requestPending = true;
		this.triage = triage;
		this.triageService.createAdministrative(this.episodeId, this.triage)
			.subscribe(idReturned => {
				this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
				this.dialogRef.close(idReturned);
			}, _ => {
				this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
				this.requestPending = false;
			});
	}
}
