import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PharmacoSummaryDto } from '@api-rest/api-model';

@Component({
	selector: 'app-card-pharmaco',
	templateUrl: './card-pharmaco.component.html',
	styleUrls: ['./card-pharmaco.component.scss']
})
export class CardPharmacoComponent {
	@Input() pharmacos: [];
	@Output() selectionChange = new EventEmitter<PharmacoSummaryDto>();

	emit(pharmaco: PharmacoSummaryDto) {
		return this.selectionChange.emit(pharmaco);
	}
}
