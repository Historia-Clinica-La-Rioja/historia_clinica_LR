import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
import { LoincFormValues, LoincInput } from '../../loinc-input.model';
import { dtoToLoincInput } from '../../utils/dto-loinc-input.mapper';

@Component({
	selector: 'app-procedure-template-form',
	templateUrl: './procedure-template-form.component.html',
	styleUrls: ['./procedure-template-form.component.scss']
})
export class ProcedureTemplateFormComponent {
	@Input() preload: ProcedureTemplateFullSummaryDto;
	@Output() valueChange: EventEmitter<LoincFormValues> = new EventEmitter<LoincFormValues>();
	@Input() values: LoincFormValues;
	loincForm: LoincInput[];

	@Input()
	set template(ProcedureTemplate: ProcedureTemplateFullSummaryDto) {

		let _preload = makeProcedureParameterIdUniquePreload(this.preload);

		let params = makeProcedureParameterIdUnique(ProcedureTemplate);

		if (params?.parameters) {
			const dtli = params.parameters.map(param => dtoToLoincInput(param, _preload)).sort((a, b) => a.order - b.order);

			this.loincForm = dtli;
		}

		function makeProcedureParameterIdUnique(obj) {
			const idCount = {};
			obj?.parameters.forEach((param) => {
				const id = param.id;
				if (idCount[id]) {
					idCount[id]++;
					param.id = `${id}_${idCount[id]}`;
					param.loincCode.customDisplayName = '';
					param.loincCode.description = '';
				} else {
					idCount[id] = 1;
				}
			});
			return obj;
		}

		function makeProcedureParameterIdUniquePreload(obj) {
			const idCount = {};

			obj?.observations.forEach(observation => {
				let id = observation.procedureParameterId;

				if (idCount[id]) {
					idCount[id]++;
					observation.procedureParameterId = `${id}_${idCount[id]}`;
					observation.customDisplayName = ' ';
				} else {
					idCount[id] = 1;
				}
			});

			return obj;
		}

	}

	changeValues($event) {
		this.valueChange.emit($event);
	}

}
