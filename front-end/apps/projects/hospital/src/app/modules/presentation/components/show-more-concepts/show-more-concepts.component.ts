import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-show-more-concepts',
	templateUrl: './show-more-concepts.component.html',
	styleUrls: ['./show-more-concepts.component.scss']
})
export class ShowMoreConceptsComponent {
	othersConcepts: string = '';
	conceptsAll: string[] = [];

	@Input() set concepts(conceptsAll: string[]) {
	  this.conceptsAll = this.capitalizeWordArray(conceptsAll);
	  this.othersConcepts = this.conceptsAll.slice(1).join(", ");
	}

	private capitalizeWordArray(value: string[]): string[] {
	  return value.map(word => word.charAt(0).toUpperCase() + word.slice(1));
	}
  }
