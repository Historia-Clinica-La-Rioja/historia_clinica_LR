import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject, Subject, forkJoin, switchMap } from 'rxjs';
import { AddDiagnosticReportObservationsCommandDto, CompleteRequestDto, DiagnosticReportInfoDto, ProcedureTemplateFullSummaryDto, ProcedureTemplateShortSummaryDto } from '@api-rest/api-model';
import { MatDialogRef } from '@angular/material/dialog';
import { ButtonService } from '../../services/button.service';
import { StudyInformation } from '../../modules/estudio/components/study/study.component';
import { ToFormGroup } from '@core/utils/form.utils';
import { ProcedureTemplatesService } from '@api-rest/services/procedure-templates.service';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { StudyInfo } from '../../services/study-results.service';
import { LoincObservationValue } from 'projects/hospital/src/app/modules/hsi-components/loinc-form/loinc-input.model';

@Component({
	selector: 'app-complete-info',
	templateUrl: './complete-info.component.html',
	styleUrls: ['./complete-info.component.scss']
})
export class CompleteInfoComponent implements OnInit {
	selectedFiles: File[] = [];
	formStudyClosure;
	selectedTemplate$: BehaviorSubject<ProcedureTemplateFullSummaryDto[]> = new BehaviorSubject<ProcedureTemplateFullSummaryDto[]>([]);
	arrayIdTemplatesProcedures = [];
	formSet$ = new Subject<any | null>();
	formTemplateValues: ValueMap = {};
	@Input() patientId: number;
	@Input() diagnosticReport: DiagnosticReportInfoDto[] | StudyInformation[];
	@Input() studies: StudyInfo[];
	constructor(
		public dialogRef: MatDialogRef<CompleteInfoComponent>,
		private prescripcionesService: PrescripcionesService,
		readonly buttonService: ButtonService,
		private procedureTemplatesService: ProcedureTemplatesService,

	) { }

	ngOnInit() {

		this.formStudyClosure = new FormGroup<ToFormGroup<FormStudyClosure>>({
			description: new FormControl('', [Validators.required])
		})

		this.formStudyClosure.valueChanges.subscribe(_ =>
			this.buttonService.updateFormStatus(!this.formStudyClosure.valid)
		);

		this.buttonService.submit$.subscribe(submit => {
			if (submit)
				this.completeStudy();
		});

		let currentFormSet = [];

		this.studies.forEach((e: StudyInfo) => {
			this.getProcedureTemplatesService(e.idDiagnostic).subscribe(
				(procedureTemplates: any) => {
					currentFormSet.push({
						name: e.snomed.pt,
						id: e.idDiagnostic,
						form: procedureTemplates
					});
					this.formSet$.next(currentFormSet);
				}
			);
		});

	}

	setSelectedFilesEmiit($event: File[]) {
		this.selectedFiles = $event;
	}

	changeValues($event, idDiagnostic: number) {
		if ($event?.values) {
			this.formTemplateValues[idDiagnostic] = $event.values;
		}
	}

	onSelect(selectedProcedureTemplate: any, index: number, diagnosticId: number) {

		this.arrayIdTemplatesProcedures.push({ diagnosticId: diagnosticId, selectedrocedureTemplateId: selectedProcedureTemplate.id });

		const selectedTemplates = this.selectedTemplate$.value.slice();

		this.selectedTemplate$.next(null);

		selectedTemplates[index] = selectedProcedureTemplate;

		setTimeout(() => {
			this.selectedTemplate$.next(selectedTemplates);
		});
	}

	private completeStudy() {
		const completeRequest: CompleteRequestDto = {
			observations: this.formStudyClosure.controls.description.value,
		};

		forkJoin(
			this.diagnosticReport.map(report => {

				const reportInfo: DiagnosticReportInfoDto = report?.diagnosticInformation || report;

				let template = this.getProcedureTemplateId(reportInfo.id);
				let reportObservations: AddDiagnosticReportObservationsCommandDto = this.buildProcedureTemplateFullSummaryDto(reportInfo.id, template);

				if (reportObservations?.procedureTemplateId && reportObservations?.values?.length > 0) {
					return this.prescripcionesService.completeStudyTemplateWhithForm(this.patientId,
						reportInfo.id, completeRequest, this.selectedFiles, reportObservations)
				}
				else {
					return this.prescripcionesService.completeStudy(this.patientId, reportInfo.id, completeRequest, this.selectedFiles)
				}

			})).subscribe(
				() => {
					this.closeModal(false, true);
				}, _ => {
					this.closeModal(false, false);
				});
	}

	private closeModal(simpleClose: boolean, completed?: boolean): void {
		this.dialogRef.close(simpleClose ? null : { completed });
	}

	private buildProcedureTemplateFullSummaryDto(idDiagnostic: number, template): AddDiagnosticReportObservationsCommandDto {
		return {
			isPartialUpload: false,
			procedureTemplateId: template,
			values: Object.keys(this.formTemplateValues).length > 0 ? this.buildAddDiagnosticReportObservationsCommandDto(idDiagnostic) : [],
		}
	}

	private getProcedureTemplateId(selectedProcedureTemplate: number): number {
		let a = this.arrayIdTemplatesProcedures.filter(a => a.diagnosticId === selectedProcedureTemplate);
		return a.length ? a[0].selectedrocedureTemplateId : null
	}

	private buildAddDiagnosticReportObservationsCommandDto(idDiagnostic: number): any {
		let procedure = this.formTemplateValues[idDiagnostic];
		return this.cleanProcedureParameterIds(procedure);
	}

	cleanProcedureParameterIds(data): AddDiagnosticReportObservationsCommandDto {
		return data.map((item) => {
			if (typeof item.procedureParameterId === 'string' && item.procedureParameterId.includes('_')) {
				item.procedureParameterId = item.procedureParameterId.split('_')[0];
			}
			return item;
		});
	}

	private getProcedureTemplatesService(idDiagnostic: number): any {
		return this.procedureTemplatesService.findByDiagnosticReportId(idDiagnostic).pipe(
			switchMap((practiceTemplates: ProcedureTemplateShortSummaryDto[]) => {
				let observables = practiceTemplates.map(t => this.procedureTemplatesService.findById(t.id));
				return forkJoin(observables);
			})
		);
	}

}

interface FormStudyClosure {
	description: string;
}

interface ValueMap {
	[idDiagnostic: string]: LoincObservationValue;
}
