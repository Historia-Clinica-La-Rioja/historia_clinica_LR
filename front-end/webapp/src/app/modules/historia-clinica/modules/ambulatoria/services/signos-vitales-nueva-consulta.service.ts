import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { EffectiveClinicalObservationDto } from '@api-rest/api-model';
import { Subject, Observable } from 'rxjs';

export interface SignosVitales {
	bloodOxygenSaturation?: EffectiveClinicalObservationDto;
	diastolicBloodPressure: EffectiveClinicalObservationDto;
	heartRate?: EffectiveClinicalObservationDto;
	respiratoryRate?: EffectiveClinicalObservationDto;
	systolicBloodPressure: EffectiveClinicalObservationDto;
	temperature?: EffectiveClinicalObservationDto;
}



export class SignosVitalesNuevaConsultaService {

	private systolicBloodPressureErrorSource = new Subject<string>();
	private _systolicBloodPressureError$: Observable<string>;
	private diastolicBloodPressureErrorSource = new Subject<string>();
	private _diastolicBloodPressureError$: Observable<string>;
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
		this.form.controls.systolicBloodPressure.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.systolicBloodPressureErrorSource.next();
				}
		});
		this.form.controls.diastolicBloodPressure.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.diastolicBloodPressureErrorSource.next();
				}
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

	get diastolicBloodPressureError$(): Observable<string> {
		if (!this._diastolicBloodPressureError$) {
			this._diastolicBloodPressureError$ = this.diastolicBloodPressureErrorSource.asObservable();
		}
		return this._diastolicBloodPressureError$;
	}

	get systolicBloodPressureError$(): Observable<string> {
		if (!this._systolicBloodPressureError$) {
			this._systolicBloodPressureError$ = this.systolicBloodPressureErrorSource.asObservable();
		}
		return this._systolicBloodPressureError$;
	}

	setSystolicBloodPressureError(errorMsg: string): void {
		this.systolicBloodPressureErrorSource.next(errorMsg);
	}

	setDiastolicBloodPressureError(errorMsg: string): void {
		this.diastolicBloodPressureErrorSource.next(errorMsg);
	}
}
