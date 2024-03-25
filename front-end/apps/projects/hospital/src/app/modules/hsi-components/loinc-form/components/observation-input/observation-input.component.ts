import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

import {
	LoincInput,
	LoincObservationValue,
} from '../../loinc-input.model';
import { ProcedureParameterUnitOfMeasureFullSummaryDto, SnomedDto } from '@api-rest/api-model';

const findByIdOrFirst = (
	unitSelectedId: number,
	unitList: ProcedureParameterUnitOfMeasureFullSummaryDto[] = [],
): ProcedureParameterUnitOfMeasureFullSummaryDto => {
	const unitSelected = unitList.find(u => u.unitOfMeasureId === unitSelectedId);
	return unitSelected?.unitOfMeasureId ? unitSelected : unitList[0];
}


@Component({
  selector: 'app-observation-input',
  templateUrl: './observation-input.component.html',
  styleUrls: ['./observation-input.component.scss']
})
export class ObservationInputComponent implements OnInit {

	@Input() loincInput!: LoincInput;
	@Input() form!: FormGroup;
	@Output() valueChange: EventEmitter<LoincObservationValue> = new EventEmitter<LoincObservationValue>();

	unitOfMeasure: ProcedureParameterUnitOfMeasureFullSummaryDto;

	ngOnInit() {
		this.unitOfMeasure = findByIdOrFirst(
			this.loincInput.observationValue?.unitOfMeasureId,
			this.loincInput.param.unitsOfMeasure,
		);

		this.form.controls[this.loincInput.key].valueChanges.subscribe(
			value => {
				this.emitValue(value);
			}
		)
	}

	unitSelected(unitOfMeasure: ProcedureParameterUnitOfMeasureFullSummaryDto) {
		this.unitOfMeasure = unitOfMeasure;
		this.emitValue(
			this.form.controls[this.loincInput.key].value
		);
	}

	setValue(controlName: string, value: SnomedDto | string | any) {
		this.form.controls[controlName].setValue(value);
	}

	get isValid() {
		return this.form.controls[this.loincInput.key].valid;
	}
	get validationErrors() {
		return this.form.controls[this.loincInput.key]?.errors;
	}

	private emitValue(value: string) {
		const procedureParameter = this.loincInput.param;

		this.valueChange.emit({
			procedureParameterId: procedureParameter.id,
			value,
			unitOfMeasureId: this.unitOfMeasure?.unitOfMeasureId,
		});
	}

}
