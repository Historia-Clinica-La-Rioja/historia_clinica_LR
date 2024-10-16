import { Component, forwardRef } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';

@Component({
	selector: 'app-isolation-alert-section',
	templateUrl: './isolation-alert-section.component.html',
	styleUrls: ['./isolation-alert-section.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => IsolationAlertSectionComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => IsolationAlertSectionComponent),
		},
	],

})
export class IsolationAlertSectionComponent extends AbstractCustomForm {

	form: FormGroup;

	constructor() {
		super();
		this.createForm();
	}

	createForm() {
		this.form = new FormGroup({
			isolationAlerts: new FormControl([])
		});
	}

}
