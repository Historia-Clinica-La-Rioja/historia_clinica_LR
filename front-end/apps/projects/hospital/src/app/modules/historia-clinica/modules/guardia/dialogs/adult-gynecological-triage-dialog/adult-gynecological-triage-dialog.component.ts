import { Component, Inject } from '@angular/core';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TriageAdultGynecologicalDto } from '@api-rest/api-model';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';

@Component({
	selector: 'app-adult-gynecological-triage-dialog',
	templateUrl: './adult-gynecological-triage-dialog.component.html',
	styleUrls: ['./adult-gynecological-triage-dialog.component.scss']
})
export class AdultGynecologicalTriageDialogComponent {

	private triage: TriageAdultGynecologicalDto;
	requestPending = false;
	NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = false;

	constructor(
		private triageService: TriageService,
		private readonly snackBarService: SnackBarService,
		public readonly dialogRef: MatDialogRef<AdultGynecologicalTriageDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
		private readonly newTriageService: NewTriageService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
	) {
	}

	setTriage(triage: TriageAdultGynecologicalDto): void {
		this.requestPending = true;
		this.triage = triage;
		this.triageService.newAdultGynecological(this.episodeId, this.triage)
			.subscribe(idReturned => {
				this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
				this.dialogRef.close(idReturned);
				this.newTriageService.newTriage();
				if (triage.riskFactors)
					this.newRiskFactorsService.newRiskFactors();
			}, _ => {
				this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
				this.requestPending = false;
			});
	}
}
