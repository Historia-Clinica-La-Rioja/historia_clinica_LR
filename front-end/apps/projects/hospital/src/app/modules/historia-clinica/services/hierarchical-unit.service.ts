import { Injectable } from '@angular/core';
import { AbstractControl, FormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Injectable({
	providedIn: 'root'

}) export class HierarchicalUnitService {
	form: UntypedFormGroup;

	control: AbstractControl

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.initializeForm();
		this.setupHierarchicalUnitValidation();
	}

	private initializeForm() {
		this.form = this.formBuilder.group({
			hierarchicalUnitId: [null],
			isAReplacement: [false]
		});
	}

	private setupHierarchicalUnitValidation() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_OBLIGATORIEDAD_UNIDADES_JERARQUICAS).subscribe(isOn => {
			const hierarchicalUnitCtrl = this.form.controls.hierarchicalUnitId;

			if (isOn) {
				hierarchicalUnitCtrl.setValidators(Validators.required);
			} else {
				hierarchicalUnitCtrl.clearValidators();
			}

			hierarchicalUnitCtrl.updateValueAndValidity();
		});
	}

	getForm(): UntypedFormGroup {
		return this.form;
	}

	resetForm() {
		this.form.reset();
	}

	isValidForm(): boolean {
		this.form.markAllAsTouched();
		return this.form.invalid;
	}
}
