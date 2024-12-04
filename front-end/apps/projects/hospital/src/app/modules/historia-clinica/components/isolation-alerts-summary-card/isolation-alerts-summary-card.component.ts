import { Component, Input, OnInit } from '@angular/core';
import { IsolationAlertMasterDataService } from '@api-rest/services/isolation-alert-master-data.service';
import { ISOLATION_ALERT_HEADER } from '@historia-clinica/constants/summaries';
import { PatientIsolationAlertsService } from '@historia-clinica/services/patient-isolation-alerts.service';

const ISOLATION_ALERT_COLUMNS = ['diagnosis', 'types', 'criticality', 'endAlert', 'actions'];
const FILTER_ONGOING = false;

@Component({
	selector: 'app-isolation-alerts-summary-card',
	templateUrl: './isolation-alerts-summary-card.component.html',
	styleUrls: ['./isolation-alerts-summary-card.component.scss']
})
export class IsolationAlertsSummaryCardComponent implements OnInit {

	readonly ISOLATION_ALERT_HEADER = ISOLATION_ALERT_HEADER;
	readonly displayedColumns = ISOLATION_ALERT_COLUMNS;
	activeStatusId: number;
	_patientId: number;
	@Input() set patientId(patientId: number) {
		this._patientId = patientId;
		this.loadPatientIsolationAlerts();
	};

	constructor(
		readonly patientIsolationAlertService: PatientIsolationAlertsService,
		private readonly isolationAlertMasterDataService: IsolationAlertMasterDataService,
	) { }

	ngOnInit(): void {
		this.isolationAlertMasterDataService.getStatus().subscribe(isolationAlertStatus =>
			this.activeStatusId = isolationAlertStatus.find(state => state.isActive).id);

		this.patientIsolationAlertService.updatedIsolationAlerts$.subscribe(updatedIsolationAlert => updatedIsolationAlert && this.loadPatientIsolationAlerts());
	}

	loadPatientIsolationAlerts() {
		this.patientIsolationAlertService.loadPatientIsolationAlertsSummary(this._patientId, FILTER_ONGOING);
	}

}

export interface IsolationAlertsSummary {
	id: number
	diagnosis: string;
	types: string[];
	criticality: string;
	endDate: Date;
	status: IsolationAlertStatus;
}

export interface IsolationAlertStatus {
	id: number;
	description: string;
}