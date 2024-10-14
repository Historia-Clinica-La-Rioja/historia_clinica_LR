import { FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { LoincFormValues, LoincInput, LoincObservationValue } from '../../loinc-input.model';
import { BehaviorSubject, Observable } from 'rxjs';

const isEquals = (one: LoincObservationValue, two: LoincObservationValue) => {
	return one.unitOfMeasureId === two.unitOfMeasureId && one.value === two.value;
}

export class LoincFormControlService {
	form!: FormGroup;
	loincInputs: LoincInput[];
	private valuesSubject: BehaviorSubject<LoincFormValues>;
	private currentValues: FormValuesService;

	constructor(loincForm: LoincInput[] = [], initialValues: LoincFormValues) {
		this.valuesSubject = new BehaviorSubject<LoincFormValues>(undefined);
		this.currentValues = new FormValuesService(initialValues?.values || []);
		this.loincInputs = loincForm.map(loincInput => {
			const procedureParameterId: number = loincInput.param.id;
			const inputValue = this.currentValues.getById(procedureParameterId);
			return {
				...loincInput,
				observationValue: inputValue,
			};
		});
		this.form = this.toForm(this.loincInputs);
	}

	setValue($event: LoincObservationValue) {
		const hasChanged = this.currentValues.update($event);
		this.valuesSubject.next({
			hasChanged,
			values: this.currentValues.values(),
		});
	}

	get valueChange$(): Observable<LoincFormValues> {
		return this.valuesSubject.asObservable();
	}

	private toForm(loincInputs: LoincInput[] | null = []): FormGroup {
		const group: any = {};

		loincInputs.forEach(loincInput => {
			const initialValue = loincInput.observationValue?.value || '';
			group[loincInput.key] = new FormControl(initialValue, { validators: this.toValidators(loincInput) });
		});
		return new FormGroup(group);
	}

	private toValidators(loincInput: LoincInput): ValidatorFn[] {
		let validators = [];
		if (loincInput.required) {
			validators.push(Validators.required);
		};
		if (loincInput.type === 'email') {
			validators.push(Validators.email);
		};
		return validators;
	}



}

class FormValuesService {
	private changedValues: LoincObservationValue[] = [];

	constructor(
		private initialValues: LoincObservationValue[],
	) {

	}

	getById(id: number): LoincObservationValue {
		const idMatcher = (lov: LoincObservationValue) => lov.procedureParameterId === id;
		return this.changedValues.find(idMatcher) || this.initialValues.find(idMatcher);
	}

	update(newValue: LoincObservationValue): boolean {
		const idMatcher = (lov: LoincObservationValue) => lov.procedureParameterId === newValue.procedureParameterId;
		this.changedValues = this.changedValues.filter(lov => !idMatcher(lov));
		const initialValue: LoincObservationValue = this.initialValues.find(idMatcher);
		if (!initialValue || !isEquals(initialValue, newValue)) {
			this.changedValues.push({
				procedureParameterId: newValue.procedureParameterId,
				value: newValue.value,
				unitOfMeasureId: newValue.unitOfMeasureId,
				snomedPt: newValue?.snomedPt,
				snomedSctid: newValue?.snomedSctid
			});
		}
		return !!this.changedValues.length; // hasChange
	}

	values(): LoincObservationValue[] {
		const unchangedValues = this.initialValues.filter(
			v => !this.changedValues.find(chdVal => v.procedureParameterId === chdVal.procedureParameterId)
		)
		return [...unchangedValues, ...this.changedValues];
	}
}
