import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class SpecialtySectorFormValidityService {

	constructor() { }

	private formValidSubject = new BehaviorSubject<boolean>(false);
	formValid$ = this.formValidSubject.asObservable();

	private confirmAttemptSubject = new BehaviorSubject<boolean>(false);
	confirmAttempt$ = this.confirmAttemptSubject.asObservable();

	setFormValidity(isValid: boolean) {
		this.formValidSubject.next(isValid);
	}

	notifyConfirmAttempt() {
		this.confirmAttemptSubject.next(true);
	}

	resetConfirmAttempt() {
		this.confirmAttemptSubject.next(false);
		this.setFormValidity(false);
	}
}
