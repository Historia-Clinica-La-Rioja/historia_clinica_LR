import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

import {
	LoincInput,
	LoincObservationValue,
} from '../../loinc-input.model';
import { ProcedureParameterUnitOfMeasureFullSummaryDto, SnomedDto } from '@api-rest/api-model';
import { NumberWithUnit } from '../loinc-input-number/loinc-input-number.component';


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

	setValue(controlName: string, value: SnomedDto | string) {
		this.form.controls[controlName].setValue(value);
	}

	emitValueNumber($event: NumberWithUnit) {
		const procedureParameter = this.loincInput.param;
		this.valueChange.emit({
			procedureParameterId: procedureParameter.id,
			value: $event.value,
			unitOfMeasureId: $event.unitOfMeasureId,
			valueNumeric: $event.valueNumeric,
			isValid: $event.isValid
		});
	}

	emitValueSnomed($event: SnomedDto) {
		const procedureParameter = this.loincInput.param;
		this.valueChange.emit({
			procedureParameterId: procedureParameter.id,
			value: null,
			unitOfMeasureId: null,
			snomedPt: $event.pt,
		 	snomedSctid: $event.sctid,
			valueNumeric: null
		});
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
			unitOfMeasureId: null,
			valueNumeric: null
		});
	}

}
