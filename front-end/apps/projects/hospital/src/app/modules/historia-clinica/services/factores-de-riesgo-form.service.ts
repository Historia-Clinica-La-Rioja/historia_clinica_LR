import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { newDate } from '@core/utils/moment.utils';
import { EffectiveClinicalObservationDto, HCELast2RiskFactorsDto } from '@api-rest/api-model';
import { Subject, Observable, BehaviorSubject } from 'rxjs';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/validation-constants';
import { PATTERN_NUMBER_WITH_DECIMALS, PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS } from '@core/utils/pattern.utils';
import { TranslateService } from '@ngx-translate/core';
import { fixDate } from '@core/utils/date/format';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

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

	private heartRateErrorSource = new Subject<string | void>();
	private _heartRateError$ = this.heartRateErrorSource.asObservable();
	private respiratoryRateErrorSource = new Subject<string | void>();
	private _respiratoryRateError$ = this.respiratoryRateErrorSource.asObservable();
	private temperatureErrorSource = new Subject<string | void>();
	private _temperatureError$ = this.temperatureErrorSource.asObservable();
	private bloodOxygenSaturationErrorSource = new Subject<string | void>();
	private _bloodOxygenSaturationError$ = this.bloodOxygenSaturationErrorSource.asObservable();
	private systolicBloodPressureErrorSource = new Subject<string | void>();
	private _systolicBloodPressureError$ = this.systolicBloodPressureErrorSource.asObservable();
	private diastolicBloodPressureErrorSource = new Subject<string | void>();
	private _diastolicBloodPressureError$ = this.diastolicBloodPressureErrorSource.asObservable();
	private bloodGlucoseErrorSource = new Subject<string | void>();
	private _bloodGlucoseError$ = this.bloodGlucoseErrorSource.asObservable();
	private glycosylatedHemoglobinErrorSource = new Subject<string | void>();
	private _glycosylatedHemoglobinError$ = this.glycosylatedHemoglobinErrorSource.asObservable();
	private cardiovascularRiskErrorSource = new Subject<string | void>();
	private _cardiovascularRiskError$ = this.cardiovascularRiskErrorSource.asObservable();
	form: UntypedFormGroup;
	private notShowPreloadedRiskFactorsData = false;
	private dateList: string[] = [];
	private riskFactorsSubject = new BehaviorSubject<boolean>(true);

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly translateService: TranslateService,
		private readonly hceGeneralStateService?: HceGeneralStateService,
		private readonly patientId?: number,
		private readonly dateFormatPipe?: DateFormatPipe,
	) {
		this.form = this.formBuilder.group({
			heartRate: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.heartRate), Validators.max(FACTORES_DE_RIESGO.MAX.heartRate), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			respiratoryRate: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.respiratoryRate), Validators.max(FACTORES_DE_RIESGO.MAX.respiratoryRate), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			temperature: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.temperature), Validators.max(FACTORES_DE_RIESGO.MAX.temperature), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			bloodOxygenSaturation: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.bloodOxygenSaturation), Validators.max(FACTORES_DE_RIESGO.MAX.bloodOxygenSaturation), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			systolicBloodPressure: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.systolicBloodPressure), Validators.max(FACTORES_DE_RIESGO.MAX.systolicBloodPressure), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			diastolicBloodPressure: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.diastolicBloodPressure), Validators.max(FACTORES_DE_RIESGO.MAX.diastolicBloodPressure), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			bloodGlucose: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.bloodGlucose), Validators.max(FACTORES_DE_RIESGO.MAX.bloodGlucose), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			}),
			glycosylatedHemoglobin: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.glycosylatedHemoglobin), Validators.max(FACTORES_DE_RIESGO.MAX.glycosylatedHemoglobin), Validators.pattern(PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS)]],
				effectiveTime: [newDate()],
			}),
			cardiovascularRisk: this.formBuilder.group({
				value: [null, [Validators.min(FACTORES_DE_RIESGO.MIN.cardiovascularRisk), Validators.max(FACTORES_DE_RIESGO.MAX.cardiovascularRisk), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]],
				effectiveTime: [newDate()],
			})
		});

		this.form.controls.heartRate.valueChanges.subscribe(_ => {
			if (this.form.get('heartRate.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.heartRate }).subscribe(
					(errorMsg: string) => this.heartRateErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('heartRate.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.heartRate }).subscribe(
					(errorMsg: string) => this.heartRateErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('heartRate.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.heartRateErrorSource.next(errorMsg)
				);
			}
			else {
				this.heartRateErrorSource.next();
			}
		});

		this.form.controls.respiratoryRate.valueChanges.subscribe(_ => {
			if (this.form.get('respiratoryRate.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.respiratoryRate }).subscribe(
					(errorMsg: string) => this.respiratoryRateErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('respiratoryRate.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.respiratoryRate }).subscribe(
					(errorMsg: string) => this.respiratoryRateErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('respiratoryRate.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.respiratoryRateErrorSource.next(errorMsg)
				);
			}
			else {
				this.respiratoryRateErrorSource.next();
			}
		});

		this.form.controls.temperature.valueChanges.subscribe(_ => {
			if (this.form.get('temperature.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.temperature }).subscribe(
					(errorMsg: string) => this.temperatureErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('temperature.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.temperature }).subscribe(
					(errorMsg: string) => this.temperatureErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('temperature.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.temperatureErrorSource.next(errorMsg)
				);
			}
			else {
				this.temperatureErrorSource.next();
			}
		});

		this.form.controls.bloodOxygenSaturation.valueChanges.subscribe(_ => {
			if (this.form.get('bloodOxygenSaturation.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.bloodOxygenSaturation }).subscribe(
					(errorMsg: string) => this.bloodOxygenSaturationErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('bloodOxygenSaturation.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.bloodOxygenSaturation }).subscribe(
					(errorMsg: string) => this.bloodOxygenSaturationErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('bloodOxygenSaturation.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.bloodOxygenSaturationErrorSource.next(errorMsg)
				);
			}
			else {
				this.bloodOxygenSaturationErrorSource.next();
			}
		});

		this.form.controls.systolicBloodPressure.valueChanges.subscribe(_ => {
			if (this.form.get('systolicBloodPressure.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.systolicBloodPressure }).subscribe(
					(errorMsg: string) => this.systolicBloodPressureErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('systolicBloodPressure.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.systolicBloodPressure }).subscribe(
					(errorMsg: string) => this.systolicBloodPressureErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('systolicBloodPressure.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.systolicBloodPressureErrorSource.next(errorMsg)
				);
			}
			else {
				this.systolicBloodPressureErrorSource.next();
			}
		});

		this.form.controls.diastolicBloodPressure.valueChanges.subscribe(_ => {
			if (this.form.get('diastolicBloodPressure.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.diastolicBloodPressure }).subscribe(
					(errorMsg: string) => this.diastolicBloodPressureErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('diastolicBloodPressure.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.diastolicBloodPressure }).subscribe(
					(errorMsg: string) => this.diastolicBloodPressureErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('diastolicBloodPressure.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.diastolicBloodPressureErrorSource.next(errorMsg)
				);
			}
			else {
				this.diastolicBloodPressureErrorSource.next();
			}
		});

		this.form.controls.bloodGlucose.valueChanges.subscribe(_ => {
			if (this.form.get('bloodGlucose.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.bloodGlucose }).subscribe(
					(errorMsg: string) => this.bloodGlucoseErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('bloodGlucose.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.bloodGlucose }).subscribe(
					(errorMsg: string) => this.bloodGlucoseErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('bloodGlucose.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.bloodGlucoseErrorSource.next(errorMsg)
				);
			}
			else {
				this.bloodGlucoseErrorSource.next();
			}
		});

		this.form.controls.glycosylatedHemoglobin.valueChanges.subscribe(_ => {
			if (this.form.get('glycosylatedHemoglobin.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.glycosylatedHemoglobin }).subscribe(
					(errorMsg: string) => this.glycosylatedHemoglobinErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('glycosylatedHemoglobin.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.glycosylatedHemoglobin }).subscribe(
					(errorMsg: string) => this.glycosylatedHemoglobinErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('glycosylatedHemoglobin.value').hasError('pattern')) {
				this.translateService.get('ambulatoria.factores-de-riesgo-form.MAX_TWO_DECIMAL_DIGITS_ERROR').subscribe(
					(errorMsg: string) => this.glycosylatedHemoglobinErrorSource.next(errorMsg)
				);
			}
			else {
				this.glycosylatedHemoglobinErrorSource.next();
			}
		});

		this.form.controls.cardiovascularRisk.valueChanges.subscribe(_ => {
			if (this.form.get('cardiovascularRisk.value').hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: FACTORES_DE_RIESGO.MIN.cardiovascularRisk }).subscribe(
					(errorMsg: string) => this.cardiovascularRiskErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('cardiovascularRisk.value').hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: FACTORES_DE_RIESGO.MAX.cardiovascularRisk }).subscribe(
					(errorMsg: string) => this.cardiovascularRiskErrorSource.next(errorMsg)
				);
			}
			else if (this.form.get('cardiovascularRisk.value').hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.cardiovascularRiskErrorSource.next(errorMsg)
				);
			}
			else {
				this.cardiovascularRiskErrorSource.next();
			}
		});
	}

	setRiskFactorEffectiveTime(newEffectiveTime: Date, formField: string): void {
		this.riskFactorsSubject.next(true);
		(this.form.controls[formField] as UntypedFormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	getForm(): UntypedFormGroup {
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

	get heartRateError$(): Observable<string | void> {
		return this._heartRateError$;
	}

	get respiratoryRateError$(): Observable<string | void> {
		return this._respiratoryRateError$;
	}

	get temperatureError$(): Observable<string | void> {
		return this._temperatureError$;
	}

	get bloodOxygenSaturationError$(): Observable<string | void> {
		return this._bloodOxygenSaturationError$;
	}

	get diastolicBloodPressureError$(): Observable<string | void> {
		return this._diastolicBloodPressureError$;
	}

	get systolicBloodPressureError$(): Observable<string | void> {
		return this._systolicBloodPressureError$;
	}

	get bloodGlucoseError$(): Observable<string | void> {
		return this._bloodGlucoseError$;
	}

	get glycosylatedHemoglobinError$(): Observable<string | void> {
		return this._glycosylatedHemoglobinError$;
	}

	get cardiovascularRiskError$(): Observable<string | void> {
		return this._cardiovascularRiskError$;
	}

	setPreviousRiskFactorsData(): void {
		if (this.patientId) {
			this.riskFactorsSubject.next(true);
			this.hceGeneralStateService.getRiskFactors(this.patientId).subscribe(
				(riskFactorsData: HCELast2RiskFactorsDto) => {
					if (riskFactorsData.current === undefined)
						this.notShowPreloadedRiskFactorsData = false;
					else {
						this.notShowPreloadedRiskFactorsData = true;
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
	}

	discardPreloadedRiskFactorsData() {
		this.notShowPreloadedRiskFactorsData = false;
		const defaultValue = { value: null, effectiveTime: newDate() };
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
		if (this.dateList.length > 0) return this.dateFormatPipe?.transform(Math.max.apply(null, this.dateList.map((date) => new Date(date))), 'datetime');
	}

	hasAtLeastOneValueLoaded(): boolean {
		return !Object.values(this.form.value).every(
			(riskFactor: UntypedFormGroup) => (riskFactor.value === null || riskFactor.value === '')
		);
	}

	isEmptyRiskFactors(): Observable<boolean> {
		return this.riskFactorsSubject.asObservable();
	}

	getEffectiveObservation(controlValue): EffectiveObservation {
		return controlValue?.value ?
			{ value: controlValue.value, effectiveTime: fixDate(controlValue.effectiveTime) }
			: undefined;
	}

	buildRiskFactorsValue(form: UntypedFormGroup): RiskFactorsValue {
		return isNull(form.value) ? undefined : {
			bloodOxygenSaturation: this.getEffectiveObservation(form.value.bloodOxygenSaturation),
			diastolicBloodPressure: this.getEffectiveObservation(form.value.diastolicBloodPressure),
			heartRate: this.getEffectiveObservation(form.value.heartRate),
			respiratoryRate: this.getEffectiveObservation(form.value.respiratoryRate),
			systolicBloodPressure: this.getEffectiveObservation(form.value.systolicBloodPressure),
			temperature: this.getEffectiveObservation(form.value.temperature),
			bloodGlucose: this.getEffectiveObservation(form.value.bloodGlucose),
			glycosylatedHemoglobin: this.getEffectiveObservation(form.value.glycosylatedHemoglobin),
			cardiovascularRisk: this.getEffectiveObservation(form.value.cardiovascularRisk)
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every((el: { value: number, effectiveTime: Date }) => el.value === null);
		}
	}
}

export interface RiskFactorsValue {
	bloodOxygenSaturation?: EffectiveObservation;
	diastolicBloodPressure?: EffectiveObservation;
	heartRate?: EffectiveObservation;
	respiratoryRate?: EffectiveObservation;
	systolicBloodPressure?: EffectiveObservation;
	temperature?: EffectiveObservation;
	bloodGlucose?: EffectiveObservation;
	glycosylatedHemoglobin?: EffectiveObservation;
	cardiovascularRisk?: EffectiveObservation;
}

export interface EffectiveObservation {
	effectiveTime: Date;
	value: string;
}
