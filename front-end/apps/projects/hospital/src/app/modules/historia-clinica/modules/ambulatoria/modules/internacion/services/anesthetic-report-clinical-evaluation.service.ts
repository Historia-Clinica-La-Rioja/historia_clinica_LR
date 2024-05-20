import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RiskFactorDto } from '@api-rest/api-model';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { CLINICAL_EVALUATION } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject, Observable, Subject, distinctUntilChanged } from 'rxjs';

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
	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    constructor(
		private readonly translateService: TranslateService,
    ) {
        this.form = new FormGroup<ClinicalEvaluationDataForm>({
            maxBloodPressure: new FormControl(null, [Validators.min(CLINICAL_EVALUATION.MIN.bloodPressure), Validators.max(CLINICAL_EVALUATION.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            minBloodPressure: new FormControl(null, [Validators.min(CLINICAL_EVALUATION.MIN.bloodPressure), Validators.max(CLINICAL_EVALUATION.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            hematocrit: new FormControl(null, [Validators.min(CLINICAL_EVALUATION.MIN.hematocrit), Validators.max(CLINICAL_EVALUATION.MAX.hematocrit), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
        });

		this.form.valueChanges.pipe(distinctUntilChanged()
		).subscribe(_ => {
			this.isEmptySource.next(this.isEmpty());
		});

        this.handleFormChanges();
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

    private handleFormChanges() {
		this.handleFormControlChanges('minBloodPressure', 'minBloodPressureSource');
		this.handleFormControlChanges('maxBloodPressure', 'maxBloodPressureSource');
		this.handleFormControlChanges('hematocrit', 'hematocritSource');

	}

	private handleFormControlChanges(controlName: string, errorSource: string) {
		this.form.controls[controlName].valueChanges.subscribe(_ => {
			this.checkErrors(controlName, errorSource);
		});
	}

	private checkErrors(controlName: string, errorSource: string) {
		const control = this.form.controls[controlName];
		if (control.hasError('min')) {
			this.translateService.get('forms.MIN_ERROR', { min: CLINICAL_EVALUATION.MIN[controlName] }).subscribe(
				(errorMsg: string) => { this[errorSource].next(errorMsg); }
			);
		}
		else if (control.hasError('max')) {
			this.translateService.get('forms.MAX_ERROR', { max: CLINICAL_EVALUATION.MAX[controlName] }).subscribe(
				(errorMsg: string) => { this[errorSource].next(errorMsg); }
			);
		}
		else if (control.hasError('pattern')) {
			this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
				(errorMsg: string) => { this[errorSource].next(errorMsg); }
			);
		}
		else {
			this[errorSource].next();
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
