import { Component, Input } from '@angular/core';
import { EVirtualConsultationStatus } from '@api-rest/api-model';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-request-info-card',
	templateUrl: './request-info-card.component.html',
	styleUrls: ['./request-info-card.component.scss']
})
export class RequestInfoCardComponent {
	virtualConsultationsSubscription: Subscription;
	virtualConsultation: any;
	@Input() set setVirtualConsultation(virtualConsultation: any) {
		this.virtualConsultation = virtualConsultation;
		if (!virtualConsultation.problem)
			this.virtualConsultation.problem = 'Sin información';
	}
	statusPending = EVirtualConsultationStatus.PENDING;

	constructor() { }
}
