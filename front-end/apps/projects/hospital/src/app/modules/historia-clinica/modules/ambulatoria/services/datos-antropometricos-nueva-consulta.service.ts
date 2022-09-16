import { PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS } from './../../../../core/utils/pattern.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClinicalObservationDto, HCEAnthropometricDataDto, MasterDataInterface } from '@api-rest/api-model';
import { Observable, Subject } from 'rxjs';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { PATTERN_INTEGER_NUMBER, PATTERN_NUMBER_WITH_DECIMALS } from '@core/utils/pattern.utils';
import { DATOS_ANTROPOMETRICOS } from '@historia-clinica/constants/validation-constants';
import { atLeastOneValueInFormGroup } from '@core/utils/form.utils';
import { TranslateService } from '@ngx-translate/core';

export interface DatosAntropometricos {
	bloodType?: ClinicalObservationDto;
	bmi?: ClinicalObservationDto;
	height: ClinicalObservationDto;
	weight: ClinicalObservationDto;
	headCircumference: ClinicalObservationDto;
}

export class DatosAntropometricosNuevaConsultaService {

	private form: FormGroup;
	private bloodTypes: MasterDataInterface<string>[];
	private heightErrorSource = new Subject<string>();
	private _heightError$ = this.heightErrorSource.asObservable();
	private weightErrorSource = new Subject<string>();
	private _weightError$ = this.weightErrorSource.asObservable();
	private headCircumferenceErrorSource = new Subject<string>();
	private _headCircumferenceError$ = this.headCircumferenceErrorSource.asObservable();
	private notShowPreloadedAnthropometricData = false;
	private dateList: string[] = [];
	private readonly atLeastOneValueInFormGroup = atLeastOneValueInFormGroup;


	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly patientId: number,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly translateService: TranslateService,
		private readonly datePipe?: DatePipe,
	) {
		this.form = this.formBuilder.group({
			bloodType: [null],
			headCircumference: [null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.headCircumference), Validators.max(DATOS_ANTROPOMETRICOS.MAX.headCircumference), Validators.pattern(PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS)]],
			height: [null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.height), Validators.max(DATOS_ANTROPOMETRICOS.MAX.height), Validators.pattern(PATTERN_INTEGER_NUMBER)]],
			weight: [null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.weight), Validators.max(DATOS_ANTROPOMETRICOS.MAX.weight), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]]
		});

		this.form.controls.headCircumference.valueChanges.subscribe(_ => {
			if (this.form.controls.headCircumference.hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: DATOS_ANTROPOMETRICOS.MIN.headCircumference }).subscribe(
					(errorMsg: string) => this.headCircumferenceErrorSource.next(errorMsg)
				);
			}
			else if (this.form.controls.headCircumference.hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: DATOS_ANTROPOMETRICOS.MAX.headCircumference }).subscribe(
					(errorMsg: string) => this.headCircumferenceErrorSource.next(errorMsg)
				);
			}
			else if (this.form.controls.headCircumference.hasError('pattern')) {
				this.translateService.get('ambulatoria.datos-antropometricos-nueva-consulta.MAX_TWO_DECIMAL_DIGITS_ERROR').subscribe(
					(errorMsg: string) => this.headCircumferenceErrorSource.next(errorMsg)
				);
			}
			else {
				this.headCircumferenceErrorSource.next();
			}
		});

		this.form.controls.height.valueChanges.subscribe(_ => {
			if (this.form.controls.height.hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: DATOS_ANTROPOMETRICOS.MIN.height }).subscribe(
					(errorMsg: string) => this.heightErrorSource.next(errorMsg)
				);
			}
			else if (this.form.controls.height.hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: DATOS_ANTROPOMETRICOS.MAX.height }).subscribe(
					(errorMsg: string) => this.heightErrorSource.next(errorMsg)
				);
			}
			else if (this.form.controls.height.hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
					(errorMsg: string) => this.heightErrorSource.next(errorMsg)
				);
			}
			else {
				this.heightErrorSource.next();
			}
		});

		this.form.controls.weight.valueChanges.subscribe(_ => {
			if (this.form.controls.weight.hasError('min')) {
				this.translateService.get('forms.MIN_ERROR', { min: DATOS_ANTROPOMETRICOS.MIN.weight }).subscribe(
					(errorMsg: string) => this.weightErrorSource.next(errorMsg)
				);
			}
			else if (this.form.controls.weight.hasError('max')) {
				this.translateService.get('forms.MAX_ERROR', { max: DATOS_ANTROPOMETRICOS.MAX.weight }).subscribe(
					(errorMsg: string) => this.weightErrorSource.next(errorMsg)
				);
			}
			else if (this.form.controls.weight.hasError('pattern')) {
				this.translateService.get('forms.FORMAT_NUMERIC').subscribe(
					(errorMsg: string) => this.weightErrorSource.next(errorMsg)
				);
			}
			else {
				this.weightErrorSource.next();
			}
		});

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
	}

	getForm(): FormGroup {
		return this.form;
	}

	getBloodTypes(): MasterDataInterface<string>[] {
		return this.bloodTypes;
	}

	getDatosAntropometricos(): DatosAntropometricos {
		return {
			bloodType: this.form.value.bloodType ? {
				id: this.form.value.bloodType.id,
				value: this.form.value.bloodType.description
			} : undefined,
			headCircumference: (this.form.value.headCircumference || (this.form.value.headCircumference === 0)) ? { value: this.form.value.headCircumference } : undefined,
			height: (this.form.value.height || (this.form.value.height === 0)) ? { value: this.form.value.height } : undefined,
			weight: (this.form.value.weight || (this.form.value.weight === 0)) ? { value: this.form.value.weight } : undefined
		};
	}

	get heightError$(): Observable<string> {
		return this._heightError$;
	}

	get weightError$(): Observable<string> {
		return this._weightError$;
	}

	get headCircumferenceError$(): Observable<string> {
		return this._headCircumferenceError$;
	}

	setPreviousAnthropometricData(): void {
		this.hceGeneralStateService.getAnthropometricData(this.patientId).subscribe(
			(anthropometricData: HCEAnthropometricDataDto) => {
				this.notShowPreloadedAnthropometricData = anthropometricData ? true : false;
				if (anthropometricData) {
					this.setAnthropometric(anthropometricData.weight?.value, anthropometricData.height?.value, anthropometricData.bloodType?.value, anthropometricData.headCircumference?.value);
					Object.keys(anthropometricData).forEach((key: string) => {
						if (anthropometricData[key].effectiveTime) {
							this.dateList.push(anthropometricData[key].effectiveTime);
						}
					});
				}
			}
		);
	}

	setAnthropometric(weight?: string, height?: string, bloodDescription?: string, headCircumference?: string): void {
		this.form.get('weight').setValue(weight);
		this.form.get('height').setValue(height);
		this.form.get('headCircumference').setValue(headCircumference);
		if (bloodDescription != null)
			this.form.get('bloodType').setValue(this.bloodTypes.find(b => b.description === bloodDescription));
		this.form.disable();
	}

	discardPreloadedAnthropometricData() {
		this.notShowPreloadedAnthropometricData = false;
		this.form.reset();
		this.form.enable();
	}

	getShowPreloadedAnthropometricData(): boolean {
		return this.notShowPreloadedAnthropometricData;
	}

	savePreloadedAnthropometricData() {
		this.notShowPreloadedAnthropometricData = false;
		this.form.enable();
	}

	getDate(): string {
		return this.datePipe.transform(Math.max.apply(null, this.dateList.map((date) => new Date(date))), DatePipeFormat.SHORT);
	}

	hasAtLeastOneValueLoaded(): boolean {
		return this.atLeastOneValueInFormGroup(this.form);
	}
}
