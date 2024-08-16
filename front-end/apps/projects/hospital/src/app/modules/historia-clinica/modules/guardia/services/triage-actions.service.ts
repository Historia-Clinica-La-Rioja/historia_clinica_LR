import { Injectable } from '@angular/core';
import { TriageAdministrativeDto, TriageAdultGynecologicalDto, TriagePediatricDto } from '@api-rest/api-model';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class TriageActionsService {
	
	private readonly verifyFormValidation = new Subject<void>();
	readonly persist = new Subject<void>();
	pediatricTriage: TriagePediatricDto;
	triageAdultGynecological: TriageAdultGynecologicalDto;
	triageAdministrative: TriageAdministrativeDto;
	verifyFormValidation$ = this.verifyFormValidation.asObservable();
	persist$ = this.persist.asObservable();

	constructor() { }

	saveTriage() {
		this.verifyFormValidation.next();
	}

}
