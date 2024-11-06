import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AppFeature, SnomedDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PresentationModule } from '@presentation/presentation.module';
import { debounceTime, distinctUntilChanged, mergeMap, Observable, of, startWith, tap } from 'rxjs';

const MIN_SEARCH_LENGTH: number = 3;

@Component({
	selector: 'app-concept-typeahead-search-v2',
	templateUrl: './concept-typeahead-search-v2.component.html',
	styleUrls: ['./concept-typeahead-search-v2.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class ConceptTypeaheadSearchV2Component implements OnInit {

	@Input() preload: SnomedDto = null;
	@Input() conceptTypeaheadInfo: ConceptTypeaheadInfo;

	@Output() conceptSelected = new EventEmitter<SnomedDto>();

	form: FormGroup;
	snomedConcept: SnomedDto;
	inputFocused: boolean = false;
	loading: boolean = false;
	items: SnomedDto[] = [];
	filteredOptions: Observable<SnomedDto[]>;
	inputText: string = '';
	totalResults: number = 0;
	clearButton: boolean = false;
	hintVisibility: boolean = false;
	isCacheEnabled: boolean = false;

	constructor(
		private fb: FormBuilder,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.setFeatureFlags();
		this.verifyPreloadData();
		this.checkClearButtonVisibility();
		this.form = this.fb.group({
			snomedInput: new FormControl('', this.conceptTypeaheadInfo?.required ? Validators.required : null)
		});
		this.checkHintVisibility();
	}

	ngOnInit(): void {
		this.filteredOptions = this.form.controls.snomedInput.valueChanges.pipe(
			startWith(''),
			debounceTime(this.conceptTypeaheadInfo?.debounceTime),
			tap(() => (this.loading = true)),
			distinctUntilChanged(),
			mergeMap(searchValue => this.filter(searchValue).pipe(tap(() => (this.loading = false))))
		);

		this.form.controls.snomedInput.statusChanges.subscribe(() => {
			this.checkClearButtonVisibility();
			this.checkHintVisibility();
		});
	}

	onInputFocus(): void {
		this.inputFocused = true;
		if (this.isCacheEnabled) {
			this.filteredOptions = of(this.items);
		}
		this.checkHintVisibility()
	}

	filter(searchValue: string): Observable<SnomedDto[]> {
		if (this.isCacheEnabled) {
			this.totalResults = this.items.length;
			return of(this.items);
		} else if (searchValue && searchValue.length >= MIN_SEARCH_LENGTH) {
			const filterValue = searchValue.toLowerCase();
			const matchingItems = this.items.filter(item =>
				item.parentFsn?.toLowerCase().startsWith(filterValue)
			);
			this.totalResults = matchingItems.length;
			const limitedItems = matchingItems.slice(0, this.conceptTypeaheadInfo?.maxSearchResults);
			return of(limitedItems);
		}
		this.totalResults = 0;
		return of([]);
	}

	onOptionSelected(selectedOption: string): void {
		const selectedItem = this.items.find(item => item.parentFsn === selectedOption);
		if (selectedItem) {
			this.snomedConcept = selectedItem
			this.form.controls.snomedInput?.setValue(selectedItem.parentFsn)
		}
	}

	clearInput(): void {
		this.snomedConcept = null;
		this.form.reset();
		this.checkClearButtonVisibility();
		this.checkHintVisibility();
	}

	private verifyPreloadData() {
		if (this.preload) {
			this.setPreload()
		}
	}

	private setPreload() {
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
		this.form.setValue(concept);
		this.conceptSelected.emit(this.snomedConcept);

	}

	private checkClearButtonVisibility() {
		this.clearButton = this.snomedConcept && this.conceptTypeaheadInfo?.clearButton && !this.form.controls.snomedInput.errors;
	}

	private checkHintVisibility() {
		this.hintVisibility = !this.isCacheEnabled && this.inputFocused && (!this.form.controls.snomedInput.value || this.form.controls.snomedInput.value.length < MIN_SEARCH_LENGTH);
	}

	private setFeatureFlags() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.isCacheEnabled = isOn);
	}
}

export interface ConceptTypeaheadInfo {
	clearButton: boolean,
	required: boolean,
	maxSearchResults: number,
	placeholder?: string,
	debounceTime?: number,
}
