import { Component, forwardRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MasterDataDto } from '@api-rest/api-model';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { EpisodeFilterService } from '../../services/episode-filter.service';

const STATES = 'states';

@Component({
	selector: 'app-states-checkbox',
	templateUrl: './states-checkbox.component.html',
	styleUrls: ['./states-checkbox.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => StatesCheckboxComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => StatesCheckboxComponent),
		},
	],
})
export class StatesCheckboxComponent extends AbstractCustomForm implements OnInit {

	states: MasterDataDto[] = [];
	form: FormGroup;
	formControlsKeys: string[] = [];

	constructor(
		private readonly filterService: EpisodeFilterService,
	) {
		super();
	}


	ngOnInit(): void {
		this.createForm();
		this.loadStates();
	}

	private loadStates(){
		this.filterService.getStates().subscribe(states => {
			this.states = states;
			this.addFormControls(states);
			const savedValues = this.filterService.getFilterValue("stateIds");
			if (savedValues) {
				this.setSavedValues(savedValues);
			}
		})
	}

	private setSavedValues(savedValues: number[]) {
		const statesForm = this.form.controls.states as FormGroup;

		savedValues.forEach(value => {
			const controlKey = value.toString();
			if (statesForm.controls[controlKey]) {
				statesForm.controls[controlKey].setValue(true, { emitEvent: false });
			}
		});
	}

	createForm() {
		this.form = new FormGroup({
			states: new FormGroup({})
		});
	}

	private addFormControls(states: MasterDataDto[]) {
		const controls = this.initializeControls(states);

		const form = new FormGroup(controls);
		this.form.setControl(STATES, form, { emitEvent: false});
		this.formControlsKeys = states.map(state => state.id.toString());
	}

	writeValue(obj: any): void {
		if (!obj)
			this.resetForm();
	}

	private resetForm() {
		const isTheFormInitialized = this.form.value.states && Object.keys(this.form.value.states).length;
		if (!!isTheFormInitialized) {
			const controls = Object.keys(this.form.value.states);
			controls.forEach(control => {
				const statesForm = this.form.controls.states as FormGroup;
				statesForm.controls[control].setValue(false, { emitEvent: false });
			});
		}
	}

	private initializeControls(states: MasterDataDto[]) {
		return states.reduce((control, state) => {
			control[state.id] = new FormControl(null);
			return control;
		}, {});
	}

}
