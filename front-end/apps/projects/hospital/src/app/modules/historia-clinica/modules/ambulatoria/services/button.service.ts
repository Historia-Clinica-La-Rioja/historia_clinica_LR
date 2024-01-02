import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable()

export class ButtonService {

	private formDisabled = new BehaviorSubject<boolean>(true);
	private submitForm = new BehaviorSubject<boolean>(false);
	private isLoading = new BehaviorSubject<boolean>(true);

	formDisabled$ = this.formDisabled.asObservable();
	submit$ = this.submitForm.asObservable();
	isLoading$ = this.submitForm.asObservable();

	updateFormStatus(isValid: boolean) {
		this.formDisabled.next(isValid);
	}

	submit() {
		this.submitForm.next(true);
	}

	updateLoading(loading: boolean) {
		this.isLoading.next(loading);
	}
}
