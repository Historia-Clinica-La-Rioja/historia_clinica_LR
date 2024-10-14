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
	@Input()
	set markAsTouched(value: boolean) {
		if (value && this.form.get('prosthesis').value) {
			this.form.get('description')?.markAsTouched();
			this.collapsed = false;
		}
	}
	@Output() validProsthesis = new EventEmitter();

	form = new FormGroup({
		prosthesis: new FormControl(),
		description: new FormControl(),
	});

	collapsed = true;

	constructor() { }

	ngOnInit(): void {
		this.form.controls.prosthesis.valueChanges.subscribe(value => {
			const descriptionControl = this.form.controls.description;
			if (value) {
				descriptionControl.enable();
				descriptionControl.setValidators([Validators.required]);
			} else {
				descriptionControl.disable();
				descriptionControl.clearValidators();
				descriptionControl.setValue('');
			}
			descriptionControl.updateValueAndValidity();
		});

		this.form.controls.description.setValue(this.surgicalReport.prosthesisInfo?.description);
		this.form.controls.prosthesis.setValue(this.surgicalReport.prosthesisInfo?.hasProsthesis);

		this.form.valueChanges.subscribe(data => {
			this.form.get('description')?.markAsTouched();
			this.surgicalReport.prosthesisInfo = {
                ...this.surgicalReport.prosthesisInfo,
                description: data.description,
                hasProsthesis: data.prosthesis
            };
			this.validateForm();
		});
	}

	validateForm(): void {
		this.validProsthesis.emit(this.form.valid);
	}

	isEmpty(): boolean {
		return !this.form.get('prosthesis').value && !this.form.get('description').value;
	}
}
