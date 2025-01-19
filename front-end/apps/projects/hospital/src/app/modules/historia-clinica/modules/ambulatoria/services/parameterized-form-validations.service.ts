import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class ParameterizedFormValidationsService {

	isNumericalParameterFormValid = true;

	constructor() { }

	isValidForm(): boolean {
		return this.isNumericalParameterFormValid;
	}
}
