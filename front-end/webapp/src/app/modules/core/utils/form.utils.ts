import { FormGroup, FormArray, AbstractControl } from '@angular/forms';
import { ElementRef } from '@angular/core';

export const VALIDATIONS = {
	MAX_LENGTH: {
		identif_number: 11,
		cuil: 11,
		medicalCoverageAffiliateNumber: 150,
		medicalCoverageName: 255
	}
};

export const DEFAULT_COUNTRY_ID = 14;

export function hasError(form: AbstractControl, type: string, control: string): boolean {
	return form.get(control).hasError(type);
}

export function getError(form: AbstractControl, type: string, control: string): any {
	return form.get(control).getError(type);
}

export function scrollIntoError(form: FormGroup, el: ElementRef) {
	for (const controlName of Object.keys(form.controls)) {
		if (form.controls[controlName].invalid) {
			const invalidControl = getInvalidElement(el, controlName, form);
			invalidControl.scrollIntoView({ behavior: 'smooth', block: 'center' });
			break;
		}
	}

	function getInvalidElement(el: ElementRef, controlName: string, form: FormGroup) {
		const formControl = form.controls[controlName] as FormArray;
		if (formControl.controls)
			return el.nativeElement.querySelector('[formgroupname="' + controlName + '"]');
		return el.nativeElement.querySelector('[formcontrolname="' + controlName + '"]');
	}
}

export function atLeastOneValueInFormGroup(form: FormGroup): boolean {
	return !Object.values(form.value).every(x => (x === null || x === ''));
}
