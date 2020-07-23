import { FormBuilder, FormGroup } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

export class SignosVitalesNuevaConsultaService {

	private form: FormGroup;

	constructor(
		private readonly formBuilder: FormBuilder
	) {
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
			})
		});
	}

	setVitalSignEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		(this.form.controls[formField] as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	getForm(): FormGroup {
		return this.form;
	}

}
