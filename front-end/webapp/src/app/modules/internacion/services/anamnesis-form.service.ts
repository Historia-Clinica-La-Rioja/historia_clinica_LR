import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AnamnesisFormService {

	private submittedSource = new Subject<any>();

	submitted = this.submittedSource.asObservable();

	changeSubmitted(nextSubmitted: any) {
		this.submittedSource.next(nextSubmitted);
	}

	constructor() {
	}
}
