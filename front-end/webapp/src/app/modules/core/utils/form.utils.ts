import { FormGroup, FormArray } from "@angular/forms";
import { ElementRef } from '@angular/core';

export const VALIDATIONS = {
	MAX_LENGTH: {
		identif_number: 11,
	}
}

export function hasError(form: FormGroup, type: string, control: string): boolean {
	return form.get(control).hasError(type);
}

export function scrollIntoError(form: FormGroup,el: ElementRef) {
	for (const controlName of Object.keys(form.controls)) {
		if (form.controls[controlName].invalid) {
			const invalidControl = getInvalidElement(el, controlName,form);
			invalidControl.scrollIntoView({behavior: 'smooth', block: 'center'});
			break;
		}
	}

	function getInvalidElement(el: ElementRef, controlName: string, form: FormGroup) {
		let formControl = form.controls[controlName] as FormArray;
		if (formControl.controls)
			return el.nativeElement.querySelector('[formgroupname="' + controlName + '"]');
		return el.nativeElement.querySelector('[formcontrolname="' + controlName + '"]');
	}
}