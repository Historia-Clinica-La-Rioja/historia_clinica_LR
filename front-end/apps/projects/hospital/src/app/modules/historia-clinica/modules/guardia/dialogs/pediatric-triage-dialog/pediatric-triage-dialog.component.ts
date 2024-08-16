import { Component, Inject, OnDestroy } from '@angular/core';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { LastTriage } from '../../utils/last-triage';
import { TriageActionsService } from '../../services/triage-actions.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-pediatric-triage-dialog',
	templateUrl: './pediatric-triage-dialog.component.html',
	styleUrls: ['./pediatric-triage-dialog.component.scss']
})
export class PediatricTriageDialogComponent extends LastTriage implements OnDestroy {

	private persistSuscription: Subscription;
	readonly NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = false;
	readonly RAISED = ButtonType.RAISED;
	isLoading = false;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly newTriageService: NewTriageService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		protected triageService: TriageService,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
		readonly dialogRef: MatDialogRef<PediatricTriageDialogComponent>,
		readonly triageActionService: TriageActionsService,
	) {
		super(triageService, episodeId);
		this.persistSuscription = this.triageActionService.persist$.subscribe(_ => this.setTriage());
	}

	ngOnDestroy(): void {
		this.persistSuscription.unsubscribe();
	}

	setTriage(): void {
		this.isLoading = true;
		const triage = this.triageActionService.pediatricTriage;
		this.triageService.newPediatric(this.episodeId, triage)
			.subscribe(idReturned => {
				this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
				this.dialogRef.close(idReturned);
				this.newTriageService.newTriage();
				if (triage.appearance || triage.breathing || triage.circulation)
					this.newRiskFactorsService.newRiskFactors();
			}, _ => {
				this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
				this.isLoading = false;
			});
	}
}
