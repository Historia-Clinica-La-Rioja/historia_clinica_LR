import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-item-summary',
	templateUrl: './item-summary.component.html',
	styleUrls: ['./item-summary.component.scss']
})
export class ItemSummaryComponent  {

	@Input() data: ItemSummary;
	@Input() size = Size.SMALL;

	constructor() { }

}

export interface ItemSummary {
	title: string;
	subtitle?: string;
	subtitle2?: string;
	avatar?: string;
}

export enum Size {
	SMALL = 'small',
	BIG = 'big'
}
