import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClinicalObservationDto, HCEAnthropometricDataDto, MasterDataInterface } from '@api-rest/api-model';
import { Observable, Subject } from 'rxjs';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';

export interface DatosAntropometricos {
	bloodType?: ClinicalObservationDto;
	bmi?: ClinicalObservationDto;
	height: ClinicalObservationDto;
	weight: ClinicalObservationDto;
}

export class DatosAntropometricosNuevaConsultaService {

	private form: FormGroup;
	private bloodTypes: MasterDataInterface<string>[];
	private heightErrorSource = new Subject<string>();
	private _heightError$: Observable<string>;
	private weightErrorSource = new Subject<string>();
	private _weightError$: Observable<string>;
	private notShowPreloadedAnthropometricData = true;
	private dateOfLastData: Date;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly patientId: number,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly datePipe?: DatePipe
	) {
		this.form = this.formBuilder.group({
			bloodType: [null],
			height: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^[0-9]+$')]],
			weight: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^\\d*\\.?\\d+$')]]
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

	setHeightError(errorMsg: string): void {
		this.heightErrorSource.next(errorMsg);
	}

	setWeightError(errorMsg: string): void {
		this.weightErrorSource.next(errorMsg);
	}

	setPreviousAnthropometricData(): void {
		this.hceGeneralStateService.getAnthropometricData(this.patientId).subscribe(
			(anthropometricData: HCEAnthropometricDataDto) => {
				if (anthropometricData === null)
					this.notShowPreloadedAnthropometricData = false;
				else {
					this.setAnthropometric(anthropometricData.weight.value, anthropometricData.height.value, anthropometricData.bloodType.value);
					this.setDateOfLastData([anthropometricData.bloodType.effectiveTime, anthropometricData.weight.effectiveTime, anthropometricData.height.effectiveTime]);
				}
			});
	}

	setAnthropometric(weight: string, height: string, bloodDescription: string): void {
		this.form.get('weight').setValue(weight);
		this.form.get('height').setValue(height);
		this.form.get('bloodType').setValue(this.bloodTypes.find(b => b.description === bloodDescription));
	}

	discardPreloadedAnthropometricData() {
		this.notShowPreloadedAnthropometricData = false;
		this.form.reset();
	}

	getShowPreloadedAnthropometricData(): boolean {
		return this.notShowPreloadedAnthropometricData;
	}

	savePreloadedAnthropometricData() {
		this.notShowPreloadedAnthropometricData = false;
	}

	setDateOfLastData(dateList: string[]) {
		this.dateOfLastData = new Date(Math.max.apply(null, dateList.map((date) => new Date(date))));
	}

	getDate(): string {
		return this.datePipe.transform(this.dateOfLastData, DatePipeFormat.SHORT);
	}
}
