import { Injectable } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { StompService } from '../../stomp.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { Message } from '@stomp/stompjs';
import { VirtualConsultationNotificationDataDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class EntryCallStompService {

	entryCall$: Observable<VirtualConsultationNotificationDataDto>
		= this.stompService.watch('/user/queue/virtual-consultation-notification').
			pipe(
				switchMap((virtualConsultationId: Message) => this.virtualConstultationService.getVirtualConsultationCall(Number(virtualConsultationId.body)))
			)

	constructor(
		private stompService: StompService,
		private virtualConstultationService: VirtualConstultationService
	) { }

}

