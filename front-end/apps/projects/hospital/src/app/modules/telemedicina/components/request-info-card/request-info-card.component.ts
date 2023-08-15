import { Component, Input } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-request-info-card',
	templateUrl: './request-info-card.component.html',
	styleUrls: ['./request-info-card.component.scss']
})
export class RequestInfoCardComponent {

	virtualConsultationsSubscription: Subscription;
	@Input() virtualConsultation: any;

	constructor() { }
}
