import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedSnomedDto, SnomedECL, SnomedSearchItemDto, SnomedTemplateDto } from "@api-rest/api-model";
import { UntypedFormControl } from "@angular/forms";
import { forkJoin, Observable, of } from "rxjs";
import { SnowstormService } from "@api-rest/services/snowstorm.service";
import { debounceTime, distinctUntilChanged, map, mergeMap, startWith } from "rxjs/operators";
import { ContextService } from "@core/services/context.service";
import { MostFrequentConceptsService } from '@api-rest/services/most-frequent-concepts.service';
import { MapperService } from '@historia-clinica/modules/ambulatoria/services/mapper.service';

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
	@Input() hideTemplateOptions = false;
	@Input() clearButton = false;
	@Input() showSelectedOption = false;
	@Output() optionSelected = new EventEmitter<TemplateOrConceptOption>();

	myControl = new UntypedFormControl();
	conceptOptions: TemplateOrConceptOption[];
	templateOptions: TemplateOrConceptOption[];
	mostFrequentStudies: TemplateOrConceptOption[] = [];
	mostFrequentTemplateOptions: TemplateOrConceptOption[] = [];
	initialMostFrequentStudies: TemplateOrConceptOption[] = [];
	opts = [];
	selectedOption: TemplateOrConceptOption;

	initialTemplateOptions: TemplateOrConceptOption[] = [];
	initialOptionsLoaded = false;

	private readonly MIN_SEARCH_LENGTH = 3;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly constextService: ContextService,
		private readonly mostFrequentConceptsService: MostFrequentConceptsService,
		private readonly mapper: MapperService
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
			this.snowstormService.searchTemplates({ ecl: this.ecl, institutionId: this.constextService.institutionId }),
			this.mostFrequentConceptsService.getMostFrequentTemplates()
		]).subscribe(([mostFrequentStudies, templateOptions, mostFrequentTemplates]) => {

			this.mostFrequentStudies = mostFrequentStudies.map((study: SharedSnomedDto) => {
				return { data: this.mapToSnomedSearchItemDto(study), type: TemplateOrConceptType.CONCEPT }
			});
			this.initialMostFrequentStudies = this.mostFrequentStudies


			this.initialTemplateOptions = templateOptions.map(template => {
				return {type: TemplateOrConceptType.TEMPLATE, data: template}
			});
			this.initialOptionsLoaded = true;
			this.myControl.reset();
			this.mapSnomedTemplateDtoToTemplateOrConceptOption(mostFrequentTemplates);
		});
	}

	private mapSnomedTemplateDtoToTemplateOrConceptOption = (mostFrequentTemplates: SnomedTemplateDto[]) => {
		this.mostFrequentTemplateOptions = mostFrequentTemplates.map((template: SnomedTemplateDto) => this.mapper.toTemplateOrConceptOption(template));
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
			  term: input.pt,
			  lang: " ",
			}
		  }
	}

	handleOptionSelected(event) {
		this.selectedOption = event.option?.value || event;
		if (this.selectedOption.type === TemplateOrConceptType.CONCEPT) {
			this.myControl.setValue(this.selectedOption.data.pt.term);
		} else if (this.selectedOption.type === TemplateOrConceptType.TEMPLATE) {
			this.myControl.setValue(this.selectedOption.data.description);
		}

		if (!this.showSelectedOption)
			this.myControl.reset();

		this.optionSelected.emit(this.selectedOption);
	}

	clear() {
		this.selectedOption = null;
		this.myControl.reset();
		this.optionSelected.emit(this.selectedOption);
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
