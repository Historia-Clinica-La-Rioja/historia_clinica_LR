import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SurgicalReportDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-prosthesis',
	templateUrl: './surgical-report-prosthesis.component.html',
	styleUrls: ['./surgical-report-prosthesis.component.scss']
})
export class SurgicalReportProsthesisComponent implements OnInit {

	@Input() surgicalReport: SurgicalReportDto;
	@Output() validProsthesis = new EventEmitter();


	form = new FormGroup({
		prosthesis: new FormControl(),
		description: new FormControl(),
	});

	constructor() {

	}

	ngOnInit(): void {
		this.form.controls.prosthesis.valueChanges.subscribe(value => {
			const descriptionControl = this.form.controls.description;
			if (value) {
				descriptionControl.enable();
				descriptionControl.setValidators([Validators.required]);
			} else {
				descriptionControl.disable();
				descriptionControl.clearValidators();
			}
			descriptionControl.updateValueAndValidity();
		});

		this.form.controls.description.setValue(this.surgicalReport.prosthesisDescription);
		this.form.controls.prosthesis.setValue(!!this.surgicalReport.prosthesisDescription);

		this.form.valueChanges.subscribe(data => {
			this.surgicalReport.prosthesisDescription = data.prosthesis ? data.description : null;
			this.validateForm();
		});
	}

	validateForm(): void {
		this.validProsthesis.emit(this.form.valid);
	}
}