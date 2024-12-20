
import { Component, Input } from '@angular/core';
import { IsolationAlertDetail } from '@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { DialogConfiguration, DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { DetailsOfPatientIsolationAlertsComponent } from '@historia-clinica/dialogs/details-of-patient-isolation-alerts/details-of-patient-isolation-alerts.component';

@Component({
	selector: 'app-isolation-alert-header',
	templateUrl: './isolation-alert-header.component.html',
	styleUrls: ['./isolation-alert-header.component.scss']
})
export class IsolationAlertHeaderComponent {

	readonly MAX_ISOLATION_ALERT_TO_SHOW: number = 2;
	readonly BASIC_BUTTON = ButtonType.BASIC;
	readonly dialogConfiguration: DialogConfiguration = { dialogWidth: DialogWidth.SMALL, };

	@Input() isolationAlertsDetails: IsolationAlertDetail[];
	constructor(
		private readonly dialogService: DialogService<DetailsOfPatientIsolationAlertsComponent>,
	) { }

	openIsolationAlertDetails() {
		this.dialogService.open(DetailsOfPatientIsolationAlertsComponent, this.dialogConfiguration, { isolationAlerts: this.isolationAlertsDetails })
	}

}