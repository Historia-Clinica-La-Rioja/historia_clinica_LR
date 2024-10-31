import { Injectable } from '@angular/core';
import { IsolationAlertService } from '@api-rest/services/isolation-alert.service';
import { IsolationAlertsSummary } from '@historia-clinica/components/isolation-alerts-summary-card/isolation-alerts-summary-card.component';
import { mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary } from '@historia-clinica/mappers/isolation-alerts.mapper';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PatientIsolationAlertsService {

	isolationAlertsSummary: IsolationAlertsSummary[] = [];

	private patientIsolationAlertsSubject = new Subject<IsolationAlertsSummary[]>();
	patientIsolationAlerts$ = this.patientIsolationAlertsSubject.asObservable();

	constructor(
		private readonly isolationAlertService: IsolationAlertService,
	) { }

	loadPatientIsolationAlerts(patientId: number) {
		this.isolationAlertService.getIsolationAlerts(patientId).subscribe(isolationAlerts => {
			const isolationAlertsSummary = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary(isolationAlerts);
			this.patientIsolationAlertsSubject.next(isolationAlertsSummary);
		});

	}

	newIsolationAlert(patientId: number) {
		this.loadPatientIsolationAlerts(patientId);
	}
}
