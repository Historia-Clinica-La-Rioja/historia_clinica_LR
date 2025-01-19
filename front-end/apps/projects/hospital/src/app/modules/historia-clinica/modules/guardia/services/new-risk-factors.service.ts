import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class NewRiskFactorsService {

	private readonly newRiskFactorsEmitter = new Subject<boolean>();
	newRiskFactors$ = this.newRiskFactorsEmitter.asObservable()

	constructor() { }

	newRiskFactors() {
		this.newRiskFactorsEmitter.next(true);
	}
}
