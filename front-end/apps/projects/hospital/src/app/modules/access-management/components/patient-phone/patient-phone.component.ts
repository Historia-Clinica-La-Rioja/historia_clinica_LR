import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-patient-phone',
	templateUrl: './patient-phone.component.html',
	styleUrls: ['./patient-phone.component.scss']
})
export class PatientPhoneComponent {

	form: FormGroup<PatientPhoneForm>;

	@Input()
	set patientPhone(patientPhone: PatientPhone) {
		this.setPatientPhone(patientPhone);
	}

	constructor() {
		this.createForm();
	}

	private setPatientPhone(phone: PatientPhone) {
		this.form.controls.phoneNumber.setValue(phone.phoneNumber);
		this.form.controls.phonePrefix.setValue(phone.phonePrefix);
	}

	private createForm() {
		this.form = new FormGroup<PatientPhoneForm>({
			phoneNumber: new FormControl({ disabled: true, value: null }),
			phonePrefix: new FormControl({ disabled: true, value: null }),
		});
	}

}

export interface PatientPhone {
	phonePrefix: string;
	phoneNumber: string;
}

interface PatientPhoneForm {
	phonePrefix: FormControl<string>;
	phoneNumber: FormControl<string>;
}
