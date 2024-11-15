import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { IsolationAlertService } from '@api-rest/services/isolation-alert.service';
import { EndDateColor, IsolationAlertDetail } from '@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component';
import { mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail } from '@historia-clinica/mappers/isolation-alerts.mapper';
import { IsolationAlertActionsService } from '@historia-clinica/services/isolation-alert-actions.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { IsolationAlertActionType } from '@historia-clinica/services/isolation-alert-actions.service';
import { map, Observable, Subscription } from 'rxjs';

@Component({
	selector: 'app-isolation-alert-action-popup',
	templateUrl: './isolation-alert-action-popup.component.html',
	styleUrls: ['./isolation-alert-action-popup.component.scss'],
	providers: [IsolationAlertActionsService]
})
export class IsolationAlertActionPopupComponent implements OnInit, OnDestroy {

	readonly RAISED = ButtonType.RAISED;
	readonly END_DATE_COLOR = EndDateColor;
	isolationAlertDetail$: Observable<IsolationAlertDetail>;
	isLoadingRequest = false;
	actionSubscription: Subscription;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: IsolationAlertActionPopup,
		private readonly isolationAlertService: IsolationAlertService,
		private readonly dialogRef: MatDialogRef<IsolationAlertActionPopupComponent>,
		private readonly isolationAlertActionsService: IsolationAlertActionsService,
	) { }

	ngOnInit(): void {
		this.isolationAlertDetail$ = this.isolationAlertService.getAlertDetail(this.data.isolationAlertId).pipe(map(mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail));
		this.data.hasActions && this.subscribeToActions();
	}

	ngOnDestroy(): void {
		this.actionSubscription && this.actionSubscription.unsubscribe();
	}

	closeDialog() {
		this.dialogRef.close();
	}

	confirm() {
		this.isLoadingRequest = true;
		this.isolationAlertActionsService.persist(this.data.isolationAlertId, this.data.actionType);
	}

	private subscribeToActions() {
		this.actionSubscription = this.isolationAlertActionsService.finishAction$.subscribe(finishAction => {
			this.isLoadingRequest = false;
			finishAction && this.dialogRef.close(true);
		})
	}

}

export interface IsolationAlertActionPopup {
	title: string;
	hasActions: boolean;
	confirmLabel?: string;
	isolationAlertId: number;
	actionType?: IsolationAlertActionType;
}
