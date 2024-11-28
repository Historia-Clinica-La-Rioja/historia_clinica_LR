import { Component, Input } from '@angular/core';
import { IsolationAlertDetail } from '@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
	selector: 'app-isolation-alert-header',
	templateUrl: './isolation-alert-header.component.html',
	styleUrls: ['./isolation-alert-header.component.scss']
})
export class IsolationAlertHeaderComponent {

	readonly MAX_ISOLATION_ALERT_TO_SHOW: number = 2;
	readonly BASIC_BUTTON = ButtonType.BASIC;

	@Input() isolationAlertsDetails: IsolationAlertDetail[];
	constructor() { }

}
