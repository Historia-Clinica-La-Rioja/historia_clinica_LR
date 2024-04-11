import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AnthropometricDataDto, MasterDataInterface } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { PATTERN_INTEGER_NUMBER, PATTERN_NUMBER_WITH_DECIMALS } from '@core/utils/pattern.utils';
import { DATOS_ANTROPOMETRICOS } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject, Observable, Subject, distinctUntilChanged } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class AnestheticReportAnthropometricDataService {

    private form: FormGroup<AnthropometricDataForm>;
	private bloodTypes: MasterDataInterface<string>[];
    private heightErrorSource = new Subject<string | void>();
	private _heightError$ = this.heightErrorSource.asObservable();
    private weightErrorSource = new Subject<string | void>();
	private _weightError$ = this.weightErrorSource.asObservable();
	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly translateService: TranslateService,
    ) {
        this.form = new FormGroup<AnthropometricDataForm>({
            bloodType: new FormControl(null),
            weight: new FormControl(null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.weight), Validators.max(DATOS_ANTROPOMETRICOS.MAX.weight), Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)]),
            height: new FormControl(null, [Validators.min(DATOS_ANTROPOMETRICOS.MIN.height), Validators.max(DATOS_ANTROPOMETRICOS.MAX.height), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
        });

		this.form.valueChanges.pipe(distinctUntilChanged()
		).subscribe(_ => {
			this.isEmptySource.next(this.isEmpty());
		});

		this.form.controls.height.valueChanges.subscribe(_ => {
			this.checkHeightErrors();
		});

		this.form.controls.weight.valueChanges.subscribe(_ => {
			this.checkWeightErrors();
		});


        this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
    }

    private checkHeightErrors() {
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
    }

    private checkWeightErrors() {
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
    }

    getForm(): FormGroup {
        return this.form;
    }

    getBloodTypes(): MasterDataInterface<string>[] {
		return this.bloodTypes;
	}

    getAnthropomethricData(): AnthropometricDataDto {
        return {
            bloodType: {
                value:  this.form.value.bloodType?.description || null
            },
            height: {
                value: this.form.value.height || null
            },
            weight: {
                value: this.form.value.weight || null
            },
        }
    }

    get heightError$(): Observable<string | void> {
		return this._heightError$;
	}

    get weightError$(): Observable<string | void> {
		return this._weightError$;
	}

    setAnthropometric(weight?: string, height?: string, bloodDescription?: string): void {
		this.form.get('weight').setValue(weight);
		this.form.get('height').setValue(height);
		if (bloodDescription != null)
			this.form.get('bloodType').setValue(this.bloodTypes?.find(b => b.description === bloodDescription));
	}

    isEmpty(): boolean {
        return !(this.form.get("bloodType").value || this.form.get("height").value || this.form.get("weight").value)
    }
}

export interface AnthropometricDataForm {
    bloodType: FormControl<MasterDataInterface<string>>;
    height: FormControl<string>;
    weight: FormControl<string>;
}
