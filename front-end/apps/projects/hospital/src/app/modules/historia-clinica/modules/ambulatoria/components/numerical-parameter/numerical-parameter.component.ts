import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { getElementAtPosition } from '@core/utils/array.utils';
import { PATTERN_NUMBER_WITH_DECIMALS } from '@core/utils/pattern.utils';
import { ParameterizedFormValidationsService } from '../../services/parameterized-form-validations.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-numerical-parameter',
	templateUrl: './numerical-parameter.component.html',
	styleUrls: ['./numerical-parameter.component.scss']
})
export class NumericalParameterComponent implements OnDestroy {

	private _amountOfInputs: number;
	private formSubscription: Subscription;
	numericalParametersSelect: NumericalParameter[] = [];
	isAmountInputsSameThatAmountUnitOfMeasure = false;
	numericalParameterData: NumericalParameterData[] = [];

	form = new FormGroup({
		parameters: new FormArray([])
	});

	@Input() title: string;
	@Input() set parameters(numericalParameterData: NumericalParameterData[]) {
		if (numericalParameterData?.length) {
			this.setData(numericalParameterData);
			this.buildForm();
			this.subscribeToFormChanges();
		}
	}

	@Output() numericalValues = new EventEmitter<NumericalParameterValues[]>;

	constructor(
		private readonly parameterizedFormValidationsService: ParameterizedFormValidationsService,
	) { }

	ngOnDestroy(): void {
		this.formSubscription.unsubscribe();
	}

	get parameterFormArray(): FormArray {
		return this.form.get('parameters') as FormArray;
	}

	setData(numericalParameterData: NumericalParameterData[]) {
		this._amountOfInputs = getElementAtPosition<NumericalParameterData>(numericalParameterData, 0).inpuntCount;
		this.numericalParametersSelect = numericalParameterData.map(parameter => this.toParameterUnitOfMeasure(parameter));
		this.numericalParameterData = numericalParameterData;
	}

	buildForm() {
		const formGroups = Array.from({ length: this._amountOfInputs }, () => this.createEmptyNumericParameterForm());
    	formGroups.forEach(formGroup => this.parameterFormArray.push(formGroup));

		this.isAmountInputsSameThatAmountUnitOfMeasure = this._amountOfInputs === this.numericalParametersSelect?.length;

		if (this.isAmountInputsSameThatAmountUnitOfMeasure) {
			this.parameterFormArray.controls.forEach((formGroup: FormGroup, index) => {
				const parameterId = getElementAtPosition<NumericalParameter>(this.numericalParametersSelect, index).parameterId;
				const code = getElementAtPosition<NumericalParameter>(this.numericalParametersSelect, index).code;
				formGroup.controls.numericalParameter.setValue({ parameterId, code });
			});
		}
	}

	compareNumericalParameter(numericalParameter1: NumericalParameter, numericalParameter2: NumericalParameter) {
		const hasValue = numericalParameter1 && numericalParameter2;
		return hasValue ? numericalParameter1.parameterId === numericalParameter2.parameterId : '';
	}

	updateOptionEnablement() {
		const selectedParameters: NumericalParameter[] = this.parameterFormArray.controls.map(group => group.value.numericalParameter);

		this.numericalParametersSelect = this.numericalParametersSelect.map(allParam => ({
			...allParam,
			disabled: !!selectedParameters.find(selectedParam => selectedParam?.parameterId === allParam?.parameterId)
		}));
	}

	private createEmptyNumericParameterForm(): FormGroup<NumericalParameterForm> {
		return new FormGroup<NumericalParameterForm>({
			numberValue: new FormControl<number>(null, Validators.pattern(PATTERN_NUMBER_WITH_DECIMALS)),
			numericalParameter: new FormControl<NumericalParameter>(null)
		});
	}

	private subscribeToFormChanges() {
		this.formSubscription = this.parameterFormArray.valueChanges.subscribe(formChanges => this.emitNumericValues());
	}

	private toParameterUnitOfMeasure(numericalParameter: NumericalParameterData): NumericalParameter {
		return {
			parameterId: numericalParameter.parameterId,
			code: numericalParameter.unitOfMeasureCode,
			disabled: false,
		}
	}

	private emitNumericValues() {
		this.parameterizedFormValidationsService.isNumericalParameterFormValid = this.parameterFormArray.valid;
		const numericalValuesToEmit: NumericalParameterValues[] = this.parameterFormArray.controls
			.filter(formGroup => formGroup.valid && formGroup.value.numberValue && formGroup.value.numericalParameter?.parameterId)
			.map(formGroup => this.toNumericalParameterValues(formGroup.value.numericalParameter.parameterId, formGroup.value.numberValue));

		if (numericalValuesToEmit.length)
			this.numericalValues.emit(numericalValuesToEmit);
	}

	private toNumericalParameterValues(parameterId: number, value: number): NumericalParameterValues {
		return { parameterId, value }
	}

}

export interface NumericalParameterValues {
	parameterId: number;
	value: number;
}

export interface NumericalParameterData {
	parameterId: number;
	inpuntCount: number;
	unitOfMeasureCode: string;
}

interface NumericalParameter {
	parameterId: number;
	code: string;
	disabled?: boolean;
}

interface NumericalParameterForm {
	numberValue: FormControl<number>,
	numericalParameter: FormControl<NumericalParameter>
}