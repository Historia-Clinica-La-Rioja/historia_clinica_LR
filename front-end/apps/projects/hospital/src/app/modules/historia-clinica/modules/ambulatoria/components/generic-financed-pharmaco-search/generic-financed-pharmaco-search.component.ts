import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { SnomedMedicationSearchDto, SnomedDto } from '@api-rest/api-model';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { debounceTime, distinctUntilChanged, Observable, of, startWith, switchMap } from 'rxjs';

const DEBOUNCE_TIME = 300;
const MIN_SEARCH_LENGTH = 3;

@Component({
	selector: 'app-generic-financed-pharmaco-search',
	templateUrl: './generic-financed-pharmaco-search.component.html',
	styleUrls: ['./generic-financed-pharmaco-search.component.scss']
})
export class GenericFinancedPharmacoSearchComponent implements OnInit {

	inputFocused = false;
	filteredOptions$: Observable<SnomedMedicationSearchDto[]>;
	problem: string;
	form = new FormGroup<GenericPharmacoSearchForm>({
		searchControl: new FormControl<string>(null, Validators.required),
	});

	@Input() set problemSelected(problemSelected: string) {
		if (!problemSelected)
			this.form.controls.searchControl.disable();
		else {
			this.form.controls.searchControl.enable();
			this.problem = problemSelected;
		}
	};

	@Input() set markFormAsTouched(markFormAsTouched: boolean) {
		if (markFormAsTouched)
			this.form.markAllAsTouched();
	}

	@Input() set preloadConcept(concept: SnomedDto) {
		if (concept) {
			this.form.controls.searchControl.setValue(concept.pt);
			this.conceptSelected.emit(concept);
		}
	}

	@Output() conceptSelected = new EventEmitter<SnomedDto>;

	constructor(
		private readonly snowstormService: SnowstormService
	) { }

	ngOnInit() {
		this.filteredOptions$ = this.form.controls.searchControl.valueChanges.pipe(
			startWith(''),
			debounceTime(DEBOUNCE_TIME),
			distinctUntilChanged(),
			switchMap(searchValue => this.searchConcepts(searchValue || ''))
		);
	}

	handleOptionSelected(event: MatAutocompleteSelectedEvent) {
		const selectedOption: SnomedMedicationSearchDto = event.option?.value;
		this.form.controls.searchControl.setValue(selectedOption.pt.term)
		if (selectedOption) {
			const snomedConcept = { sctid: selectedOption.conceptId, pt: selectedOption.pt.term };
			this.conceptSelected.emit(snomedConcept);
		}
	}

	clear() {
		this.form.controls.searchControl.reset();
		this.conceptSelected.emit(null);
	}

	private searchConcepts(searchValue: string): Observable<SnomedMedicationSearchDto[]> {
		const hasMinOfCharacters = searchValue.length >= MIN_SEARCH_LENGTH;
		const hasSelectedProblem = !!this.problem;
		return (hasMinOfCharacters && hasSelectedProblem) ?
			this.snowstormService.getMedicationConceptsWithFinancingData(searchValue, this.problem)
			: of([]);
	}

}

interface GenericPharmacoSearchForm {
	searchControl: FormControl<string>;
}