import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
import { Templates } from '../control-select-template/control-select-template.component';
import { LoincObservationValue } from 'projects/hospital/src/app/modules/hsi-components/loinc-form/loinc-input.model';

@Component({
	selector: 'app-select-template',
	templateUrl: './select-template.component.html',
	styleUrls: ['./select-template.component.scss'],
})

export class SelectTemplateComponent implements OnInit {

	selectedTemplate$: BehaviorSubject<ProcedureTemplateFullSummaryDto> = new BehaviorSubject<ProcedureTemplateFullSummaryDto>(null);
	@Input() template: Templates;
	@Output() selectedTemplateEmit: EventEmitter<ProcedureTemplateFullSummaryDto> = new EventEmitter<ProcedureTemplateFullSummaryDto>();
	@Output() changeValuesEmit: EventEmitter<ResultTemplate> = new EventEmitter<ResultTemplate>();
	constructor() { }

	ngOnInit() {
		if (this.template.preloadedSelectedTemplate) {
			this.onSelect(this.template.preloadedSelectedTemplate, this.template.id);

			const result: ResultTemplate = {
				form: this.template.preloadedSelectedTemplate,
				idDiagnostic: this.template.id
			}
			this.changeValuesEmit.emit(result);
		}
	}

	onSelect(selectedProcedureTemplate: ProcedureTemplateFullSummaryDto, diagnosticId: number) {
		this.selectedTemplate$.next(null);

		setTimeout(() => {
			this.selectedTemplate$.next(selectedProcedureTemplate);
		});

		this.selectedTemplateEmit.emit(selectedProcedureTemplate);
	}

	changeValues($event, idDiagnostic: number) {
		const result: ResultTemplate = {
			form: $event?.values,
			idDiagnostic: idDiagnostic
		}
		this.changeValuesEmit.emit(result);
	}

}
export interface ResultTemplate {
	idDiagnostic: number;
	form: LoincObservationValue | ProcedureTemplateFullSummaryDto;
}
