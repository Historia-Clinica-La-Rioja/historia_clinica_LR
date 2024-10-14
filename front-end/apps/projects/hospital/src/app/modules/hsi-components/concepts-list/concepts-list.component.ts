import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';

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
	standalone: true,
	imports: [PresentationModule]
})
export class ConceptsListComponent implements OnInit {

	@Input() content: ConceptsList;
	@Input() hasConcepts: boolean;
	@Input() initialCheckboxState?: boolean;
	@Output() openEmit = new EventEmitter<{ addPressed: boolean, checkboxSelected: boolean }>();

	checkboxOn: boolean = false;

	toggleCheckbox = () => {
		this.checkboxOn = !this.checkboxOn;
		this.setOpenEmit(false, this.checkboxOn);
	}

	open = () => {
		this.setOpenEmit(true, false);
	}

	ngOnInit() {
		if (this.initialCheckboxState) {
			this.checkboxOn = this.initialCheckboxState;
			this.setOpenEmit(false, this.checkboxOn);
		}
	}

	private setOpenEmit(addPressed: boolean, checkboxSelected: boolean) {
		this.openEmit.emit({ addPressed: addPressed, checkboxSelected: checkboxSelected });
	}
}
