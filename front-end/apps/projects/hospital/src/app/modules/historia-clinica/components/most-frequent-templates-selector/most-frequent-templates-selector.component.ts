import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TemplateOrConceptOption } from '../template-concept-typeahead-search/template-concept-typeahead-search.component';

@Component({
	selector: 'app-most-frequent-templates-selector',
	templateUrl: './most-frequent-templates-selector.component.html',
	styleUrls: ['./most-frequent-templates-selector.component.scss']
})
export class MostFrequentTemplatesSelectorComponent {

	@Input() mostFrequentTemplateOptions: TemplateOrConceptOption[] = [];
	@Output() onSelected: EventEmitter<TemplateOrConceptOption> = new EventEmitter<TemplateOrConceptOption>();

	select = ($event) => {
		this.onSelected.emit($event);
	}
}
