import { Component, Input } from '@angular/core';
import { EVirtualConsultationStatus, VirtualConsultationDto } from '@api-rest/api-model';
import { Priority } from '@presentation/components/priority/priority.component';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-request-info-card',
	templateUrl: './request-info-card.component.html',
	styleUrls: ['./request-info-card.component.scss']
})
export class RequestInfoCardComponent {
	virtualConsultationsSubscription: Subscription;
	virtualConsultation: VirtualConsultationCustom;
	@Input() set setVirtualConsultation(virtualConsultation: VirtualConsultationCustom) {
		this.virtualConsultation = virtualConsultation;
		if (!virtualConsultation.problem)
			this.virtualConsultation.problem = 'Sin información';
	}
	statusPending = EVirtualConsultationStatus.PENDING;

	constructor() { }
}
 export interface VirtualConsultationCustom extends VirtualConsultationDto {
	statusLabel: {description:string,color:string},
	priorityLabel: Priority,
	waitingTime: string,
 }