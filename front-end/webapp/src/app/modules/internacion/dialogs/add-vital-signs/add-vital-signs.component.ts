import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-add-vital-signs',
	templateUrl: './add-vital-signs.component.html',
	styleUrls: ['./add-vital-signs.component.scss']
})
export class AddVitalSignsComponent implements OnInit {

	form: FormGroup;

	constructor(
		public dialogRef: MatDialogRef<AddVitalSignsComponent>,
		private formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			heartRate: this.formBuilder.group({
				value: [null],
				effectiveTime: [newMoment()],
			}),
			respiratoryRate: this.formBuilder.group({
				value: [null],
				effectiveTime: [newMoment()],
			}),
			temperature: this.formBuilder.group({
				value: [null],
				effectiveTime: [newMoment()],
			}),
			bloodOxygenSaturation: this.formBuilder.group({
				value: [null],
				effectiveTime: [newMoment()],
			}),
			systolicBloodPressure: this.formBuilder.group({
				value: [null],
				effectiveTime: [newMoment()],
			}),
			diastolicBloodPressure: this.formBuilder.group({
				value: [null],
				effectiveTime: [newMoment()],
			}),
		});
	}

	setVitalSignEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		(<FormGroup>(<FormGroup>this.form.controls['vitalSigns']).controls[formField]).controls['effectiveTime'].setValue(newEffectiveTime);
	}

	submit() {
		this.dialogRef.close(this.form.value);
	}

}
