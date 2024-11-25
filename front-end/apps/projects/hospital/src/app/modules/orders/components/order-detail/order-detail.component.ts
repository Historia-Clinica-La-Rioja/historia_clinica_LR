import { Component, Input } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-order-detail',
	templateUrl: './order-detail.component.html',
	styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent {
	colorRed = Color.RED;
	colorGrey = Color.GREY;
	identiferCases = IDENTIFIER_CASES;
	@Input() orderDetails: OrderDetails;
}


export interface OrderDetails {

	status: string;
	sourceTypeId: string;
	EStudyType: string;
	snomed: string[];
	requiresTransfer: boolean;
	date: DateTimeDto;
}
