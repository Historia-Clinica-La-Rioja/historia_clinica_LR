import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-filter-button',
  templateUrl: './filter-button.component.html',
  styleUrls: ['./filter-button.component.scss']
})

export class FilterButtonComponent {

	isExpanded: boolean = false;
	@Output() isExpandedEmitter = new EventEmitter<boolean>();

  	constructor() { }

	filter() {
		this.isExpanded = !this.isExpanded;
		this.isExpandedEmitter.emit(this.isExpanded);
	}

}
