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
	@Output() valueChange: EventEmitter<LoincFormValues> = new EventEmitter<LoincFormValues>();
	@Input() values: LoincFormValues;
	loincForm: LoincInput[];

	constructor() { }

	@Input()
	set template(t: ProcedureTemplateFullSummaryDto) {

		if (t?.parameters) {
			let dtli = t.parameters.map(dtoToLoincInput).sort((a, b) => a.order - b.order);

			this.loincForm = fixDuplicateIds(dtli);

			function fixDuplicateIds(data) {
				const keyMap = {};
				const idMap = {};
				data.forEach(obj => {
					let originalKey = obj.key;
					let count = 1;
					while (keyMap[obj.key]) {
						obj.key = originalKey + `_${count}`;
						obj.label = null;
						count++;
					}
					keyMap[obj.key] = true;

					const originalId = obj.param.id;
					count = 1;
					while (idMap[obj.param.id]) {
						obj.param.id = originalId + `_${count}`;
						count++;
					}
					idMap[obj.param.id] = true;
				});
				return data;
			}
		}
	}


	changeValues($event) {
		this.valueChange.emit($event);
	}

}

