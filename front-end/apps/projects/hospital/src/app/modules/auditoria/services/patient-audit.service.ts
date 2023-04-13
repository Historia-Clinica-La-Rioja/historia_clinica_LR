import { Injectable } from '@angular/core';
import { DuplicatePatientDto } from '@api-rest/api-model';
import { Subject, BehaviorSubject } from 'rxjs';
import { Filters } from '../routes/control-patient-duplicate/control-patient-duplicate.component';

@Injectable()
export class PatientAuditService {
	patientToAuditSubject: Subject<any> = new BehaviorSubject<any>([]);
	readonly patientToAudit$ = this.patientToAuditSubject.asObservable();

	filterBySubject: Subject<any> = new BehaviorSubject<any>([]);
	readonly filterBySubject$ = this.filterBySubject.asObservable();

	constructor() {

	}

	setPatientToAudit(patientToAudit: DuplicatePatientDto) {
		this.patientToAuditSubject.next(patientToAudit);
	}

	setFilter(filter: Filters) {
		this.filterBySubject.next(filter);
	}
}
