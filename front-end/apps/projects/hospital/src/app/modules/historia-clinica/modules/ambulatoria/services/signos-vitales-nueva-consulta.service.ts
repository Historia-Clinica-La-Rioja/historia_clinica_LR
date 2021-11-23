import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { EffectiveClinicalObservationDto, HCELast2VitalSignsDto } from '@api-rest/api-model';
import { Subject, Observable } from 'rxjs';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';

export interface SignosVitales {
	bloodOxygenSaturation?: EffectiveClinicalObservationDto;
	diastolicBloodPressure: EffectiveClinicalObservationDto;
	heartRate?: EffectiveClinicalObservationDto;
	respiratoryRate?: EffectiveClinicalObservationDto;
	systolicBloodPressure: EffectiveClinicalObservationDto;
	temperature?: EffectiveClinicalObservationDto;
}



export class SignosVitalesNuevaConsultaService {

	private heartRateErrorSource = new Subject<string>();
	private _heartRateError$: Observable<string>;
	private respiratoryRateErrorSource = new Subject<string>();
	private _respiratoryRateError$: Observable<string>;
	private temperatureErrorSource = new Subject<string>();
	private _temperatureError$: Observable<string>;
	private bloodOxygenSaturationErrorSource = new Subject<string>();
	private _bloodOxygenSaturationError$: Observable<string>;
	private systolicBloodPressureErrorSource = new Subject<string>();
	private _systolicBloodPressureError$: Observable<string>;
	private diastolicBloodPressureErrorSource = new Subject<string>();
	private _diastolicBloodPressureError$: Observable<string>;
	private form: FormGroup;
	private notShowPreloadedVitalSignsData = true;
	private dateList: string[] = [];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService?: HceGeneralStateService,
		private readonly patientId?: number,
		private readonly datePipe?: DatePipe,
	) {
		this.form = this.formBuilder.group({
			heartRate: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			respiratoryRate: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			temperature: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			bloodOxygenSaturation: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			systolicBloodPressure: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			diastolicBloodPressure: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			})
		});
		this.form.controls.heartRate.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.heartRateErrorSource.next();
				}
		});
		this.form.controls.respiratoryRate.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.respiratoryRateErrorSource.next();
				}
		});
		this.form.controls.temperature.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.temperatureErrorSource.next();
				}
		});
		this.form.controls.bloodOxygenSaturation.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.bloodOxygenSaturationErrorSource.next();
				}
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
		return controlValue.value || controlValue.value === 0 ?
			{ value: controlValue.value, effectiveTime: controlValue.effectiveTime }
			: undefined;
	}

	get heartRateError$(): Observable<string> {
		if (!this._heartRateError$) {
			this._heartRateError$ = this.heartRateErrorSource.asObservable();
		}
		return this._heartRateError$;
	}

	get respiratoryRateError$(): Observable<string> {
		if (!this._respiratoryRateError$) {
			this._respiratoryRateError$ = this.respiratoryRateErrorSource.asObservable();
		}
		return this._respiratoryRateError$;
	}

	get temperatureError$(): Observable<string> {
		if (!this._temperatureError$) {
			this._temperatureError$ = this.temperatureErrorSource.asObservable();
		}
		return this._temperatureError$;
	}

	get bloodOxygenSaturationError$(): Observable<string> {
		if (!this._bloodOxygenSaturationError$) {
			this._bloodOxygenSaturationError$ = this.bloodOxygenSaturationErrorSource.asObservable();
		}
		return this._bloodOxygenSaturationError$;
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

	setHeartRateError(errorMsg: string): void {
		this.heartRateErrorSource.next(errorMsg);
	}

	setRespiratoryRateError(errorMsg: string): void {
		this.respiratoryRateErrorSource.next(errorMsg);
	}

	setTemperatureError(errorMsg: string): void {
		this.temperatureErrorSource.next(errorMsg);
	}

	setBloodOxygenSaturationError(errorMsg: string): void {
		this.bloodOxygenSaturationErrorSource.next(errorMsg);
	}

	setSystolicBloodPressureError(errorMsg: string): void {
		this.systolicBloodPressureErrorSource.next(errorMsg);
	}

	setDiastolicBloodPressureError(errorMsg: string): void {
		this.diastolicBloodPressureErrorSource.next(errorMsg);
	}

	setPreviousVitalSignsData(): void {
		this.hceGeneralStateService.getVitalSigns(this.patientId).subscribe(
			(vitalSignsData: HCELast2VitalSignsDto) => {
				if (vitalSignsData.current === undefined)
					this.notShowPreloadedVitalSignsData = false;
				else {
					Object.keys(vitalSignsData.current).forEach((key: string) => {
						if (vitalSignsData.current[key].value != undefined) {
							this.form.patchValue({ [key]: { value: vitalSignsData.current[key].value } });
							this.dateList.push(vitalSignsData.current[key].effectiveTime);
						}
					});
					this.form.disable();
				}
			});
	}

	discardPreloadedVitalSignsData() {
		this.notShowPreloadedVitalSignsData = false;
		const defaultValue = { value: null, effectiveTime: newMoment() };
		Object.keys(this.form.controls).forEach((key: string) => {
			this.form.patchValue({ [key]:defaultValue});
		});
		this.form.enable();
	}

	getShowPreloadedVitalSignsData(): boolean {
		return this.notShowPreloadedVitalSignsData;
	}

	savePreloadedVitalSignsData() {
		this.notShowPreloadedVitalSignsData = false;
		this.form.enable();
	}

	getDate(): string {
		return this.datePipe.transform(Math.max.apply(null, this.dateList.map((date) => new Date(date))), DatePipeFormat.SHORT);
	}
}
