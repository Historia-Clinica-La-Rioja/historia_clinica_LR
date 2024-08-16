import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { Observable, of } from "rxjs";
import { debounceTime, distinctUntilChanged, mergeMap, startWith } from "rxjs/operators";
import { SnowstormService } from "@api-rest/services/snowstorm.service";
import { SnomedECL, SnomedDto, SnomedSearchItemDto } from "@api-rest/api-model";
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-concept-typeahead-search',
	templateUrl: './concept-typeahead-search.component.html',
	styleUrls: ['./concept-typeahead-search.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class ConceptTypeaheadSearchComponent implements OnInit {

	snomedConcept: SnomedDto;
	inputFocused = false;
	@Input() ecl: SnomedECL;
	@Input() placeholder = '';
	@Input() debounceTime = 300;
	@Input() appearanceOutline = false;
	@Input() enableSubmitButton = false;
	@Input() clearButton = false;
	@Input() buttonMessage = '';
	@Input() showSearchIcon = false;
	@Input() preload: SnomedDto = null;
	@Input() required = false;

	@Output() conceptSelected = new EventEmitter<SnomedDto>();

	myControl = new UntypedFormControl();
	filteredOptions: Observable<any[]>;
	opts: SnomedDto[] = [];
	formGroup: UntypedFormGroup;

	private readonly MIN_SEARCH_LENGTH = 3;

	constructor(private readonly snowstormService: SnowstormService) {
		this.filteredOptions = this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.filter(searchValue || '')
			})
		);

		this.formGroup = new UntypedFormGroup({
			myControl: this.myControl
		});

		if (this.required) {
			this.myControl.setValidators([Validators.required]);
		}
	}

	ngOnInit() {
		this.setPreload();
	}

	private filter(searchValue: string): Observable<SnomedDto[]> {
		return this.searchConcepts(searchValue);
	}

	private searchConcepts(searchValue: string): Observable<SnomedDto[]> {
		if (searchValue.length < this.MIN_SEARCH_LENGTH) return of(this.opts);
		return this.opts.length ?
			of(this.opts) :
			this.snowstormService.searchSNOMEDConcepts({ term: searchValue, ecl: this.ecl });
	}

	getDisplayName(option: SnomedSearchItemDto): string {
		return option && option.pt?.term ? option.pt?.term : '';
	}

	handleOptionSelected(event: any) {
		const selectedOption = event.option?.value;
		if (selectedOption) {
			this.snomedConcept = {
				sctid: selectedOption.conceptId,
				pt: selectedOption.pt.term
			};
			if (!this.enableSubmitButton) {
				this.conceptSelected.emit(this.snomedConcept);
			}
		}
	}

	emitButton() {
		this.conceptSelected.emit(this.snomedConcept);
		this.clear();
	}

	clear() {
		this.snomedConcept = null;
		this.myControl.reset();
		this.conceptSelected.emit(this.snomedConcept);
	}

	private setPreload() {
		if (this.preload) {
			let concept = {
				conceptId: "",
				id: "",
				fsn: {
					term: this.preload.pt,
					lang: ""
				},
				pt: {
					term: this.preload.pt,
					lang: ""
				}
			}
			this.snomedConcept = {
				sctid: this.preload.sctid,
				pt: this.preload.pt
			};
			this.myControl.setValue(concept);
			this.conceptSelected.emit(this.snomedConcept);
		}
	}
}
