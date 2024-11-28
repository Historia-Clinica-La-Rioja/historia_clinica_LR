import { Component, Input } from '@angular/core';
import { IsolationAlertActionPopup, IsolationAlertActionPopupComponent } from '@historia-clinica/dialogs/isolation-alert-action-popup/isolation-alert-action-popup.component';
import { DialogConfiguration, DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { IsolationAlertActionType } from '@historia-clinica/services/isolation-alert-actions.service';
import { EditIsolationAlertPopup, EditIsolationAlertPopupComponent } from '@historia-clinica/dialogs/edit-isolation-alert-popup/edit-isolation-alert-popup.component';
import { PatientIsolationAlertsService } from '@historia-clinica/services/patient-isolation-alerts.service';

@Component({
	selector: 'app-isolation-alerts-summary-actions',
	templateUrl: './isolation-alerts-summary-actions.component.html',
	styleUrls: ['./isolation-alerts-summary-actions.component.scss']
})
export class IsolationAlertsSummaryActionsComponent {

	readonly dialogConfiguration: DialogConfiguration = { dialogWidth: DialogWidth.SMALL, };

	@Input() isolationAlertId: number;
	@Input() isIsolationAlertActive: boolean;

	constructor(
		private readonly dialogService: DialogService<IsolationAlertActionPopupComponent | EditIsolationAlertPopupComponent>,
		private readonly patientIsolationAlertService: PatientIsolationAlertsService,
	) { }

	openViewDetail() {
		const popupData: IsolationAlertActionPopup = {
			title: 'historia-clinica.isolation-alert.ISOLATION_ALERT_DETAILS',
			hasActions: false,
			isolationAlertId: this.isolationAlertId,
		};
		this.dialogService.open(IsolationAlertActionPopupComponent, this.dialogConfiguration, popupData);
	}

	openEditAlert() {
		const popupData: EditIsolationAlertPopup = { isolationAlertId: this.isolationAlertId, };
		this.dialogService.open(EditIsolationAlertPopupComponent, this.dialogConfiguration, popupData)
			.afterClosed().subscribe(action => action && this.patientIsolationAlertService.updatedIsolationAlertsSubject.next(true));
	}

	openEndAlert() {
		const popupData: IsolationAlertActionPopup = {
			title: 'historia-clinica.isolation-alert-action.finish-alert.END_ALERT_TITLE',
			hasActions: true,
			isolationAlertId: this.isolationAlertId,
			confirmLabel: 'historia-clinica.isolation-alert-action.finish-alert.CONFIRM_END_ALERT',
			actionType: IsolationAlertActionType.FINALIZE,
		};

		this.dialogService.open(IsolationAlertActionPopupComponent, this.dialogConfiguration, popupData)
			.afterClosed().subscribe(action => action && this.patientIsolationAlertService.updatedIsolationAlertsSubject.next(true));
	}

}
