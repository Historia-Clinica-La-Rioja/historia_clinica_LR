import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Pharmaco } from '../../dialogs/pharmaco/pharmaco.component';

@Component({
	selector: 'app-card-pharmaco',
	templateUrl: './card-pharmaco.component.html',
	styleUrls: ['./card-pharmaco.component.scss']
})
export class CardPharmacoComponent {
	@Input() pharmacos: [];
	@Output() selectionChange = new EventEmitter<PharmacoSummaryDto>();

	emit(pharmaco: Pharmaco) {
		return this.selectionChange.emit(pharmaco);
	}
}
