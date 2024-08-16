import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TriageService } from '@api-rest/services/triage.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { LastTriage } from '../../utils/last-triage';
import { TriageActionsService } from '../../services/triage-actions.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { SpecialtySectorFormValidityService } from '../../services/specialty-sector-form-validity.service';

@Component({
	selector: 'app-administrative-triage-dialog',
	templateUrl: './administrative-triage-dialog.component.html',
	styleUrls: ['./administrative-triage-dialog.component.scss']
})
export class AdministrativeTriageDialogComponent  extends LastTriage implements OnInit, OnDestroy{

	readonly NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = false;
	readonly RAISED = ButtonType.RAISED;
	private isSpecialtySectorFormValid : boolean;
	isLoading = false;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly newTriageService: NewTriageService,
		protected triageService: TriageService,
		@Inject(MAT_DIALOG_DATA) public episodeId: number,
		readonly dialogRef: MatDialogRef<AdministrativeTriageDialogComponent>,
		readonly triageActionsService: TriageActionsService,
		private specialtySectorFormValidityService: SpecialtySectorFormValidityService,
	) {
		super(triageService, episodeId);
	}

	ngOnInit() {
		this.specialtySectorFormValidityService.formValid$.subscribe(isValid => {
			this.isSpecialtySectorFormValid = isValid;
		});
	}

	ngOnDestroy() {
		this.specialtySectorFormValidityService.resetConfirmAttempt();
	}

	save() {
		this.specialtySectorFormValidityService.notifyConfirmAttempt();
		if (this.isSpecialtySectorFormValid) {
			this.isLoading = true;
			this.triageService.createAdministrative(this.episodeId, this.triageActionsService.triageAdministrative)
				.subscribe(idReturned => {
					this.snackBarService.showSuccess('guardia.triage.NEW_TRIAGE_CONFIRMATION_MSG');
					this.dialogRef.close(idReturned);
					this.newTriageService.newTriage();
				}, _ => {
					this.snackBarService.showError('guardia.triage.NEW_TRIAGE_ERROR_MSG');
					this.isLoading = false;
				});
			}
	}
}
