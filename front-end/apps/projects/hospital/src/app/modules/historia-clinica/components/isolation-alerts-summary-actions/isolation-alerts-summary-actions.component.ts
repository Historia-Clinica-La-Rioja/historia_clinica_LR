import { Component, Input } from '@angular/core';
import { IsolationAlertActionPopup, IsolationAlertActionPopupComponent } from '@historia-clinica/dialogs/isolation-alert-action-popup/isolation-alert-action-popup.component';
import { DialogConfiguration, DialogService, DialogWidth } from '@presentation/services/dialog.service';

@Component({
	selector: 'app-isolation-alerts-summary-actions',
	templateUrl: './isolation-alerts-summary-actions.component.html',
	styleUrls: ['./isolation-alerts-summary-actions.component.scss']
})
export class IsolationAlertsSummaryActionsComponent {

	@Input() isolationAlertId: number;

	constructor(
		private readonly dialogService: DialogService<IsolationAlertActionPopupComponent>
	) { }

	openViewDetail() {
		const popupData: IsolationAlertActionPopup = {
			title: 'historia-clinica.isolation-alert.ISOLATION_ALERT_DETAILS',
			hasActions: false,
			isolationAlertId: this.isolationAlertId,
		};
		const dialogConfiguration: DialogConfiguration = { dialogWidth: DialogWidth.SMALL, };
		this.dialogService.open(IsolationAlertActionPopupComponent, dialogConfiguration, popupData);
	}

}
