import { Injectable } from '@angular/core';
import { PatientIsolationAlertsService } from './patient-isolation-alerts.service';
import { IsolationAlertDetail } from '@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component';
import { Subject } from 'rxjs';
import { mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail } from '@historia-clinica/mappers/isolation-alerts.mapper';

const FILTER_ONGOING = true;

@Injectable()
export class IsolationAlertHeaderService {

	private patientIsolationAlertsDetailsSubject = new Subject<IsolationAlertDetail[]>();
	patientIsolationAlertsDetails$ = this.patientIsolationAlertsDetailsSubject.asObservable();
	patientId: number;

	constructor(
		private readonly patientIsolationAlertService: PatientIsolationAlertsService,
	) {
		this.patientIsolationAlertService.updatedIsolationAlerts$.subscribe(updatedIsolationAlerts => updatedIsolationAlerts && this.loadPatientIsolationAlertHeader())
	}

	loadPatientIsolationAlertHeader() {
		this.patientId && this.patientIsolationAlertService.getPatientIsolationAlerts(this.patientId, FILTER_ONGOING).subscribe(isolationAlerts => {
			const isolationAlertsDetail = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail(isolationAlerts);
			this.patientIsolationAlertsDetailsSubject.next(isolationAlertsDetail);
		});
	}

}
