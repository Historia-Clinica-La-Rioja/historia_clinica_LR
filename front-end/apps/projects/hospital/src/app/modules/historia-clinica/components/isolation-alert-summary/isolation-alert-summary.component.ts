import { Component, Input } from '@angular/core';
import { IsolationAlertDescriptionItemData } from '@historia-clinica/utils/document-summary.model';

@Component({
	selector: 'app-isolation-alert-summary',
	templateUrl: './isolation-alert-summary.component.html',
	styleUrls: ['./isolation-alert-summary.component.scss']
})
export class IsolationAlertSummaryComponent {

	@Input() isolationAlerts: IsolationAlertDescriptionItemData[];

	constructor() { }

}
