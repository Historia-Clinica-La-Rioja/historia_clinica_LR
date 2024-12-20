import { Injectable } from '@angular/core';
import { IsolationAlertService } from '@api-rest/services/isolation-alert.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class IsolationAlertActionsService {

	private readonly isolationAlertActions = { [IsolationAlertActionType.FINALIZE]: () => this.finalizeAlert(), }
	private isolationAlertId: number;
	private finishActionSubject = new BehaviorSubject<boolean>(false);

	finishAction$ = this.finishActionSubject.asObservable();

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly isolationAlertService: IsolationAlertService,
	) { }

	persist(isolationAlertId: number, actionType: IsolationAlertActionType) {
		this.isolationAlertId = isolationAlertId;
		const getAction = this.isolationAlertActions[actionType];
		getAction && getAction();
	}

	private finalizeAlert() {
		return this.isolationAlertService.cancel(this.isolationAlertId).subscribe({
			next: finalizeAlert => {
				this.snackBarService.showSuccess('historia-clinica.isolation-alert-action.finish-alert.SNACKBAR_SUCCESS');
				this.finishActionSubject.next(true);
			},
			error: error => {
				this.snackBarService.showError('historia-clinica.isolation-alert-action.finish-alert.SNACKBAR_ERROR');
				this.finishActionSubject.next(false);
			}
		});
	}
}

export enum IsolationAlertActionType {
	FINALIZE,
}