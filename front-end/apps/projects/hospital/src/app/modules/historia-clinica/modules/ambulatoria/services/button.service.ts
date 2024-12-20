import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable()

export class ButtonService {

	private formDisabled = new BehaviorSubject<boolean>(true);
	private formDisabledPartialSave = new BehaviorSubject<boolean | null>(null);
	private submitForm = new BehaviorSubject<boolean>(false);
	private _submitPartialSave = new BehaviorSubject<boolean>(false);
	private activatePartialSave = new BehaviorSubject<boolean>(false);

	formDisabled$ = this.formDisabled.asObservable();
	formDisabledPartialSave$ = this.formDisabledPartialSave.asObservable();
	submit$ = this.submitForm.asObservable();
	submitPartialSave$ = this._submitPartialSave.asObservable();
	isLoading$ = this.submitForm.asObservable();
	isLoadingPartialSave$ = this._submitPartialSave.asObservable();
	activatePartialSaveButton$ = this.activatePartialSave.asObservable();

	updateFormStatus(isValid: boolean) {
		this.formDisabled.next(isValid);
	}

	updateFormPartialSaveStatus(isValid: boolean) {
		this.formDisabledPartialSave.next(isValid);
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
		this._submitPartialSave.next(false);
	}

	activatePartialSaveButton() {
		this.activatePartialSave.next(true);
	}

}
