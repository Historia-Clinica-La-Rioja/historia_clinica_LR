import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IsolationAlertService } from '@api-rest/services/isolation-alert.service';
import { EndDateColor, IsolationAlertDetail } from '@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component';
import { mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail } from '@historia-clinica/mappers/isolation-alerts.mapper';
import { ButtonType } from '@presentation/components/button/button.component';
import { map, Observable } from 'rxjs';

@Component({
	selector: 'app-isolation-alert-action-popup',
	templateUrl: './isolation-alert-action-popup.component.html',
	styleUrls: ['./isolation-alert-action-popup.component.scss']
})
export class IsolationAlertActionPopupComponent implements OnInit {

	readonly RAISED = ButtonType.RAISED;
	readonly END_DATE_COLOR = EndDateColor;
	isolationAlertDetail$: Observable<IsolationAlertDetail>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: IsolationAlertActionPopup,
		private readonly isolationAlertService: IsolationAlertService,
	) { }

	ngOnInit(): void {
		this.isolationAlertDetail$ = this.isolationAlertService.getAlertDetail(this.data.isolationAlertId).pipe(map(mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail));
	}

}

export interface IsolationAlertActionPopup {
	title: string;
	hasActions: boolean;
	confirmLabel?: string;
	isolationAlertId: number;
}