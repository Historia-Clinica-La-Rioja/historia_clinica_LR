import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SnomedECL } from "@api-rest/api-model";
import { FormControl } from "@angular/forms";
import { Observable, of } from "rxjs";
import { SnowstormService } from "@api-rest/services/snowstorm.service";
import { debounceTime, distinctUntilChanged, map, mergeMap, startWith } from "rxjs/operators";
import { ContextService } from "@core/services/context.service";

@Component({
  selector: 'app-template-concept-typeahead-search',
  templateUrl: './template-concept-typeahead-search.component.html',
  styleUrls: ['./template-concept-typeahead-search.component.scss']
})
export class TemplateConceptTypeaheadSearchComponent implements OnInit {

	@Input() ecl: SnomedECL;
	@Input() placeholder = '';
	@Input() debounceTime = 300;
	@Output() optionSelected = new EventEmitter<TemplateOrConceptOption>();

	myControl = new FormControl();
	conceptOptions: TemplateOrConceptOption[];
	templateOptions: TemplateOrConceptOption[];
	opts = [];

	initialTemplateOptions: TemplateOrConceptOption[] = [];
	initialOptionsLoaded = false;

	private readonly MIN_SEARCH_LENGTH = 3;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly constextService: ContextService,
	) {
		this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.searchConcepts(searchValue || '')
			})
		).subscribe(data => {
			this.conceptOptions = data;
		});
		this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.searchTemplates(searchValue || '')
			})
		).subscribe(data => {
			this.templateOptions = data;
		});
	}

	ngOnInit(): void {
		this.snowstormService.searchTemplates({ecl: this.ecl, institutionId: this.constextService.institutionId}).subscribe(res => {
			this.initialTemplateOptions = res.map(template => {
				return {type: TemplateOrConceptType.TEMPLATE, data: template}
			});
			this.initialOptionsLoaded = true;
			this.myControl.reset();
		});
	}

	private searchConcepts(searchValue):  Observable<TemplateOrConceptOption[]> {
		if (searchValue.length < this.MIN_SEARCH_LENGTH) {
			return of(this.opts);
		}
		return this.mapToOption(
			this.snowstormService.searchSNOMEDConcepts({ term: searchValue, ecl: this.ecl }),
			TemplateOrConceptType.CONCEPT
		);
	}

	private searchTemplates(searchValue): Observable<TemplateOrConceptOption[]> {
		if (searchValue.length < this.MIN_SEARCH_LENGTH) {
			return of(this.initialTemplateOptions);
		}
		return this.mapToOption(
			this.snowstormService.searchTemplates({term: searchValue, ecl: this.ecl, institutionId: this.constextService.institutionId}),
			TemplateOrConceptType.TEMPLATE
		);

	}

	private mapToOption(observableArray: Observable<any[]>, type: TemplateOrConceptType) {
		return observableArray
			.pipe(
				map(array => {
						return array.map((object) => {
							return {
								type: type,
								data: object
							};
						});
					}
				)
			);
	}

	getDisplayName(option: TemplateOrConceptOption): string {
		if (!option) return '';
		return (option.type === TemplateOrConceptType.TEMPLATE) ? option.data.description : option.data.pt.term;
	}

	handleOptionSelected(event) {
		const option: TemplateOrConceptOption = event.option.value;
		this.optionSelected.emit(option);
	}
}

export enum TemplateOrConceptType {
	TEMPLATE,
	CONCEPT
}

export interface TemplateOrConceptOption {
	type: TemplateOrConceptType;
	data: any;
}
