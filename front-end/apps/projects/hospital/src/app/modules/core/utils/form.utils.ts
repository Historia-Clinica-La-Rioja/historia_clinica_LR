import {UntypedFormGroup, UntypedFormArray, AbstractControl, UntypedFormControl, ValidationErrors, ValidatorFn, FormControl} from '@angular/forms';
import { ElementRef } from '@angular/core';
import { Moment } from 'moment';
import { momentFormat, newMoment } from './moment.utils';
import { DateFormat, toHourMinute } from './date.utils';
import { format } from 'date-fns';
import { EVENT_CODE_NUMBERS } from './core.utils';

export const VALIDATIONS = {
	MAX_LENGTH: {
		identif_number: 11,
		cuil: 11,
		gender: 40,
		phonePrefix:10,
		phone:15,
	}
};

export const TIME_PATTERN = '([0-1]{1}[0-9]{1}|20|21|22|23):[0-5]{1}[0-9]{1}';
export const NUMBER_PATTERN = /^[0-9]\d*$/;
export const DEFAULT_COUNTRY_ID = 14;
export const NON_WHITESPACE_REGEX = /\S/;
export const STRING_PATTERN = /^[a-zA-Z\s]+$/;

export function hasError(form: AbstractControl, type: string, control: string): boolean {
	return form.get(control).hasError(type);
}

export function getError(form: AbstractControl, type: string, control: string): any {
	return form.get(control).getError(type);
}

export function scrollIntoError(form: UntypedFormGroup, el: ElementRef) {
	for (const controlName of Object.keys(form.controls)) {
		if (form.controls[controlName].invalid) {
			const invalidControl = getInvalidElement(el, controlName, form);
			invalidControl.scrollIntoView({ behavior: 'smooth', block: 'center' });
			break;
		}
	}

	function getInvalidElement(elementRef: ElementRef, controlName: string, formGroup: UntypedFormGroup) {
		const formControl = formGroup.controls[controlName] as UntypedFormArray;
		if (formControl.controls) {
			return elementRef.nativeElement.querySelector('[formgroupname="' + controlName + '"]');
		}
		return elementRef.nativeElement.querySelector('[formcontrolname="' + controlName + '"]');
	}
}

export function atLeastOneValueInFormGroup(form: UntypedFormGroup): boolean {
	return !Object.values(form.value).every(x => (x === null || x === ''));
}

export function futureTimeValidation(control: UntypedFormControl): ValidationErrors | null {
	const time: string = control.value;
	const today: Moment = newMoment();
	if (isValidTime(time)) {
		if (time > momentFormat(today, DateFormat.HOUR_MINUTE)) {
			return {
				futureTime: true
			};
		}
	}
	return null;
}

export function futureTimeValidationDate(control: UntypedFormControl): ValidationErrors | null {
	const time: string = control.value;
	const today: Date = new Date();
	if (isValidTime(time)) {
		if (time > format(today, DateFormat.HOUR_MINUTE))
			return { futureTime: true };
	}
	return null;
}

export function beforeTimeValidation(moment: Moment) {
	return (control: UntypedFormControl): ValidationErrors | null => {
		const time: string = control.value;
		if (isValidTime(time)) {
			if (time < momentFormat(moment, DateFormat.HOUR_MINUTE)) {
				return {
					beforeTime: true
				};
			}
		}
		return null;
	};
}

export function beforeTimeValidationDate(date: Date) {
	return (control: UntypedFormControl): ValidationErrors | null => {
		const time: string = control.value;
		if (isValidTime(time)) {
			if (time < format(date, DateFormat.HOUR_MINUTE))
				return { beforeTime: true };
		}
		return null;
	};
}

export function beforeTimeDateValidation(date: string) {
	return (control: UntypedFormControl): ValidationErrors | null => {
		const time: string = control.value;
		if (isValidTime(time)) {
			if (time < date) {
				return {
					beforeTime: true
				};
			}
		}
		return null;
	};
}

function isValidTime(time: string) {
	return time.match('([0-1]{1}[0-9]{1}|20|21|22|23):[0-5]{1}[0-9]{1}');
}

export class MinTimeValidator {
	constructor(private readonly minDateTime: Date) { }

	minTimeValidation(control: UntypedFormControl): ValidationErrors | null {
		const time: string = control.value;
		if (isValidTime(time)) {
			if (time <= toHourMinute(this.minDateTime)) {
				return {
					previousTime: true
				};
			}
		}
		return null;
	}
}

export function processErrors(errorResponse, showMessageCallback) {
	if (errorResponse?.text) {
		showMessageCallback(errorResponse.text);
	}
	if (errorResponse?.errors) {
		errorResponse.errors.forEach(error => {
			showMessageCallback(extractErrorMessage(error));
		});
	}

	function extractErrorMessage(error: string): string {
		return error.split(':').pop();
	}
}

export function updateControlValidator(form: UntypedFormGroup, control: string, validations) {
	form.controls[control].setValidators(validations);
	form.controls[control].updateValueAndValidity();
}

export function updateForm(form: UntypedFormGroup) {
	// Esta función se hizo porque no funciona form.updateValueAndValidity() - bug
	Object.keys(form.controls).forEach((key: string) => {
		const abstractControl = form.controls[key];
		abstractControl.updateValueAndValidity();
	});
}

export function patternValidator(regex: RegExp, error: ValidationErrors): ValidatorFn {
	return (control: AbstractControl): {[key: string]: any} => {
		if (!control.value) {
			return null;
		}
		return regex.test(control.value) ? null : error;
	};
}

export function requiredFileType(type: string): ValidatorFn  {
    return (control: AbstractControl): ValidationErrors | null => {
		const file = control.value;
      	if (file) {
        	const extension = file.name.split('.')[1].toLowerCase();
        	if (type.toLowerCase() !== extension.toLowerCase()) {
          		return {'requiredFileType': true};
        	}
      	}
      return null;
    };
}

export function NoWhitespaceValidator(): ValidatorFn {
	return (control: AbstractControl): { [key: string]: any } | null => {
	  const isWhitespace = (control.value || '').trim().length === 0;
	  const isValid = !isWhitespace;
	  return isValid ? null : { 'whitespace': true };
	};
}

export function includesEventCodeNumber(event: KeyboardEvent) {
	const code = event.code;
	return EVENT_CODE_NUMBERS.includes(code);
}

export type ToFormGroup<T> = {[P in keyof T]: FormControl<T[P]>; };
