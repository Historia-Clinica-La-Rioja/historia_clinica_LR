import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SharedSnomedDto } from '@api-rest/api-model';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
	selector: 'app-typeahead-practices',
	templateUrl: './typeahead-practices.component.html',
	styleUrls: ['./typeahead-practices.component.scss']
})
export class TypeaheadPracticesComponent {

	practicesTypeahead: TypeaheadOption<SharedSnomedDto>[];
	showError = false;
	@Input()
	set practices(list: SharedSnomedDto[]) {
		if (list?.length)
			this.practicesTypeahead = this.toTypeaheadOptionList(list);
	}
	@Output() selectedOption = new EventEmitter<SharedSnomedDto>();

	constructor() { }

	setPractice(practice: SharedSnomedDto) {
		this.selectedOption.emit(practice);
	}

	private toTypeaheadOptionList(practices: SharedSnomedDto[]): TypeaheadOption<SharedSnomedDto>[] {
		return practices.map(toTypeaheadOption);

		function toTypeaheadOption(practice: SharedSnomedDto): TypeaheadOption<SharedSnomedDto> {
			return {
				compareValue: practice.pt,
				value: practice
			};
		}
	}

}
