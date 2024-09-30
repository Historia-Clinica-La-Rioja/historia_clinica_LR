import { Component, Input } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';

@Component({
	selector: 'app-order-detail',
	templateUrl: './order-detail.component.html',
	styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent {
	@Input() orderDetails: OrderDetails;
}


export interface OrderDetails {

	status: string;
	sourceTypeId: string;
	EStudyType: string;
	snomedPt: string;
	requiresTransfer: boolean;
	date: DateTimeDto;
}
