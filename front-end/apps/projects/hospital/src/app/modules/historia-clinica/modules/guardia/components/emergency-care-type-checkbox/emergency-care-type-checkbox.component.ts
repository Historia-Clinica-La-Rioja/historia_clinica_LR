import { Component, forwardRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { MasterDataInterface } from '@api-rest/api-model';

const EPISODE_TYPES = 'emergencyCareTypes';

@Component({
	selector: 'app-emergency-care-type-checkbox',
	templateUrl: './emergency-care-type-checkbox.component.html',
	styleUrls: ['./emergency-care-type-checkbox.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => EmergencyCareTypeCheckboxComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => EmergencyCareTypeCheckboxComponent),
		},
	],
})
export class EmergencyCareTypeCheckboxComponent extends AbstractCustomForm implements OnInit {

	form: FormGroup;
	formControlsKeys: string[] = [];
	types: MasterDataInterface<number>[] = [];

	constructor(
		private readonly episodeFilterService: EpisodeFilterService,
	) {
		super();
	}

	ngOnInit() {
		this.createForm();
		this.episodeFilterService.getEmergencyCareTypes().subscribe(types => {
			this.types = types;
			this.addFormControls(types)
		});
	}

	createForm() {
		this.form = new FormGroup({
			emergencyCareTypes: new FormGroup({})
		});
	}

	private addFormControls(types: MasterDataInterface<number>[]) {
		const controls = this.initializeControls(types);
		const form = new FormGroup(controls);
		this.form.setControl(EPISODE_TYPES, form, { emitEvent: false });
		this.formControlsKeys = Object.keys(this.form.value.emergencyCareTypes);
	}

	writeValue(obj: any): void {
		if (!obj)
			this.resetForm();
	}

	private resetForm() {
		const isTheFormInitialized = this.form.value.emergencyCareTypes && Object.keys(this.form.value.emergencyCareTypes).length;

		if (!!isTheFormInitialized) {
			const controls = Object.keys(this.form.value.emergencyCareTypes);
			controls.forEach(control => {
				const emergencyCareTypesForm = this.form.controls.emergencyCareTypes as FormGroup;
				emergencyCareTypesForm.controls[control].setValue(false, { emitEvent: false });
			});
		}
	}

	private initializeControls(types: MasterDataInterface<number>[]) {
		return types.reduce((control, type) => {
			control[type.id] = new FormControl(null);
			return control;
		}, {});
	}

}
