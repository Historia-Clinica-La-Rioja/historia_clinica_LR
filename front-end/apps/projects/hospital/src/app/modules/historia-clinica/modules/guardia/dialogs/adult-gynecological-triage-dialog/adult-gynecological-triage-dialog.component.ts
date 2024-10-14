import { Component, Inject, OnDestroy } from '@angular/core';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { LastTriage } from '../../utils/last-triage';
import { ButtonType } from '@presentation/components/button/button.component';
import { TriageActionsService } from '../../services/triage-actions.service';
import { Subscription, take } from 'rxjs';

@Component({
	selector: 'app-adult-gynecological-triage-dialog',
	templateUrl: './adult-gynecological-triage-dialog.component.html',
	styleUrls: ['./adult-gynecological-triage-dialog.component.scss']
})
export class AdultGynecologicalTriageDialogComponent extends LastTriage implements OnDestroy {

	private persistSuscription: Subscription;
	readonly NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = false;
	readonly ButtonType = ButtonType;
	isLoading = false;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly newTriageService: NewTriageService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		protected triageService: TriageService,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
		readonly dialogRef: MatDialogRef<AdultGynecologicalTriageDialogComponent>,
		readonly triageActionsService: TriageActionsService,	
	) {
		super(triageService, episodeId);
		this.persistSuscription = this.triageActionsService.persist$.pipe(take(1)).subscribe(_ => this.persistAdultGynecologicalTriage());
	}

	ngOnDestroy(): void {
		this.persistSuscription.unsubscribe();
	}

	persistAdultGynecologicalTriage() {
		this.isLoading = true;
		const triageAdultGynecological = this.triageActionsService.triageAdultGynecological;
		this.triageService.newAdultGynecological(this.episodeId, triageAdultGynecological)
			.subscribe(idReturned => {
				this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
				this.dialogRef.close(idReturned);
				this.newTriageService.newTriage();
				if (triageAdultGynecological.riskFactors)
					this.newRiskFactorsService.newRiskFactors();
			}, _ => {
				this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
				this.isLoading = false;
			});
	}
}
