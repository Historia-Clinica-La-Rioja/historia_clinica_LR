import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClinicalObservationDto, HCEAnthropometricDataDto, MasterDataInterface } from '@api-rest/api-model';
import { Observable, Subject } from 'rxjs';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { PATTERN_INTEGER_NUMBER, PATTERN_NUMBER_WITH_DECIMALS } from '@core/constants/validation-constants';
import { DATOS_ANTROPOMETRICOS } from '@historia-clinica/constants/validation-constants';

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
	private _heightError$: Observable<string>;
	private weightErrorSource = new Subject<string>();
	private _weightError$: Observable<string>;
	private headCircumferenceErrorSource = new Subject<string>();
	private _headCircumferenceError$: Observable<string>;
	private notShowPreloadedAnthropometricData = true;
	private dateList: string[] = [];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly patientId: number,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly datePipe?: DatePipe
	) {
		this.form = this.formBuilder.group({
			bloodType: [null],
			headCircumference: [null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.headCircumference), Validators.max(DATOS_ANTROPOMETRICOS.MAX.headCircumference), Validators.pattern(PATTERN_INTEGER_NUMBER)]],
			height: [null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.height), Validators.max(DATOS_ANTROPOMETRICOS.MAX.height), Validators.pattern(PATTERN_INTEGER_NUMBER)]],
			weight: [null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.weight), Validators.max(DATOS_ANTROPOMETRICOS.MAX.weight), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]]
		});
		this.form.controls.height.valueChanges.subscribe(height => {
			if (height !== undefined) {
				this.heightErrorSource.next();
			}
		});
		this.form.controls.weight.valueChanges.subscribe(weight => {
			if (weight !== undefined) {
				this.weightErrorSource.next();
			}
		});
		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
		this.form.controls.headCircumference.valueChanges.subscribe(headCircumference => {
			if (headCircumference !== undefined) {
				this.headCircumferenceErrorSource.next();
			}
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
		if (!this._heightError$) {
			this._heightError$ = this.heightErrorSource.asObservable();
		}
		return this._heightError$;
	}

	get weightError$(): Observable<string> {
		if (!this._weightError$) {
			this._weightError$ = this.weightErrorSource.asObservable();
		}
		return this._weightError$;
	}

	get headCircumferenceError$(): Observable<string> {
		if (!this._headCircumferenceError$) {
			this._headCircumferenceError$ = this.headCircumferenceErrorSource.asObservable();
		}
		return this._headCircumferenceError$;
	}

	setHeightError(errorMsg: string): void {
		this.heightErrorSource.next(errorMsg);
	}

	setWeightError(errorMsg: string): void {
		this.weightErrorSource.next(errorMsg);
	}

	setHeadCircumferenceError(errorMsg: string): void {
		this.headCircumferenceErrorSource.next(errorMsg);
	}

	setPreviousAnthropometricData(): void {
		this.hceGeneralStateService.getAnthropometricData(this.patientId).subscribe(
			(anthropometricData: HCEAnthropometricDataDto) => {
				if (anthropometricData === null)
					this.notShowPreloadedAnthropometricData = false;
				else {
					this.setAnthropometric(anthropometricData.weight?.value, anthropometricData.height?.value, anthropometricData.bloodType?.value, anthropometricData.headCircumference?.value);
					Object.keys(anthropometricData).forEach((key: string) => {
						if (anthropometricData[key].effectiveTime != undefined) {
							this.dateList.push(anthropometricData[key].effectiveTime);
						}
					})
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
}
