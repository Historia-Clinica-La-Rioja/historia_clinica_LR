import { Injectable } from '@angular/core';
import { Observable, filter, map, switchMap } from 'rxjs';
import { StompService } from '../../stomp.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { Message } from '@stomp/stompjs';
import { EVirtualConsultationEvent, VirtualConsultationNotificationDataDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class EntryCallStompService {

	entryCall$: Observable<VirtualConsultationNotificationDataDto>
		= this.stompService.watch(`/user/queue/${callStates.INCOMING_CALL}`).
			pipe(
				switchMap(
					(virtualConsultationId: Message) => {
						const messageDto: VirtualConsultationEventBo = JSON.parse(virtualConsultationId.body);
						return this.virtualConstultationService.getVirtualConsultationCall(messageDto.virtualConsultationId)
					}
				)
			)

	rejectedCall$: Observable<VirtualConsultationNotificationDataDto>
		= this.stompService.watch(`/user/queue/${callStates.CALL_REJECTED}`).
			pipe(
				map(a => JSON.parse(a.body)),
				filter(messageDto => messageDto.event === EVirtualConsultationEvent.CALL_REJECTED),
				switchMap(
					(messageDto: VirtualConsultationEventBo) => {
						return this.virtualConstultationService.getVirtualConsultationCall(messageDto.virtualConsultationId);
					}
				)
			);

	acceptedCall$: Observable<VirtualConsultationNotificationDataDto>
		= this.stompService.watch(`/user/queue/${callStates.CALL_ACCEPTED}`).
			pipe(
				map(a => JSON.parse(a.body)),
				filter(messageDto => messageDto.event === EVirtualConsultationEvent.CALL_ACCEPTED),
				switchMap(
					(messageDto: VirtualConsultationEventBo) => {
						return this.virtualConstultationService.getVirtualConsultationCall(messageDto.virtualConsultationId);
					}
				)
			);

	constructor(
		private stompService: StompService,
		private virtualConstultationService: VirtualConstultationService
	) { }

}

const callStates = {
	[EVirtualConsultationEvent.CALL_ACCEPTED]: 'virtual-consultation-call-response',
	[EVirtualConsultationEvent.CALL_CANCELED]: 'virtual-consultation-call-response',
	[EVirtualConsultationEvent.CALL_REJECTED]: 'virtual-consultation-call-response',
	[EVirtualConsultationEvent.INCOMING_CALL]: 'virtual-consultation-call-request',
}

interface VirtualConsultationEventBo {
	virtualConsultationId: number;
	destinationUserId: number;
	event: EVirtualConsultationEvent
}
