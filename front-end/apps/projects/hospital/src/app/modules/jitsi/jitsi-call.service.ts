import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class JitsiCallService {


	private readonly emitter = new Subject<string>();
	readonly $roomId = this.emitter.asObservable();
	readonly userName = '';

	constructor() { }

	open(roomId: string) {
		this.emitter.next(roomId);
	}

	close() {
		this.emitter.next(null)
	}
}
