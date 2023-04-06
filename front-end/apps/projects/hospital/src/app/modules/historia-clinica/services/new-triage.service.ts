import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class NewTriageService {

	readonly newTriageEmitter = new Subject();
	newTriage$ = this.newTriageEmitter.asObservable()

	constructor() { }

	newTriage() {
		this.newTriageEmitter.next(true);
	}
}
