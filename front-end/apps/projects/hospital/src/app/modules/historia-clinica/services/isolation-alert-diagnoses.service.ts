import { Injectable } from '@angular/core';
import { ClinicalTermDto } from '@api-rest/api-model';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class IsolationAlertDiagnosesService {

	isolationAlertDiagnosisSubject = new Subject<ClinicalTermDto[]>;
	isolationAlertDiagnisis$ = this.isolationAlertDiagnosisSubject.asObservable();
	constructor() { }


	setIsolationAlertDiagnosis(diagnoses: ClinicalTermDto[]) {
		this.isolationAlertDiagnosisSubject.next(diagnoses);
	}

}
