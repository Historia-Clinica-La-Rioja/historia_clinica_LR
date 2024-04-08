import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DetailedInformation } from '../detailed-information/detailed-information.component';

const INPUT_NODE_NAME: string = "INPUT";

@Component({
  selector: 'app-selectable-card',
  templateUrl: './selectable-card.component.html',
  styleUrls: ['./selectable-card.component.scss']
})
export class SelectableCardComponent {

	@Input() dataList: ItemListCard[];
	@Input() enableCheckboxes: boolean = true;
	@Input() noDataMessage: string = 'presentation.selectable-card.NO_DATA'
	@Input() isDownloadable: boolean = true;
	@Input() detailedInformation: DetailedInformation;

	@Output() selectedDataList: EventEmitter<number[]> = new EventEmitter();
	@Output() downloadId: EventEmitter<number> = new EventEmitter();
	@Output() selectedId: EventEmitter<number> = new EventEmitter();

	private isAllSelected: boolean = false;
	private selectedIds: number[] = [];

	constructor() {}

	selectAll() {
		if (this.isAllSelected) {
			this.selectedIds = [];
			this.toggleAllCheckboxes(false);
		} else {
			this.selectedIds = this.dataList.map((data) => data.id);
			this.toggleAllCheckboxes(true);
		}
		this.selectedDataList.emit(this.selectedIds);
		this.isAllSelected = !this.isAllSelected;
	}

	checkboxSelect(id: number) {
		(!this.selectedIds.includes(id))    
		? this.selectedIds.push(id)
		: this.selectedIds.splice(this.selectedIds.indexOf(id), 1); 
		
		const item: ItemListCard = this.dataList.find(item => item.id === id);
		item.checked = !item.checked;
		this.selectedDataList.emit(this.selectedIds);
	}
	
	download(id: number) {
		this.downloadId.emit(id);
	}

	seeDetails(id: number, event: any) {
		(event.target.nodeName === INPUT_NODE_NAME)
			? event.stopPropagation()
			: this.selectedId.emit(id);
	}

	private toggleAllCheckboxes(value: boolean) {
		this.dataList.forEach((item: ItemListCard) => item.checked = value);
	}
}

export interface ItemListCard {
	id: number,
	icon: string,
	title: string,
	options: ItemListOption[],
	checked?: boolean
}

export interface ItemListOption {
	title: string,
	value: string[],
}
