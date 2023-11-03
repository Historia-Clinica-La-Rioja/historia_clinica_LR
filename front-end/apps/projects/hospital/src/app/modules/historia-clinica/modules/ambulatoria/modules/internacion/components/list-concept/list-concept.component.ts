import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SnomedConcept } from '../../dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component';

@Component({
	selector: 'app-list-concept',
	templateUrl: './list-concept.component.html',
	styleUrls: ['./list-concept.component.scss']
})
export class ListConceptComponent {
	allChecked = false;
	concepts: SnomedConcept<any>[] = [];

	@Input() set listConcept(concepts: SnomedConcept<any>[]) {
		this.allChecked = concepts.every(t => t.isAdded);
		this.concepts = concepts;
	};
	@Input() title: string = '';
	@Output() listConceptChange = new EventEmitter();

	updateAllChecked(item: any) {
		item.isAdded = !item.isAdded;

		this.allChecked = this.concepts.every(t => t.isAdded);
		this.listConceptChange.emit(this.concepts);

	}

	someComplete(): boolean {
		if (this.concepts == null) {
			return false;
		}
		return this.concepts.filter(t => t.isAdded).length > 0 && !this.allChecked;
	}

	setAll(completed: boolean) {
		this.allChecked = completed;
		if (this.concepts == null) {
			return;
		}
		this.concepts.forEach(t => (t.isAdded = completed));
		this.concepts.forEach(item => {
			item.isAdded = completed
		});
		this.listConceptChange.emit(this.concepts);

	}
}
