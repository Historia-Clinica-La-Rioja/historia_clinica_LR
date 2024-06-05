import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';
import { SummaryMultipleSignComponent, SummaryMultipleSignData } from '../summary-multiple-sign/summary-multiple-sign.component';

const INPUT_NODE_NAME: string = "INPUT";

@Component({
	selector: 'app-summary-list',
	templateUrl: './summary-list.component.html',
	styleUrls: ['./summary-list.component.scss'],
	standalone: true,
	imports: [PresentationModule, SummaryMultipleSignComponent]
})

export class SummaryListComponent {

	@Input() dataList: SummaryItem[];
	@Input() noDataMessage: string = 'presentation.selectable-card.NO_DATA'
	@Input() enableCheckboxes: boolean = true;

	@Output() checkedIds = new EventEmitter<number[]>();
	@Output() selectedId = new EventEmitter<number>();

	selectedIds: number[] = [];
	isAllSelected: boolean = false;

	constructor() { }

	selectAll(): void {
		if (this.isAllSelected) {
			this.selectedIds = [];
			this.toggleAllCheckboxes(false);
		} else {
			this.selectedIds = this.dataList.map((data) => data.id);
			this.toggleAllCheckboxes(true);
		}
		this.isAllSelected = !this.isAllSelected;
		this.checkedIds.emit(this.selectedIds);
	}

	private toggleAllCheckboxes(value: boolean) {
		this.dataList.forEach((item: SummaryItem) => item.checked = value);
	}

	checkboxSelect(id: number): void {
		(!this.selectedIds.includes(id))
			? this.selectedIds.push(id)
			: this.selectedIds.splice(this.selectedIds.indexOf(id), 1);

		const item: SummaryItem = this.dataList.find(item => item.id === id);
		item.checked = !item.checked;
		this.checkedIds.emit(this.selectedIds);
	}

	handleOptionClick(id: number, event: any): void {
		if (event.target.nodeName !== INPUT_NODE_NAME) {
			this.selectedId.emit(id);
		}
	}

	stopEvent(event: Event): void {
		event.stopPropagation();
	}
}

export interface SummaryItem {
	id: number,
	checked?: boolean
	data?: SummaryMultipleSignData;
}
