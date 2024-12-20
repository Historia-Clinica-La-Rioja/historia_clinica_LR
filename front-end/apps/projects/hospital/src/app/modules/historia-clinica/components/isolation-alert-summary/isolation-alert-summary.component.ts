import { Component, Input } from '@angular/core';
import { IsolationAlertDescriptionItemData } from '@historia-clinica/utils/document-summary.model';
import { REGISTER_EDITOR_CASES } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-isolation-alert-summary',
	templateUrl: './isolation-alert-summary.component.html',
	styleUrls: ['./isolation-alert-summary.component.scss']
})
export class IsolationAlertSummaryComponent {

	readonly REGISTER_EDITOR_CASES = REGISTER_EDITOR_CASES.DATE_HOUR;
	@Input() isolationAlerts: IsolationAlertDescriptionItemData[];

	constructor() { }

}
