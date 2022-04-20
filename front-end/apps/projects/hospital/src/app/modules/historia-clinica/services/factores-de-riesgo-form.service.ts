import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { EffectiveClinicalObservationDto, HCELast2RiskFactorsDto } from '@api-rest/api-model';
import { Subject, Observable } from 'rxjs';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/validation-constants';
import { PATTERN_NUMBER_WITH_DECIMALS, PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS } from '@core/utils/pattern.utils';

export interface FactoresDeRiesgo {
	bloodOxygenSaturation?: EffectiveClinicalObservationDto;
	diastolicBloodPressure: EffectiveClinicalObservationDto;
	heartRate?: EffectiveClinicalObservationDto;
	respiratoryRate?: EffectiveClinicalObservationDto;
	systolicBloodPressure: EffectiveClinicalObservationDto;
	temperature?: EffectiveClinicalObservationDto;
	bloodGlucose?: EffectiveClinicalObservationDto;
	glycosylatedHemoglobin?: EffectiveClinicalObservationDto;
	cardiovascularRisk?: EffectiveClinicalObservationDto;
}

export class FactoresDeRiesgoFormService {

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
	private bloodGlucoseErrorSource = new Subject<string>();
	private _bloodGlucoseError$: Observable<string>;
	private glycosylatedHemoglobinErrorSource = new Subject<string>();
	private _glycosylatedHemoglobinError$: Observable<string>;
	private cardiovascularRiskErrorSource = new Subject<string>();
	private _cardiovascularRiskError$: Observable<string>;
	private form: FormGroup;
	private notShowPreloadedRiskFactorsData = true;
	private dateList: string[] = [];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService?: HceGeneralStateService,
		private readonly patientId?: number,
		private readonly datePipe?: DatePipe,
	) {
		this.form = this.formBuilder.group({
			heartRate: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.heartRate), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			respiratoryRate: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.respiratoryRate), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			temperature: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.temperature), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			bloodOxygenSaturation: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.bloodOxygenSaturation), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			systolicBloodPressure: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.systolicBloodPressure), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			diastolicBloodPressure: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.diastolicBloodPressure), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			bloodGlucose: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.bloodGlucose), Validators.max(FACTORES_DE_RIESGO.MAX.bloodGlucose), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newMoment()],
			}),
			glycosylatedHemoglobin: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.glycosylatedHemoglobin), Validators.max(FACTORES_DE_RIESGO.MAX.glycosylatedHemoglobin), Validators.pattern(PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS)]],
				effectiveTime: [newMoment()],
			}),
			cardiovascularRisk: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.cardiovascularRisk), Validators.max(FACTORES_DE_RIESGO.MAX.cardiovascularRisk), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
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
		this.form.controls.bloodGlucose.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.bloodGlucoseErrorSource.next();
				}
			});
		this.form.controls.glycosylatedHemoglobin.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.glycosylatedHemoglobinErrorSource.next();
				}
			});
		this.form.controls.cardiovascularRisk.valueChanges.subscribe(
			dat => {
				if (dat.value !== undefined) {
					this.cardiovascularRiskErrorSource.next();
				}
			});
	}

	setRiskFactorEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		(this.form.controls[formField] as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	getForm(): FormGroup {
		return this.form;
	}

	getFactoresDeRiesgo(): FactoresDeRiesgo {
		return {
			bloodOxygenSaturation: this.getEffectiveClinicalObservationDto(this.form.value.bloodOxygenSaturation),
			diastolicBloodPressure: this.getEffectiveClinicalObservationDto(this.form.value.diastolicBloodPressure),
			heartRate: this.getEffectiveClinicalObservationDto(this.form.value.heartRate),
			respiratoryRate: this.getEffectiveClinicalObservationDto(this.form.value.respiratoryRate),
			systolicBloodPressure: this.getEffectiveClinicalObservationDto(this.form.value.systolicBloodPressure),
			temperature: this.getEffectiveClinicalObservationDto(this.form.value.temperature),
			bloodGlucose: this.getEffectiveClinicalObservationDto(this.form.value.bloodGlucose),
			glycosylatedHemoglobin: this.getEffectiveClinicalObservationDto(this.form.value.glycosylatedHemoglobin),
			cardiovascularRisk: this.getEffectiveClinicalObservationDto(this.form.value.cardiovascularRisk)
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

	get bloodGlucoseError$(): Observable<string> {
		if (!this._bloodGlucoseError$) {
			this._bloodGlucoseError$ = this.bloodGlucoseErrorSource.asObservable();
		}
		return this._bloodGlucoseError$;
	}

	get glycosylatedHemoglobinError$(): Observable<string> {
		if (!this._glycosylatedHemoglobinError$) {
			this._glycosylatedHemoglobinError$ = this.glycosylatedHemoglobinErrorSource.asObservable();
		}
		return this._glycosylatedHemoglobinError$;
	}

	get cardiovascularRiskError$(): Observable<string> {
		if (!this._cardiovascularRiskError$) {
			this._cardiovascularRiskError$ = this.cardiovascularRiskErrorSource.asObservable();
		}
		return this._cardiovascularRiskError$;
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

	setBloodGlucoseError(errorMsg: string): void {
		this.bloodGlucoseErrorSource.next(errorMsg);
	}

	setGlycosylatedHemoglobinError(errorMsg: string): void {
		this.glycosylatedHemoglobinErrorSource.next(errorMsg);
	}

	setCardiovascularRiskError(errorMsg: string): void {
		this.cardiovascularRiskErrorSource.next(errorMsg);
	}

	setPreviousRiskFactorsData(): void {
		this.hceGeneralStateService.getRiskFactors(this.patientId).subscribe(
			(riskFactorsData: HCELast2RiskFactorsDto) => {
				if (riskFactorsData.current === undefined)
					this.notShowPreloadedRiskFactorsData = false;
				else {
					Object.keys(riskFactorsData.current).forEach((key: string) => {
						if (riskFactorsData.current[key].value != undefined) {
							this.form.patchValue({ [key]: { value: riskFactorsData.current[key].value } });
							this.dateList.push(riskFactorsData.current[key].effectiveTime);
						}
					});
					this.form.disable();
				}
			});
	}

	discardPreloadedRiskFactorsData() {
		this.notShowPreloadedRiskFactorsData = false;
		const defaultValue = { value: null, effectiveTime: newMoment() };
		Object.keys(this.form.controls).forEach((key: string) => {
			this.form.patchValue({ [key]: defaultValue });
		});
		this.form.enable();
	}

	getShowPreloadedRiskFactorsData(): boolean {
		return this.notShowPreloadedRiskFactorsData;
	}

	savePreloadedRiskFactorsData() {
		this.notShowPreloadedRiskFactorsData = false;
		this.form.enable();
	}

	getDate(): string {
		return this.datePipe.transform(Math.max.apply(null, this.dateList.map((date) => new Date(date))), DatePipeFormat.SHORT);
	}
}
