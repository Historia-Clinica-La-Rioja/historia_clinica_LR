import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { GetCommercialMedicationSnomedDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { CommercialMedicationService } from '@api-rest/services/commercial-medication.service';
import { distinctUntilChanged, mergeMap, Observable, of, startWith } from 'rxjs';

@Component({
	selector: 'app-commercial-pharmaco-typeahead',
	templateUrl: './commercial-pharmaco-typeahead.component.html',
	styleUrls: ['./commercial-pharmaco-typeahead.component.scss']
})
export class CommercialPharmacoTypeaheadComponent {

	snomedConcept: SnomedDto;
	inputFocused = false;

	@Input() ecl: SnomedECL;
	@Input() placeholder = '';
	@Input() showSearchIcon = false;
	@Input() required = false;

	@Output() conceptSelected = new EventEmitter<SnomedDto>();

	private readonly MIN_SEARCH_LENGTH = 3;

	myControl = new UntypedFormControl();
	filteredOptions: Observable<any[]>;
	opts: GetCommercialMedicationSnomedDto[] = [];
	formGroup: UntypedFormGroup;

	constructor(
		private readonly commercialMedicationService: CommercialMedicationService
	) {

		this.filteredOptions = this.myControl.valueChanges.pipe(
			startWith(''),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.filter(searchValue || '')
			})
		)

		this.formGroup = new UntypedFormGroup({
			myControl: this.myControl
		});

		if (this.required) {
			this.myControl.setValidators([Validators.required]);
		}
	}

	private filter(searchValue: string): Observable<GetCommercialMedicationSnomedDto[]> {
		return this.searchConcepts(searchValue)
	}

	private searchConcepts(searchValue): Observable<GetCommercialMedicationSnomedDto[]> {
		if (searchValue.length < this.MIN_SEARCH_LENGTH) return of(this.opts);
		return this.opts.length ?
			of(this.opts) :
			this.commercialMedicationService.getCommercialMedicationSnomedList(searchValue);
	}

	getDisplayName(option: SnomedDto): string {
		return option && option.pt ? option.pt : '';
	}

	handleOptionSelected(event: any) {
		const selectedOption = event.option?.value;
		if (selectedOption) {
			this.snomedConcept = selectedOption;
			this.conceptSelected.emit(this.snomedConcept);
		}
	}

	clear() {
		this.snomedConcept = null;
		this.myControl.reset();
		this.conceptSelected.emit(this.snomedConcept);
	}
}
