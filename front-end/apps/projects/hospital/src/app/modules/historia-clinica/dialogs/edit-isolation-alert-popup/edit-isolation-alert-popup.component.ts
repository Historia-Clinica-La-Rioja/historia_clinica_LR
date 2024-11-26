import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { IsolationAlertActionPopup } from '../isolation-alert-action-popup/isolation-alert-action-popup.component';
import { IsolationAlertService } from '@api-rest/services/isolation-alert.service';
import { toIsolationAlert, toUpdateIsolationAlertDto } from '@historia-clinica/mappers/isolation-alerts.mapper';
import { map, Observable, Subscription } from 'rxjs';
import { IsolationAlert } from '@historia-clinica/components/isolation-alert-form/isolation-alert-form.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { SaveIsolationAlertService } from '@historia-clinica/services/save-isolation-alert.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-edit-isolation-alert-popup',
	templateUrl: './edit-isolation-alert-popup.component.html',
	styleUrls: ['./edit-isolation-alert-popup.component.scss']
})
export class EditIsolationAlertPopupComponent implements OnInit, OnDestroy {

	readonly RAISED = ButtonType.RAISED;
	isolationAlertDetails$: Observable<IsolationAlert>;
	isLoadingRequest = false;
	isolationAlertToPersist: IsolationAlert;
	persistSubscription: Subscription;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: IsolationAlertActionPopup,
		private readonly dialogRef: MatDialogRef<EditIsolationAlertPopupComponent>,
		private readonly isolationAlertService: IsolationAlertService,
		private readonly saveIsolationAlertService: SaveIsolationAlertService,
		private readonly snackbarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.isolationAlertDetails$ = this.isolationAlertService.getAlertDetail(this.data.isolationAlertId).pipe(map(toIsolationAlert));
		this.persistSubscription = this.saveIsolationAlertService.persist$.subscribe(isolationAlertToPersist => this.persistEdit(isolationAlertToPersist));
	}

	ngOnDestroy(): void {
		this.persistSubscription.unsubscribe();
	}

	closeDialog() {
		this.dialogRef.close();
	}

	confirm() {
		this.saveIsolationAlertService.submitSubject.next();
	}

	persistEdit(isolationAlert: IsolationAlert) {
		const isolationAlertDto = toUpdateIsolationAlertDto(isolationAlert);
		this.isLoadingRequest = true;
		this.isolationAlertService.update(isolationAlert.id, isolationAlertDto).subscribe({
			next: (save) => this.successfulEdition(),
			error: (error) => this.unsuccessfulEdition()
		});

	}

	private successfulEdition() {
		this.snackbarService.showSuccess('historia-clinica.isolation-alert-action.edit-alert.SNACKBAR_SUCCESS');
		this.isLoadingRequest = false;
		this.dialogRef.close(true);
	}

	private unsuccessfulEdition() {
		this.snackbarService.showError('historia-clinica.isolation-alert-action.edit-alert.SNACKBAR_ERROR');
		this.isLoadingRequest = false;
	}

}

export interface EditIsolationAlertPopup {
	isolationAlertId: number;
}