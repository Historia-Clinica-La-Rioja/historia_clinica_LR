import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ShowEntryCallService {

	private emitter = new Subject();
	$newCall = this.emitter.asObservable();

	constructor() { }

	show(entryCall) {
		this.emitter.next(entryCall);
	}

	/* Este sirve por si desde stomp nos dicen que cortaron la llamada */
	close() {
		this.emitter.next(null)
	}
}
