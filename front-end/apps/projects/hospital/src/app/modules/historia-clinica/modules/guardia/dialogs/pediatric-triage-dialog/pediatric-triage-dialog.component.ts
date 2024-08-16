import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { LastTriage } from '../../utils/last-triage';
import { TriageActionsService } from '../../services/triage-actions.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { Subscription } from 'rxjs';
import { SpecialtySectorFormValidityService } from '../../services/specialty-sector-form-validity.service';

@Component({
	selector: 'app-pediatric-triage-dialog',
	templateUrl: './pediatric-triage-dialog.component.html',
	styleUrls: ['./pediatric-triage-dialog.component.scss']
})
export class PediatricTriageDialogComponent extends LastTriage implements OnInit, OnDestroy {

	private persistSuscription: Subscription;
	readonly NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = false;
	readonly RAISED = ButtonType.RAISED;
	private isSpecialtySectorFormValid : boolean;
	isLoading = false;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly newTriageService: NewTriageService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		protected triageService: TriageService,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
		readonly dialogRef: MatDialogRef<PediatricTriageDialogComponent>,
		readonly triageActionService: TriageActionsService,
		private specialtySectorFormValidityService: SpecialtySectorFormValidityService,
	) {
		super(triageService, episodeId);
		this.persistSuscription = this.triageActionService.persist$.subscribe(_ => this.setTriage());
	}

	ngOnInit() {
		this.specialtySectorFormValidityService.formValid$.subscribe(isValid => {
			this.isSpecialtySectorFormValid = isValid;
		});
	}

	ngOnDestroy() {
		this.specialtySectorFormValidityService.resetConfirmAttempt();
		this.persistSuscription.unsubscribe();
	}

	setTriage() {
		this.specialtySectorFormValidityService.notifyConfirmAttempt();
		if (this.isSpecialtySectorFormValid) {
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
}
