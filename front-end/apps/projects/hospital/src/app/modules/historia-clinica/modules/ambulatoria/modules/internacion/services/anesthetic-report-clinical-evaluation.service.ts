import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RiskFactorDto } from '@api-rest/api-model';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { CLINICAL_EVALUATION } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportClinicalEvaluationService {

    private form: FormGroup<ClinicalEvaluationDataForm>;
    private maxBloodPressureSource = new Subject<string | void>();
	private _maxBloodPressure$ = this.maxBloodPressureSource.asObservable();
    private minBloodPressureSource = new Subject<string | void>();
	private _minBloodPressure$ = this.minBloodPressureSource.asObservable();
    private hematocritSource = new Subject<string | void>();
	private _hematocrit$ = this.hematocritSource.asObservable();

    constructor(
		private readonly translateService: TranslateService,
    ) { 
        this.form = new FormGroup<ClinicalEvaluationDataForm>({
            maxBloodPressure: new FormControl(null, [Validators.min(CLINICAL_EVALUATION.MIN.bloodPressure), Validators.max(CLINICAL_EVALUATION.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            minBloodPressure: new FormControl(null, [Validators.min(CLINICAL_EVALUATION.MIN.bloodPressure), Validators.max(CLINICAL_EVALUATION.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            hematocrit: new FormControl(null, [Validators.min(CLINICAL_EVALUATION.MIN.hematocrit), Validators.max(CLINICAL_EVALUATION.MAX.hematocrit), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
        });

        this.form.controls.maxBloodPressure.valueChanges.subscribe(_ => {
			this.checkMaxBloodPressureErrors();
		});

		this.form.controls.minBloodPressure.valueChanges.subscribe(_ => {
			this.checkMinBloodPressureErrors();
		});

		this.form.controls.hematocrit.valueChanges.subscribe(_ => {
			this.checkHematocritErrors();
		});
    }

    get maxBloodPressureError$(): Observable<string | void> {
		return this._maxBloodPressure$;
	}

    get minBloodPressureError$(): Observable<string | void> {
		return this._minBloodPressure$;
	}
    
    get hematocritError$(): Observable<string | void> {
		return this._hematocrit$;
	}

    checkMinBloodPressureErrors() {
        if (this.form.controls.minBloodPressure.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: CLINICAL_EVALUATION.MIN.bloodPressure }).subscribe(
                (errorMsg: string) => this.minBloodPressureSource.next(errorMsg)
            );
        }
        else if (this.form.controls.minBloodPressure.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: CLINICAL_EVALUATION.MAX.bloodPressure }).subscribe(
                (errorMsg: string) => this.minBloodPressureSource.next(errorMsg)
            );
        }
        else if (this.form.controls.minBloodPressure.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.minBloodPressureSource.next(errorMsg)
            );
        }
        else {
            this.minBloodPressureSource.next();
        }
    }

    checkMaxBloodPressureErrors() {
        if (this.form.controls.maxBloodPressure.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: CLINICAL_EVALUATION.MIN.bloodPressure }).subscribe(
                (errorMsg: string) => this.maxBloodPressureSource.next(errorMsg)
            );
        }
        else if (this.form.controls.maxBloodPressure.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: CLINICAL_EVALUATION.MAX.bloodPressure }).subscribe(
                (errorMsg: string) => this.maxBloodPressureSource.next(errorMsg)
            );
        }
        else if (this.form.controls.maxBloodPressure.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.maxBloodPressureSource.next(errorMsg)
            );
        }
        else {
            this.maxBloodPressureSource.next();
        }
    }

    checkHematocritErrors() {
        if (this.form.controls.hematocrit.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: CLINICAL_EVALUATION.MIN.hematocrit }).subscribe(
                (errorMsg: string) => this.hematocritSource.next(errorMsg)
            );
        }
        else if (this.form.controls.hematocrit.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: CLINICAL_EVALUATION.MAX.hematocrit }).subscribe(
                (errorMsg: string) => this.hematocritSource.next(errorMsg)
            );
        }
        else if (this.form.controls.hematocrit.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.hematocritSource.next(errorMsg)
            );
        }
        else {
            this.hematocritSource.next();
        }
    }

    getForm(): FormGroup {
        return this.form;
    }

    isEmpty(): boolean {
        return !(this.form.get("minBloodPressure").value || this.form.get("maxBloodPressure").value || this.form.get("hematocrit").value)
    }

    getClinicalEvaluationData(): RiskFactorDto {
        return this.mapToRiskFactorDto();
    }

    private mapToRiskFactorDto(): RiskFactorDto {
        return {
            systolicBloodPressure: this.form.value.maxBloodPressure ? { 
                value: this.form.value.maxBloodPressure
            } : null,
            diastolicBloodPressure: this.form.value.minBloodPressure ? {
                value: this.form.value.minBloodPressure
            } : null,
            hematocrit: this.form.value.hematocrit ? {
                value: this.form.value.hematocrit
            } : null,
        }
    }
}

export interface ClinicalEvaluationDataForm {
    maxBloodPressure: FormControl<string>;
    minBloodPressure: FormControl<string>;
    hematocrit: FormControl<string>;
}