import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { HOURS_LIST, INTERVALS_TIME, OTHER_FREQUENCY, OTHER_INDICATION_ID } from '../../constants/internment-indications';

@Component({
	selector: 'app-other-indication',
	templateUrl: './other-indication.component.html',
	styleUrls: ['./other-indication.component.scss']
})
export class OtherIndicationComponent implements OnInit {
	form: FormGroup;
	othersIndicatiosType: OtherIndicationTypeDto[];
	intervals = INTERVALS_TIME;
	hoursList = HOURS_LIST;
	otherFrequency = OTHER_FREQUENCY;
	DEFAULT_RADIO_OPTION = '0';
	OtherIndication = OTHER_INDICATION_ID;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { othersIndicatiosType: OtherIndicationTypeDto[] },
		private readonly formBuilder: FormBuilder,
		private dialog: MatDialogRef<OtherIndicationComponent>

	) { }

	ngOnInit(): void {
		this.othersIndicatiosType = this.data.othersIndicatiosType;

		this.form = this.formBuilder.group({
			indicationType: [null, [Validators.required]],
			note: [null, [Validators.required]],
			indication: [null],
			frequencyOption: [this.DEFAULT_RADIO_OPTION],
			interval: [null],
			startTime: [null],
			frequencyHour: [null],
			event: [null]

		});

		this.form.controls.indicationType.valueChanges.subscribe((frequencyOption: number) => {
			if (frequencyOption === this.OtherIndication) {
				this.form.controls.indication.setValidators([Validators.required]);
				this.form.controls.indication.updateValueAndValidity();
			} else {
				this.form.controls.indication.removeValidators([Validators.required]);
				this.form.controls.indication.updateValueAndValidity();
			}
		});

		this.form.controls.frequencyOption.valueChanges.subscribe((frequencyOption) => {

			this.removeFormValidators();

			switch (frequencyOption) {
				case '1': {
					this.form.controls.startTime.reset();

					this.form.controls.interval.setValidators([Validators.required]);
					this.form.controls.interval.updateValueAndValidity();

					this.form.controls.startTime.setValidators([Validators.required]);
					this.form.controls.startTime.updateValueAndValidity();

					this.form.controls.event.reset();

					break;
				}
				case '2': {
					this.form.controls.startTime.setValidators([Validators.required]);
					this.form.controls.startTime.updateValueAndValidity();

					this.form.controls.interval.reset();
					this.form.controls.startTime.reset();
					this.form.controls.event.reset();
					break;
				}
				case '3': {
					this.form.controls.event.setValidators([Validators.required]);
					this.form.controls.event.updateValueAndValidity();

					this.form.controls.interval.reset();
					this.form.controls.startTime.reset();
					break;
				}
			}

		});
	}

	private removeFormValidators(): void {
		this.form.controls.interval.removeValidators([Validators.required]);
		this.form.controls.interval.updateValueAndValidity();

		this.form.controls.startTime.removeValidators([Validators.required]);
		this.form.controls.startTime.updateValueAndValidity();

		this.form.controls.event.removeValidators([Validators.required]);
		this.form.controls.event.updateValueAndValidity();
	}

	clearFilterField(control: AbstractControl): void {
		control.reset();
	}

	submit(): void {
		if (this.form.valid) {
			if (this.form.controls.frequencyHour?.value)
				this.form.controls.interval.setValue(this.form.controls.frequencyHour.value);
			this.dialog.close(this.form.value);
		}
	}

}

