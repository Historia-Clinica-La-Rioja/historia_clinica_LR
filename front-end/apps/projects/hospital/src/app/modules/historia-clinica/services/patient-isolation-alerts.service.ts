import { Injectable } from '@angular/core';
import { PatientCurrentIsolationAlertDto } from '@api-rest/api-model';
import { IsolationAlertService } from '@api-rest/services/isolation-alert.service';
import { IsolationAlertsSummary } from '@historia-clinica/components/isolation-alerts-summary-card/isolation-alerts-summary-card.component';
import { mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary } from '@historia-clinica/mappers/isolation-alerts.mapper';
import { Subject, Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PatientIsolationAlertsService {

	isolationAlertsSummary: IsolationAlertsSummary[] = [];

	updatedIsolationAlertsSubject = new Subject<boolean>();
	updatedIsolationAlerts$ =  this.updatedIsolationAlertsSubject.asObservable();

	private patientIsolationAlertsSubject = new Subject<IsolationAlertsSummary[]>();
	patientIsolationAlerts$ = this.patientIsolationAlertsSubject.asObservable();

	constructor(
		private readonly isolationAlertService: IsolationAlertService,
	) { }

	loadPatientIsolationAlertsSummary(patientId: number, onlyActive: boolean) {
		this.getPatientIsolationAlerts(patientId, onlyActive).subscribe(isolationAlerts => {
			const isolationAlertsSummary = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary(isolationAlerts);
			this.patientIsolationAlertsSubject.next(isolationAlertsSummary);
		});
	}

	getPatientIsolationAlerts(patientId: number, onlyActive: boolean): Observable<PatientCurrentIsolationAlertDto[]> {
		return this.isolationAlertService.getIsolationAlerts(patientId, onlyActive);
	}

}
