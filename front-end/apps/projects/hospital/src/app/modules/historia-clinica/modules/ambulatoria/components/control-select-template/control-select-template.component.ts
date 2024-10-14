import { Component, Input, OnInit } from '@angular/core';
import { ControlTemplatesService, ResultTemplate } from '../../services/control-templates.service';
import { GetDiagnosticReportObservationGroupDto, ProcedureParameterFullSummaryDto, ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
import { Subject, forkJoin, switchMap } from 'rxjs';
import { StudyInfo } from '../../services/study-results.service';
import { ProcedureTemplatesService } from '@api-rest/services/procedure-templates.service';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { ButtonService } from '../../services/button.service';

@Component({
	selector: 'app-control-select-template',
	templateUrl: './control-select-template.component.html',
	styleUrls: ['./control-select-template.component.scss']
})
export class ControlSelectTemplateComponent implements OnInit {
	customForms$ = new Subject<Templates[] | null>();
	@Input() studies: StudyInfo[];
	@Input() patientId: number;

	constructor(
		readonly controlTemplatesService: ControlTemplatesService,
		private procedureTemplatesService: ProcedureTemplatesService,
		private prescripcionesService: PrescripcionesService,
		readonly buttonService: ButtonService,

	) { }

	ngOnInit() {

		let currentFormsSet: Templates[] = [];

		this.studies?.forEach((study: StudyInfo) => {

			this.getProcedureTemplatesService(study.idDiagnostic).subscribe(
				(procedureTemplates: ProcedureTemplateFullSummaryDto[]) => {

					this.prescripcionesService.showStudyResultsWithFormTempalte(this.patientId, study.idDiagnostic).subscribe(
						(getDiagnosticReportObservationGroupDto: GetDiagnosticReportObservationGroupDto) => {

							currentFormsSet.push({
								name: study.snomed.pt,
								id: study.idDiagnostic,
								form: procedureTemplates,
								preloadValues: getDiagnosticReportObservationGroupDto?.procedureTemplateId ? this.getPreloadValues(procedureTemplates, getDiagnosticReportObservationGroupDto) : null,
								preloadedSelectedTemplate: procedureTemplates.find((e: ProcedureTemplateFullSummaryDto) => e.id === getDiagnosticReportObservationGroupDto.procedureTemplateId)
							});

							this.customForms$.next(currentFormsSet);
						});
				}
			);
		});

	}



	selectedProcedureTemplate(selectedProcedureTemplate: ProcedureTemplateFullSummaryDto, diagnosticId: number) {
		this.controlTemplatesService.setForm(selectedProcedureTemplate, diagnosticId);
	}

	changeValues($event: ResultTemplate) {
		setTimeout(() => {
			this.buttonService.activatePartialSaveButton();
		})
		this.controlTemplatesService.changeValues($event);
	}

	private getProcedureTemplatesService(idDiagnostic: number): any {
		return this.procedureTemplatesService.findByDiagnosticReportId(idDiagnostic).pipe(
			switchMap((practiceTemplates: ProcedureTemplateFullSummaryDto[]) => {
				let observables = practiceTemplates.map(t => this.procedureTemplatesService.findById(t.id));
				return forkJoin(observables);
			})
		);
	}

	private getTypeInput(dataList: ProcedureParameterFullSummaryDto[], id: number): number | null {
		for (const item of dataList) {
			if (item.id === id) {
				return item.typeId;
			}
		}
		return null;
	}

	private getPreloadValues(procedureTemplates: ProcedureTemplateFullSummaryDto[], getDiagnosticReportObservationGroupDto: GetDiagnosticReportObservationGroupDto): ProcedureTemplate {
		const procedureTemplate = procedureTemplates.find(e => e.id === getDiagnosticReportObservationGroupDto.procedureTemplateId);

		return {
			procedureTemplateId: getDiagnosticReportObservationGroupDto.procedureTemplateId,
			observations: getDiagnosticReportObservationGroupDto.observations.map(observation => ({
				id: observation.id,
				typeIput: this.getTypeInput(procedureTemplate.parameters, observation.procedureParameterId),
				procedureParameterId: observation.procedureParameterId,
				unitOfMeasureId: observation.unitOfMeasureId,
				snomedSctid: observation.snomedSctid,
				snomedPt: observation.snomedPt,
				value: observation.value
			}))
		};
	}

}

export interface Templates {
	name: string,
	id: number,
	form: ProcedureTemplateFullSummaryDto[],
	preloadValues: ProcedureTemplate,
	preloadedSelectedTemplate: ProcedureTemplateFullSummaryDto
}

export interface ProcedureTemplate {
	procedureTemplateId: number,
	observations: PreloadValues[]
}

interface PreloadValues {
	id: number;
	typeIput: number;
	procedureParameterId: number;
	unitOfMeasureId: number;
	value: string;
}
