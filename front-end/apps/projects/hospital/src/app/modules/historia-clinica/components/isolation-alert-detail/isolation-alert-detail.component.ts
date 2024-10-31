import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-isolation-alert-detail',
	templateUrl: './isolation-alert-detail.component.html',
	styleUrls: ['./isolation-alert-detail.component.scss']
})
export class IsolationAlertDetailComponent {

	@Input() isolationAlertDetail: IsolationAlertDetail;

	constructor() { }

}

export interface IsolationAlertDetail {
	diagnosis: string,
	types: string[],
	criticality: string,
	endDate: Date,
	observations: string,
}
