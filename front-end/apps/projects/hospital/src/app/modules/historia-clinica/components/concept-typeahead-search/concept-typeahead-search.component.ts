import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl } from "@angular/forms";
import { Observable, of } from "rxjs";
import { debounceTime, distinctUntilChanged, mergeMap, startWith } from "rxjs/operators";
import { SnowstormService } from "@api-rest/services/snowstorm.service";
import { SnomedECL, SnomedDto } from "@api-rest/api-model";

@Component({
	selector: 'app-concept-typeahead-search',
	templateUrl: './concept-typeahead-search.component.html',
	styleUrls: ['./concept-typeahead-search.component.scss']
})
export class ConceptTypeaheadSearchComponent {

	@Input() ecl: SnomedECL;
	@Input() placeholder = '';
	@Input() debounceTime = 300;
	@Input() appearanceOutline = false;
	@Input() showSearchIcon = false;
	@Output() conceptSelected = new EventEmitter<SnomedDto>();

	myControl = new FormControl();
	filteredOptions: Observable<any[]>;
	opts: SnomedDto[] = [];

	private readonly MIN_SEARCH_LENGTH = 3;

	constructor(private readonly snowstormService: SnowstormService) {
		this.filteredOptions = this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.filter(searchValue || '')
			})
		)
	}

	private filter(searchValue: string): Observable<SnomedDto[]> {
		return this.searchConcepts(searchValue)
	}

	private searchConcepts(searchValue): Observable<SnomedDto[]> {
		if (searchValue.length < this.MIN_SEARCH_LENGTH) return of(this.opts);
		return this.opts.length ?
			of(this.opts) :
			this.snowstormService.searchSNOMEDConcepts({ term: searchValue, ecl: this.ecl });
	}

	getDisplayName(option): string {
		return option && option.pt.term ? option.pt.term : '';
	}

	handleOptionSelected(event) {
		const selectedOption = event.option.value;
		const snomedConcept: SnomedDto = {
			sctid: selectedOption.conceptId,
			pt: selectedOption.pt.term
		};
		this.conceptSelected.emit(snomedConcept)
	}

}

