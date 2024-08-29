import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedSnomedDto, SnomedECL, SnomedSearchItemDto } from "@api-rest/api-model";
import { UntypedFormControl } from "@angular/forms";
import { forkJoin, Observable, of } from "rxjs";
import { SnowstormService } from "@api-rest/services/snowstorm.service";
import { debounceTime, distinctUntilChanged, map, mergeMap, startWith } from "rxjs/operators";
import { ContextService } from "@core/services/context.service";
import { MostFrequentConceptsService } from '@api-rest/services/most-frequent-concepts.service';
import { capitalize } from '@core/utils/core.utils';

const MAX_ITEMS_DISPLAY = 30;

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

	myControl = new UntypedFormControl();
	conceptOptions: TemplateOrConceptOption[];
	templateOptions: TemplateOrConceptOption[];
	mostFrequentStudies: TemplateOrConceptOption[] = [];
	initialMostFrequentStudies: TemplateOrConceptOption[] = [];
	opts = [];

	initialTemplateOptions: TemplateOrConceptOption[] = [];
	initialOptionsLoaded = false;

	private readonly MIN_SEARCH_LENGTH = 3;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly constextService: ContextService,
		private readonly mostFrequentConceptsService: MostFrequentConceptsService,
	) {

		this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap((value: string) => {
				return this.filterMostFrequent(value || '')
			})

		).subscribe((data: TemplateOrConceptOption[]) => {
			this.mostFrequentStudies = data;
		});

		this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.searchConcepts(searchValue || '')
			})
		).subscribe(data => {
			this.conceptOptions = data.slice(0, MAX_ITEMS_DISPLAY);;
		});
		this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.searchTemplates(searchValue || '')
			})
		).subscribe(data => {
			this.templateOptions = data.slice(0, MAX_ITEMS_DISPLAY);;
		});
	}

	ngOnInit(): void {
		this.setInitialValues();
	}

	private setInitialValues() {
		forkJoin([
			this.mostFrequentConceptsService.getMostFrequentConceptsService(),
			this.snowstormService.searchTemplates({ ecl: this.ecl, institutionId: this.constextService.institutionId })
		]).subscribe(([mostFrequentStudies, templateOptions]) => {

			this.mostFrequentStudies = mostFrequentStudies.map((study: SharedSnomedDto) => {
				return { data: this.mapToSnomedSearchItemDto(study), type: TemplateOrConceptType.CONCEPT }
			});
			this.initialMostFrequentStudies = this.mostFrequentStudies


			this.initialTemplateOptions = templateOptions.map(template => {
				return {type: TemplateOrConceptType.TEMPLATE, data: template}
			});
			this.initialOptionsLoaded = true;
			this.myControl.reset();
		});
	}

	private filterMostFrequent(value: string): Observable<TemplateOrConceptOption[]> {

		const filterValue = value.toLowerCase();
		let values = this.initialMostFrequentStudies;

		const mostFrequentStudiesLeaked = values.filter((option: TemplateOrConceptOption) =>
			option.data.pt.term.toLowerCase().includes(filterValue)

		);

		return of(mostFrequentStudiesLeaked);
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
			this.snowstormService.searchTemplates({ term: searchValue, ecl: this.ecl, institutionId: this.constextService.institutionId }),
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

	private mapToSnomedSearchItemDto(input: SharedSnomedDto): SnomedSearchItemDto {
		return {
			conceptId: input.sctid,
			id: input.sctid,
			fsn: {
			  term: input.parentFsn,
			  lang: " ",
			},
			pt: {
			  term: capitalize(input.pt),
			  lang: " ",
			}
		  }
	}

	handleOptionSelected(event) {
		const option: TemplateOrConceptOption = event.option.value;
		this.optionSelected.emit(option);
		this.myControl.reset();
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
