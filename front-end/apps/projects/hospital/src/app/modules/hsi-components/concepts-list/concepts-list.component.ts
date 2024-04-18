import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';

export interface ConceptsList {
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
  standalone: true,
  imports: [PresentationModule]
})
export class ConceptsListComponent {

	@Input() content: ConceptsList;
	@Input() hasConcepts: boolean;
	@Output() openEmit = new EventEmitter<{addPressed: boolean, checkboxSelected: boolean}>();

	checkboxOn: boolean = false;

	toggleCheckbox = () => {
		this.checkboxOn = !this.checkboxOn;
		this.openEmit.emit({addPressed: false, checkboxSelected: this.checkboxOn});
	}

	open = () => {
		this.openEmit.emit({addPressed: true, checkboxSelected: false});
	}
}