import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { AppFeature, SnomedDto, SnomedECL, SnomedSearchDto } from '@api-rest/api-model';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PresentationModule } from '@presentation/presentation.module';

const MIN_SEARCH_LENGTH: number = 3;

@Component({
	selector: 'app-concept-typeahead-search-v2',
	templateUrl: './concept-typeahead-search-v2.component.html',
	styleUrls: ['./concept-typeahead-search-v2.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class ConceptTypeaheadSearchV2Component implements OnInit {

	@Input() preload: SnomedDto
	@Input() ecl: SnomedECL;
	@Input() conceptTypeaheadInfo: ConceptTypeaheadInfo;

	@Output() conceptSelected = new EventEmitter<SnomedDto>();

	form: FormGroup;
	snomedConcept: SnomedDto;
	loading: boolean = false;
	items: SnomedDto[] = [];
	itemsSearchDto: SnomedSearchDto = null;
	inputText: string = '';
	totalResults: number = 0;
	clearButton: boolean = false;
	hintVisibility: boolean = false;
	isCacheEnabled: boolean = false;
	minSearchLength: number = MIN_SEARCH_LENGTH

	@ViewChild('input', { read: MatAutocompleteTrigger })
    inputAutocomplete: MatAutocompleteTrigger;

	constructor(
		private readonly fb: FormBuilder,
		private readonly featureFlagService: FeatureFlagService,
		private readonly snowstormService: SnowstormService
	) {
		this.setFeatureFlags();
		this.checkClearButtonVisibility();
		this.form = this.fb.group({
			snomedInput: new FormControl('', this.conceptTypeaheadInfo?.required ? Validators.required : null)
		});
	}

	ngOnInit(): void {
		this.verifyPreloadData();
		this.form.controls.snomedInput.valueChanges.subscribe(data => {
			this.inputText = data
			this.hintVisibility = true
			if (this.inputText && this.inputText.length >= MIN_SEARCH_LENGTH) {
				this.loading = true;
				this.filter(this.inputText)
			} else {
				this.hintVisibility = true
				this.items = []
				this.totalResults = 0
			}
		})
		this.form.controls.snomedInput.statusChanges.subscribe(() => {
			this.checkClearButtonVisibility();
		});
		window.addEventListener('scroll', this.scrollEventMatAutocomplete, true);
	}

	onInputFocus() {
		if (this.isCacheEnabled && !this.inputText?.length) {
			this.hintVisibility = false
			if (!this.items.length) {
				this.loading = true;
				this.snowstormService.searchSNOMEDConceptsWithoutTerms(this.ecl).subscribe(data => {
					const items = data.total > this.conceptTypeaheadInfo.maxSearchResults ? data.items.slice(0, this.conceptTypeaheadInfo.maxSearchResults) : data.items
					this.itemsSearchDto = data
					this.totalResults = this.itemsSearchDto.total
					this.itemsSearchDto.items = items
					this.items = items.map(item => ({
						sctid: item.conceptId,
						pt: item.pt.term
					}));
					this.loading = false;
				});
			}
		} else {
			this.hintVisibility = true
		}
	}

	filter(searchValue: string) {
		this.hintVisibility = false
		const filterValue = searchValue.toLowerCase();
		this.snowstormService.getSNOMEDConcepts({ term: filterValue, ecl: this.ecl }).subscribe(data => {
			this.totalResults = data.total
			this.items = data.items.length > this.conceptTypeaheadInfo.maxSearchResults ? data.items.slice(0, this.conceptTypeaheadInfo.maxSearchResults) : data.items
		})
		this.loading = false
	}

	onOptionSelected(selectedOption: SnomedDto): void {
		const selectedItem = this.items.find(item => item.pt === selectedOption.pt);
		if (selectedItem) {
			this.snomedConcept = selectedItem;
			this.conceptSelected.emit(this.snomedConcept);
			this.form.controls.snomedInput?.setValue(selectedItem.pt);
		}
	}

	clearInput(): void {
		this.snomedConcept = {
			sctid: null,
			pt: null
		}
		this.itemsSearchDto = null
		this.form.reset();
		this.conceptSelected.emit(this.snomedConcept);
		this.checkClearButtonVisibility();
	}

	private verifyPreloadData() {
		if (this.preload.pt && this.preload.sctid) {
			this.setPreload()
		}
	}

	private setPreload() {
		this.snomedConcept = {
			sctid: this.preload.sctid,
			pt: this.preload.pt
		};
		this.form.controls.snomedInput?.setValue(this.preload?.pt);
		this.checkClearButtonVisibility()
		this.conceptSelected.emit(this.snomedConcept);
	}

	private checkClearButtonVisibility() {
		this.clearButton = this.snomedConcept
			&& this.conceptTypeaheadInfo?.clearButton
			&& !this.form.controls.snomedInput.errors
			&& this.snomedConcept.sctid !== null
			&& this.snomedConcept.pt !== null;
	}

	private setFeatureFlags() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.isCacheEnabled = isOn);
	}

	private scrollEventMatAutocomplete = (event: any): void => {
        if(this.inputAutocomplete?.panelOpen){
            this.inputAutocomplete.updatePosition();
        }
    };
}

export interface ConceptTypeaheadInfo {
	clearButton: boolean,
	required: boolean,
	maxSearchResults: number,
	placeholder?: string,
	debounceTime?: number,
}
