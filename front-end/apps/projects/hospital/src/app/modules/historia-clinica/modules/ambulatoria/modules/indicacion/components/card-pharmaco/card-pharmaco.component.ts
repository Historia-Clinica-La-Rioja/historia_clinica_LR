import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MasterDataDto } from '@api-rest/api-model';

@Component({
	selector: 'app-card-pharmaco',
	templateUrl: './card-pharmaco.component.html',
	styleUrls: ['./card-pharmaco.component.scss']
})
export class CardPharmacoComponent<T> {
	@Input() pharmacos: [];
	@Input() vias: MasterDataDto[] = [];
	@Output() selectionChange = new EventEmitter<T>();

	emit(pharmaco: T) {
		return this.selectionChange.emit(pharmaco);
	}
}
