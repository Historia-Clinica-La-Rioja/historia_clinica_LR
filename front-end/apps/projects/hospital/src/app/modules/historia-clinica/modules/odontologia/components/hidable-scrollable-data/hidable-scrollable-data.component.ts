import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-hidable-scrollable-data',
	templateUrl: './hidable-scrollable-data.component.html',
	styleUrls: ['./hidable-scrollable-data.component.scss']
})
export class HidableScrollableDataComponent {

	constructor() { }

	showingMore = false;

	@Input() data: ScrollableData[]

}


export interface ScrollableData {
	firstElement: string;
	secondElement: string;
}
