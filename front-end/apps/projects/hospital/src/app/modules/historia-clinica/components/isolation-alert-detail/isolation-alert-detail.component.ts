import { Component, Input } from '@angular/core';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-isolation-alert-detail',
	templateUrl: './isolation-alert-detail.component.html',
	styleUrls: ['./isolation-alert-detail.component.scss']
})
export class IsolationAlertDetailComponent {

	readonly REGISTER_EDITOR_CASES = REGISTER_EDITOR_CASES.DATE_HOUR;
	@Input() endDateColor: string;
	@Input() isolationAlertDetail: IsolationAlertDetail;

	constructor() { }

}

export interface IsolationAlertDetail {
	diagnosis: string,
	types: string[],
	criticality: string,
	endDate: Date,
	observations?: string,
	creator?: RegisterEditor,
	editor?: RegisterEditor,
}

export enum EndDateColor {
	GREY = 'grey'
}
