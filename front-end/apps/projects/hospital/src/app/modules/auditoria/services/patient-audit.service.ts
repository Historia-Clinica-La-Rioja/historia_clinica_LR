import { EventEmitter, Injectable, OnDestroy, Output } from '@angular/core';
import { DuplicatePatientDto } from '@api-rest/api-model';
import { Subject, BehaviorSubject } from 'rxjs';

@Injectable()
export class PatientAuditService {
	patientToAuditSubject: Subject<any> = new BehaviorSubject<any>([]);
	readonly patientToAudit$ = this.patientToAuditSubject.asObservable();

	constructor() {

	}

	setPatientToAudit(patientToAudit:DuplicatePatientDto){
		this.patientToAuditSubject.next(patientToAudit);
	}
}
