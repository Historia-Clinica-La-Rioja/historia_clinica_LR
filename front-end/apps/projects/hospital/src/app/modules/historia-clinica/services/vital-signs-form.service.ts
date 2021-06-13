import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { Injectable } from '@angular/core';
import { VITAL_SIGNS } from '@core/constants/validation-constants';

@Injectable({
	providedIn: 'root'
})
export class VitalSignsFormService {

	constructor(private formBuilder: FormBuilder) {
	}

	buildForm(): FormGroup {
		return this.formBuilder.group({
			heartRate: this.formBuilder.group({
				value: [null, Validators.min(VITAL_SIGNS.min_value)],
				effectiveTime: [newMoment()],
			}),
			respiratoryRate: this.formBuilder.group({
				value: [null, Validators.min(VITAL_SIGNS.min_value)],
				effectiveTime: [newMoment()],
			}),
			temperature: this.formBuilder.group({
				value: [null, Validators.min(VITAL_SIGNS.min_value)],
				effectiveTime: [newMoment()],
			}),
			bloodOxygenSaturation: this.formBuilder.group({
				value: [null, Validators.min(VITAL_SIGNS.min_value)],
				effectiveTime: [newMoment()],
			}),
			systolicBloodPressure: this.formBuilder.group({
				value: [null, Validators.min(VITAL_SIGNS.min_value)],
				effectiveTime: [newMoment()],
			}),
			diastolicBloodPressure: this.formBuilder.group({
				value: [null, Validators.min(VITAL_SIGNS.min_value)],
				effectiveTime: [newMoment()],
			}),
		});
	}

	setVitalSignEffectiveTime(newEffectiveTime: Moment, form: FormGroup, field: string): void {
		(form.controls[field] as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	formHasNoValues(form: FormGroup): boolean {
		return ((form.value.bloodOxygenSaturation.value === null)
			&& (form.value.diastolicBloodPressure.value === null)
			&& (form.value.heartRate.value === null)
			&& (form.value.respiratoryRate.value === null)
			&& (form.value.systolicBloodPressure.value === null)
			&& (form.value.temperature.value === null));
	}

	getEffectiveObservation(controlValue: any): EffectiveObservation {
		return (controlValue.value !== null && controlValue.value !== undefined) ?
			{ value: controlValue.value, effectiveTime: controlValue.effectiveTime.toDate() }
			: undefined;
	}

	buildVitalSignsValue(form: FormGroup): VitalSignsValue {
		return isNull(form.value) ? undefined : {
			bloodOxygenSaturation: this.getEffectiveObservation(form.value.bloodOxygenSaturation),
			diastolicBloodPressure: this.getEffectiveObservation(form.value.diastolicBloodPressure),
			heartRate: this.getEffectiveObservation(form.value.heartRate),
			respiratoryRate: this.getEffectiveObservation(form.value.respiratoryRate),
			systolicBloodPressure: this.getEffectiveObservation(form.value.systolicBloodPressure),
			temperature: this.getEffectiveObservation(form.value.temperature)
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every((el: { value: number, effectiveTime: Moment }) => el.value === null);
		}
	}

}

export interface VitalSignsValue {
	bloodOxygenSaturation?: EffectiveObservation;
	diastolicBloodPressure?: EffectiveObservation;
	heartRate?: EffectiveObservation;
	respiratoryRate?: EffectiveObservation;
	systolicBloodPressure?: EffectiveObservation;
	temperature?: EffectiveObservation;
}

export interface EffectiveObservation {
	effectiveTime: Date;
	value: string;
}
