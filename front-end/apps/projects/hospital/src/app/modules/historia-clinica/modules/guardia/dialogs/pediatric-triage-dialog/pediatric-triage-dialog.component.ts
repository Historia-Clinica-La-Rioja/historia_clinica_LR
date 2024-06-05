import { Component, Inject } from '@angular/core';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TriagePediatricDto } from '@api-rest/api-model';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { LastTriage } from '../../utils/last-triage';

@Component({
	selector: 'app-pediatric-triage-dialog',
	templateUrl: './pediatric-triage-dialog.component.html',
	styleUrls: ['./pediatric-triage-dialog.component.scss']
})
export class PediatricTriageDialogComponent extends LastTriage {

	private triage: TriagePediatricDto;
	requestPending = false;
	NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = false;

	constructor(
		protected triageService: TriageService,
		private readonly snackBarService: SnackBarService,
		public readonly dialogRef: MatDialogRef<PediatricTriageDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
		private readonly newTriageService: NewTriageService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
	) {
		super(triageService, episodeId);
	}

	setTriage(triage: TriagePediatricDto): void {
		this.requestPending = true;
		this.triage = triage;
		this.triageService.newPediatric(this.episodeId, this.triage)
			.subscribe(idReturned => {
				this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
				this.dialogRef.close(idReturned);
				this.newTriageService.newTriage();
				if (triage.appearance || triage.breathing || triage.circulation)
					this.newRiskFactorsService.newRiskFactors();
			}, _ => {
				this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
				this.requestPending = false;
			});
	}
}
