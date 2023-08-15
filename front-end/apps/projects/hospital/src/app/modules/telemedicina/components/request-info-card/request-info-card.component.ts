import { Component, Input, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-request-info-card',
	templateUrl: './request-info-card.component.html',
	styleUrls: ['./request-info-card.component.scss']
})
export class RequestInfoCardComponent implements OnInit {

	virtualConsultationsSubscription: Subscription;
	@Input() virtualConsultation: any;

	constructor() { }


	ngOnInit(): void {
	}
}
