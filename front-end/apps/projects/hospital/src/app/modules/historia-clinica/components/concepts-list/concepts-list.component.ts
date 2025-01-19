import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface ConceptsList {
	id: string,
	header: {
		text: string;
		icon: string;
	},
	titleList: string;
	actions: {
		button: string;
		checkbox?: string;
	}
}

@Component({
	selector: 'app-concepts-list',
	templateUrl: './concepts-list.component.html',
	styleUrls: ['./concepts-list.component.scss'],
})
export class ConceptsListComponent {

	@Input() content: ConceptsList;
	@Input() hasConcepts: boolean;
	@Input() 
	set initialCheckboxState(initialCheckboxState: boolean) {
		if (initialCheckboxState) {
			this.checkboxOn = initialCheckboxState;
			this.setOpenEmit(false, this.checkboxOn);
		}
	};
	@Input() isEmpty = true;
	@Output() openEmit = new EventEmitter<{ addPressed: boolean, checkboxSelected: boolean }>();

	checkboxOn: boolean = false;

	toggleCheckbox = () => {
		this.checkboxOn = !this.checkboxOn;
		this.setOpenEmit(false, this.checkboxOn);
	}

	open = () => {
		this.setOpenEmit(true, false);
	}

	private setOpenEmit(addPressed: boolean, checkboxSelected: boolean) {
		this.openEmit.emit({ addPressed: addPressed, checkboxSelected: checkboxSelected });
	}
}
