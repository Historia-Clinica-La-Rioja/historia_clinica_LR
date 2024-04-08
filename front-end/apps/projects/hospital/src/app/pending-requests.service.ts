import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PendingRequestsService {

	private pendingRequests = 0;
	private readonly emitter = new Subject<boolean>();

	arePendingRequests$ = this.emitter.asObservable()
	constructor() { }


	addPendingRequest() {
		this.pendingRequests++;
		this.emitter.next(!!this.pendingRequests);
	}

	removePendingRequest() {
		this.pendingRequests--;
		this.emitter.next(!!this.pendingRequests);
	}
}
