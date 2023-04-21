import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class NewEmergencyCareEvolutionNoteService {

	readonly newEvolutionNoteEmitter = new Subject();
	new$ = this.newEvolutionNoteEmitter.asObservable()

	constructor() { }

	newEvolutionNote() {
		this.newEvolutionNoteEmitter.next(true);
	}
}
