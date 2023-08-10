import { Injectable } from '@angular/core';
import { VirtualConsultationNotificationDataDto } from '@api-rest/api-model';
import { Observable, Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ShowEntryCallService {

	private emitter = new Subject<VirtualConsultationNotificationDataDto>();
	$entryCall: Observable<VirtualConsultationNotificationDataDto> = this.emitter.asObservable();

	constructor() { }

	show(entryCall: VirtualConsultationNotificationDataDto) {
		this.emitter.next(entryCall);
	}

	/* Este sirve por si desde stomp nos dicen que cortaron la llamada */
	close() {
		this.emitter.next(null)
	}
}
