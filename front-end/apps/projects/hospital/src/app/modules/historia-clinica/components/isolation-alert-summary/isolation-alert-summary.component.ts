import { Component, Input } from '@angular/core';
import { IsolationAlert } from '../isolation-alert-form/isolation-alert-form.component';

@Component({
	selector: 'app-isolation-alert-summary',
	templateUrl: './isolation-alert-summary.component.html',
	styleUrls: ['./isolation-alert-summary.component.scss']
})
export class IsolationAlertSummaryComponent {

	typesDisplay: string[] = [];
	_isolationAlert: IsolationAlert;

	@Input() set isolationAlert(isolationAlert: IsolationAlert) {
		this._isolationAlert = isolationAlert;
		this.typesDisplay = isolationAlert.types.map(type => type.description);
	};

	constructor() { }

}

