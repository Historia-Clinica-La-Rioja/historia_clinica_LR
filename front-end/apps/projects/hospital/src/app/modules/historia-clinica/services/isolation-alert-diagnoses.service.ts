import { Injectable } from '@angular/core';
import { DiagnosisDto } from '@api-rest/api-model';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class IsolationAlertDiagnosesService {

	isolationAlertDiagnosesSubject = new Subject<DiagnosisDto[]>;
	isolationAlertDiagnoses$ = this.isolationAlertDiagnosesSubject.asObservable();

	constructor() { }

	setIsolationAlertDiagnosis(diagnoses: DiagnosisDto[]) {
		this.isolationAlertDiagnosesSubject.next(diagnoses);
	}

}
