import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
import { LoincObservationValue } from 'projects/hospital/src/app/modules/hsi-components/loinc-form/loinc-input.model';
import { Templates } from '@historia-clinica/modules/ambulatoria/components/control-select-template/control-select-template.component';

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
			form: this.cleanProcedureParameterIds($event?.values),
			idDiagnostic: idDiagnostic
		}
		this.changeValuesEmit.emit(result);
	}

	private cleanProcedureParameterIds(data): LoincObservationValue | ProcedureTemplateFullSummaryDto {
		return data?.map((item) => {
			if (item) {
				if (typeof item.procedureParameterId === 'string' && item.procedureParameterId.includes('_')) {
					item.procedureParameterId = item.procedureParameterId.split('_')[0];
				}
				return item;
			}
		});
	}

}
export interface ResultTemplate {
	idDiagnostic: number;
	form: LoincObservationValue | ProcedureTemplateFullSummaryDto;
}
