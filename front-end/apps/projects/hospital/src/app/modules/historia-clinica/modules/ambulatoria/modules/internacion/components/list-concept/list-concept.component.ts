import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SnomedConcept } from '../../dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component';

@Component({
	selector: 'app-list-concept',
	templateUrl: './list-concept.component.html',
	styleUrls: ['./list-concept.component.scss']
})
export class ListConceptComponent {

	checked = false;
	@Input() listConcept: SnomedConcept<any>[] = [];
	@Input() title: string = '';
	@Output() listConceptChange = new EventEmitter();

	constructor() {
		this.checked = this.listConcept.every(i => i.isAdded);
	}

	toggleItemSelection(item: any) {
		item.isAdded = !item.isAdded;
		this.listConceptChange.emit(this.listConcept);
	}

	toggleAllSelection(event: any) {
		this.listConcept.forEach(item => {
			item.isAdded = event.checked
		});
		this.listConceptChange.emit(this.listConcept);
	}
}
