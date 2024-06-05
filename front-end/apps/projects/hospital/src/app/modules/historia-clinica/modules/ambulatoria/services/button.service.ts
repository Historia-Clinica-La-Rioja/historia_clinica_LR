import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable()

export class ButtonService {

	private formDisabled = new BehaviorSubject<boolean>(true);
	private submitForm = new BehaviorSubject<boolean>(false);
	private _submitPartialSave = new BehaviorSubject<boolean>(false);

	formDisabled$ = this.formDisabled.asObservable();
	submit$ = this.submitForm.asObservable();
	submitPartialSave$ = this._submitPartialSave.asObservable();
	isLoading$ = this.submitForm.asObservable();

	updateFormStatus(isValid: boolean) {
		this.formDisabled.next(isValid);
	}

	submit() {
		this.submitForm.next(true);
	}

	resetLoading() {
		this.submitForm.next(false);
	}

	submitPartialSave() {
		this._submitPartialSave.next(true);
	}

	resetLoadingPartialSave() {
		this.submitForm.next(false);
	}

}
