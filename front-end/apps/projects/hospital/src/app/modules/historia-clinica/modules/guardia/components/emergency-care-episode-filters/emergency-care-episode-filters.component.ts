import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { EpisodeFilters, EpisodeFilterService } from '../../services/episode-filter.service';
import { atLeastOneValueInFormGroup, getError, hasError } from '@core/utils/form.utils';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PERSON } from '@core/constants/validation-constants';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-emergency-care-episode-filters',
	templateUrl: './emergency-care-episode-filters.component.html',
	styleUrls: ['./emergency-care-episode-filters.component.scss']
})
export class EmergencyCareEpisodeFiltersComponent implements OnInit, OnDestroy {

	readonly getError = getError;
	readonly hasError = hasError;
	isCleanFilters = false;

	form: FormGroup<FiltersEpisodeForm>;
	formControlsSubscription: Subscription[] = [];
	@Output() updateEpisodes = new EventEmitter<void>();

	constructor(
		readonly filterService: EpisodeFilterService,
	) { }

	ngOnInit() {
		this.buildForm();
		this.subscribeToFormChanges();
	}

	ngOnDestroy() {
		this.formControlsSubscription.forEach(subscription => 
			subscription.unsubscribe()
		);

		this.filterService.resetFilters();
	}

	filter() {
		this.form.markAllAsTouched();
		this.form.valid && this.updateFilters();
	}

	clear(control: string) {
		this.form.controls[control].reset();
		this.filter();
	}

	hasFilters(): boolean {
		return atLeastOneValueInFormGroup(this.form);
	}

	resetFilters() {
		this.isCleanFilters = true;
		this.form.reset();
		this.filter();
	}

	private buildForm() {
		this.form = new FormGroup<FiltersEpisodeForm>({
			triageCategories: new FormControl(null),
			emergencyCareTypes: new FormControl(null),
			states: new FormControl(null),
			clinicalSpecialtySectorIds: new FormControl(null),
			patientId: new FormControl(null),
			identificationNumber: new FormControl(null, Validators.maxLength(PERSON.MAX_LENGTH.identificationNumber)),
			firstName: new FormControl(null, Validators.maxLength(PERSON.MAX_LENGTH.firstName)),
			lastName: new FormControl(null, Validators.maxLength(PERSON.MAX_LENGTH.lastName)),
			emergencyCareTemporary: new FormControl(null),
			administrativeDischarge: new FormControl(null),
		});
	}

	private subscribeToFormChanges() {
		const formControls: FormControlType[] = ['triageCategories', 'emergencyCareTypes', 'states', 'clinicalSpecialtySectorIds'];
		formControls.forEach(formControl => this.subscribeToFormControlsChanges(formControl));
	}

	private subscribeToFormControlsChanges(formControl: FormControlType) {
		const controlSubscription = this.form.get(formControl).valueChanges.subscribe(selectedFilters => {
			selectedFilters && !this.isCleanFilters && this.updateFilters();
		});

		this.formControlsSubscription.push(controlSubscription);

	}

	private getFormValuesAsEpisodeFilters(): EpisodeFilters {
		const controls = this.form.controls;
		return {
			triageCategories: controls.triageCategories.value,
			emergencyCareTypes: controls.emergencyCareTypes.value,
			states: controls.states.value,
			clinicalSpecialtySectorIds: controls.clinicalSpecialtySectorIds.value,
			patientId: controls.patientId.value,
			identificationNumber: controls.identificationNumber.value,
			firstName: controls.firstName.value,
			lastName: controls.lastName.value,
			emergencyCareTemporary: controls.emergencyCareTemporary.value,
			administrativeDischarge: controls.administrativeDischarge.value,
		}
	}

	private updateFilters() {
		const episodeFilters = this.getFormValuesAsEpisodeFilters();
		this.filterService.setFilters(episodeFilters);
		this.updateEpisodes.emit();
		this.isCleanFilters = false;
	}

}

interface FiltersEpisodeForm {
	triageCategories: FormControl<FormCheckbox>,
	emergencyCareTypes: FormControl<FormCheckbox>,
	clinicalSpecialtySectorIds: FormControl<FormChips>,
	states: FormControl<FormCheckbox>,
	patientId: FormControl<number>,
	identificationNumber: FormControl<string>,
	firstName: FormControl<string>,
	lastName: FormControl<string>,
	emergencyCareTemporary: FormControl<boolean>,
	administrativeDischarge: FormControl<boolean>,
}

export type FormCheckbox = {
	[key: string]: boolean;
}

type FormControlType = 'triageCategories' | 'emergencyCareTypes' | 'states' | 'clinicalSpecialtySectorIds';

export type FormChips = {
	[key: string]: number[];
}
