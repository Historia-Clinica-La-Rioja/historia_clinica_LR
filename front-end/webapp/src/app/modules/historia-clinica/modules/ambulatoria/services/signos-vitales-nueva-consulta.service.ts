import { FormBuilder, FormGroup } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { EffectiveClinicalObservationDto } from '@api-rest/api-model';

export interface SignosVitales {
	bloodOxygenSaturation?: EffectiveClinicalObservationDto;
	diastolicBloodPressure?: EffectiveClinicalObservationDto;
	heartRate?: EffectiveClinicalObservationDto;
	respiratoryRate?: EffectiveClinicalObservationDto;
	systolicBloodPressure?: EffectiveClinicalObservationDto;
	temperature?: EffectiveClinicalObservationDto;
}



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

	getSignosVitales(): SignosVitales {
		return {
			bloodOxygenSaturation: this.getEffectiveClinicalObservationDto(this.form.value.bloodOxygenSaturation),
			diastolicBloodPressure: this.getEffectiveClinicalObservationDto(this.form.value.diastolicBloodPressure),
			heartRate: this.getEffectiveClinicalObservationDto(this.form.value.heartRate),
			respiratoryRate: this.getEffectiveClinicalObservationDto(this.form.value.respiratoryRate),
			systolicBloodPressure: this.getEffectiveClinicalObservationDto(this.form.value.systolicBloodPressure),
			temperature: this.getEffectiveClinicalObservationDto(this.form.value.temperature)
		};
	}

	private getEffectiveClinicalObservationDto(controlValue: any): EffectiveClinicalObservationDto {
		return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
	}

}
