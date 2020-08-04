import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClinicalObservationDto, MasterDataInterface } from '@api-rest/api-model';
import { Observable, Subject } from 'rxjs';

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

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService
	) {
		this.form = this.formBuilder.group({
			bloodType: [null],
			height: [null],
			weight: [null]
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
		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => this.bloodTypes = bloodTypes);
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
			height: this.form.value.height ? { value: this.form.value.height } : undefined,
			weight: this.form.value.weight ? { value: this.form.value.weight } : undefined
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
}
