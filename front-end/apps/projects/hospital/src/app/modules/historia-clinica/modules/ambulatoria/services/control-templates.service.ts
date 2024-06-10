import { Injectable } from '@angular/core';
import { AddDiagnosticReportObservationsCommandDto, ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
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

	build(reportInfoId: number, isPartialUpload: boolean): AddDiagnosticReportObservationsCommandDto {
		let template = this.getProcedureTemplateId(reportInfoId);

		let formTemplateValues = this.getFormTemplateValues();

		let reportObservations: AddDiagnosticReportObservationsCommandDto =
			this.buildProcedureTemplateFullSummaryDto(reportInfoId, template, formTemplateValues, false);

		return reportObservations;
	}

	private buildProcedureTemplateFullSummaryDto(idDiagnostic: number, template, formTemplateValues, isPartialUpload?: boolean): AddDiagnosticReportObservationsCommandDto {
		return {
			isPartialUpload: isPartialUpload || false,
			referenceClosure: null,
			procedureTemplateId: template,
			values: Object.keys(formTemplateValues).length > 0 ? this.buildAddDiagnosticReportObservationsCommandDto(idDiagnostic, formTemplateValues) : [],
		}
	}

	private buildAddDiagnosticReportObservationsCommandDto(idDiagnostic: number, formTemplateValues): any {
		let procedure = formTemplateValues[idDiagnostic];
		return this.cleanProcedureParameterIds(procedure);
	}

	private cleanProcedureParameterIds(data): AddDiagnosticReportObservationsCommandDto {
		return data?.map((item) => {
			if (item) {
				if (typeof item.procedureParameterId === 'string' && item.procedureParameterId.includes('_')) {
					item.procedureParameterId = item.procedureParameterId.split('_')[0];
				}
				return item;
			}
		});
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
	[idDiagnostic: string]: LoincObservationValue;
}
