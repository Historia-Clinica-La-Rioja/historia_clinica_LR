import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProcedureParameterUnitOfMeasureFullSummaryDto } from '@api-rest/api-model';

@Component({
	selector: 'app-observation-input-unit',
	templateUrl: './observation-input-unit.component.html',
	styleUrls: ['./observation-input-unit.component.scss']
})
export class ObservationInputUnitComponent {
	@Input() value: ProcedureParameterUnitOfMeasureFullSummaryDto;
	@Input() list: ProcedureParameterUnitOfMeasureFullSummaryDto[];
	@Output() valueSelected: EventEmitter<ProcedureParameterUnitOfMeasureFullSummaryDto> = new EventEmitter<ProcedureParameterUnitOfMeasureFullSummaryDto>();
}
