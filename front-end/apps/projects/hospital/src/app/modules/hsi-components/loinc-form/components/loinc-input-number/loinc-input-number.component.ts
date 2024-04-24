import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProcedureParameterUnitOfMeasureFullSummaryDto } from '@api-rest/api-model';
import { LoincInput } from '../../loinc-input.model';
import { FormGroup } from '@angular/forms';

@Component({
	selector: 'app-loinc-input-number',
	templateUrl: './loinc-input-number.component.html',
	styleUrls: ['./loinc-input-number.component.scss']
})

export class LoincInputNumberComponent {

	@Input() loincInput: LoincInput;
	@Input() form!: FormGroup;
	@Input() unitOfMeasure: ProcedureParameterUnitOfMeasureFullSummaryDto;
	@Output() valueSelected: EventEmitter<ProcedureParameterUnitOfMeasureFullSummaryDto> = new EventEmitter<ProcedureParameterUnitOfMeasureFullSummaryDto>();

}
