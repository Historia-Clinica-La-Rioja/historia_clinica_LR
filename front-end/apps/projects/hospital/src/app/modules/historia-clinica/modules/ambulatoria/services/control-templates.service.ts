import { Injectable } from '@angular/core';
import { ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';
import { LoincObservationValue } from '../../../../hsi-components/loinc-form/loinc-input.model';

@Injectable()
export class ControlTemplatesService {

	selectedTemplate$: BehaviorSubject<ProcedureTemplateFullSummaryDto> = new BehaviorSubject<ProcedureTemplateFullSummaryDto>(null);
	formTemplateValues: ValueMap = {};
	arrayIdTemplatesProcedures = [];

	setForm(selectedProcedureTemplate: ProcedureTemplateFullSummaryDto, diagnosticId: number) {

		this.setDiagnosticRelateTemplate(selectedProcedureTemplate, diagnosticId);

		if (selectedProcedureTemplate?.id) {

			this.selectedTemplate$.next(null);
			setTimeout(() => {
				this.selectedTemplate$.next(selectedProcedureTemplate);
			});
		}

	}

	changeValues($event: ResultTemplate) {

		let idDiagnostic = $event?.idDiagnostic;
		if ($event?.form) {
			this.formTemplateValues[idDiagnostic] = $event.form;
		}
	}

	getProcedureTemplateId(diagnosticId: number): number {
		return this.arrayIdTemplatesProcedures.find(a => a.diagnosticId === diagnosticId)?.selectedProcedureTemplateId;
	}

	getFormTemplateValues() {
		return this.formTemplateValues
	}

	private setDiagnosticRelateTemplate(selectedProcedureTemplate: ProcedureTemplateFullSummaryDto, diagnosticId: number) {
		const templateId = selectedProcedureTemplate?.id || null;

		const existingItem = this.arrayIdTemplatesProcedures.find(item => item.diagnosticId === diagnosticId);

		if (existingItem) {
			existingItem.selectedProcedureTemplateId = templateId;
		} else {
			this.arrayIdTemplatesProcedures.push({
				diagnosticId: diagnosticId,
				selectedProcedureTemplateId: templateId
			});
		}
	}

}
export interface ResultTemplate {

	idDiagnostic: number;
	form: LoincObservationValue;
}


interface ValueMap {
	[idDiagnostic: string]: LoincObservationValue; //estsa bien que sea un string y
}
