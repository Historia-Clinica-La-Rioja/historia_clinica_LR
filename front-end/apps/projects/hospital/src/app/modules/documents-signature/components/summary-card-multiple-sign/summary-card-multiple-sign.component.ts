import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';
import { SummaryItem } from '../summary-list-multiple-sign/summary-list-multiple-sign.component';

@Component({
	selector: 'app-summary-card-multiple-sign',
	templateUrl: './summary-card-multiple-sign.component.html',
	styleUrls: ['./summary-card-multiple-sign.component.scss']
})

export class SummaryCardMultipleSignComponent {

	@Input() dataList: SummaryItem[];
	@Input() enableCheckboxes: boolean = true;
	@Input() noDataMessage: string = 'presentation.selectable-card.NO_DATA'
	@Input() detailedInformation: DetailedInformation;
	@Input() hasHeader: boolean = false;

	@Output() checkedIdsOnChange: EventEmitter<number[]> = new EventEmitter();
	@Output() selectedIdOnChange: EventEmitter<number> = new EventEmitter();

	constructor() { }

	selectedId(id: number): void {
		this.selectedIdOnChange.emit(id);
	}

	checkedIds(ids: number[]): void {
		this.checkedIdsOnChange.emit(ids);
	}
}

