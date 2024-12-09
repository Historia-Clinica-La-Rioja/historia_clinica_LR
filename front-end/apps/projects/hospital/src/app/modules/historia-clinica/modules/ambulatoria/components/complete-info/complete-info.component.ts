import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { forkJoin, of, switchMap } from 'rxjs';
import {
	AddDiagnosticReportObservationsCommandDto, DiagnosticReportInfoDto,
	CompleteRequestDto
} from '@api-rest/api-model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ButtonService } from '../../services/button.service';
import { StudyInformation } from '../../modules/estudio/components/study/study.component';
import { ToFormGroup } from '@core/utils/form.utils';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { StudyInfo } from '../../services/study-results.service';
import { ControlTemplatesService } from '../../services/control-templates.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-complete-info',
	templateUrl: './complete-info.component.html',
	styleUrls: ['./complete-info.component.scss'],
	providers: [ControlTemplatesService]
})
export class CompleteInfoComponent implements OnInit {
	selectedFiles: File[] = [];
	formStudyClosure;
	@Input() patientId: number;
	@Input() diagnosticReport: DiagnosticReportInfoDto[] | StudyInformation[];
	@Input() studies: StudyInfo[];
	constructor(
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<CompleteInfoComponent>,
		private prescripcionesService: PrescripcionesService,
		readonly buttonService: ButtonService,
		readonly controlTemplatesService: ControlTemplatesService,
		readonly snackBarService: SnackBarService
	) { }

	ngOnInit() {

		this.formStudyClosure = new FormGroup<ToFormGroup<FormStudyClosure>>({
			description: new FormControl('', [Validators.required])
		})

		this.formStudyClosure.valueChanges.pipe(
			switchMap(() =>
				this.buttonService.formDisabledPartialSave$
			)
		).subscribe((disabled: boolean) => {
			const isFormValid = this.formStudyClosure.valid;
			this.buttonService.updateFormStatus(!isFormValid || disabled);
		});

		this.buttonService.submit$.subscribe(submit => {
			if (submit)
				this.completeStudy();
		});

		this.buttonService.submitPartialSave$.subscribe(submitPartialSlave => {
			if (submitPartialSlave)
				this.savedPartialStudy();
		});

	}

	setSelectedFilesEmiit($event: File[]) {
		this.selectedFiles = $event;
	}

	private completeStudy() {
		const completeRequest: CompleteRequestDto = {
			observations: this.formStudyClosure.controls.description.value,
		};

		forkJoin(
			this.diagnosticReport.map(report => {

				const reportInfo: DiagnosticReportInfoDto = report?.diagnosticInformation || report;
				let isPartialUpload = false;

				let reportObservations: AddDiagnosticReportObservationsCommandDto =
					this.controlTemplatesService.build(reportInfo.id, isPartialUpload);

				if (reportObservations?.procedureTemplateId && reportObservations?.values?.length > 0) {
					return this.prescripcionesService.completeStudyTemplateWhithForm(this.patientId,
						reportInfo.id, completeRequest, this.selectedFiles, reportObservations)
				}
				else {
					return this.prescripcionesService.completeStudy(this.patientId, reportInfo.id, completeRequest, this.selectedFiles)
				}

			})).subscribe(
				() => {
					this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_SUCCESS');
					this.closeModal(false, true);
				}, _ => {
					this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_ERROR');
					this.closeModal(false, false);
				});
	}


	private partialSave() {
		forkJoin(
			this.diagnosticReport.map(report => {

				const reportInfo: DiagnosticReportInfoDto = report?.diagnosticInformation || report;

				let template = this.controlTemplatesService.getProcedureTemplateId(reportInfo.id);

				let isPartialUpload = true;

				let reportObservations: AddDiagnosticReportObservationsCommandDto =
					this.controlTemplatesService.build(reportInfo.id, isPartialUpload);

				if (reportObservations?.procedureTemplateId && reportObservations?.values?.length > 0 && !!template) {
					return this.prescripcionesService.partialStudyTemplateWhithForm(this.patientId,
						reportInfo.id, reportObservations)
				} else {
					return of(null);
				}

			})).subscribe(
				() => {
					this.snackBarService.showSuccess('ambulatoria.complete-info.SUCCESS');
					this.closeModal(false, true);
				}, _ => {
					this.snackBarService.showSuccess('ambulatoria.complete-info.ERROR');
					this.closeModal(false, false);
				});

	}

	private closeModal(simpleClose: boolean, completed?: boolean): void {
		this.dialogRef.close(simpleClose ? null : { completed });
	}

	private savedPartialStudy() {
		if (this.formStudyClosure.valid) {
			const warnignComponent = this.dialog.open(DiscardWarningComponent,
				{
					disableClose: false,
					data: {
						title: 'ambulatoria.complete-info.TITLE',
						content: 'ambulatoria.complete-info.CONTENT',
						okButtonLabel: 'ambulatoria.complete-info.OK_BUTTON',
						cancelButtonLabel: 'ambulatoria.complete-info.CANCEL'
					},
					maxWidth: '500px'
				});
			warnignComponent.afterClosed().subscribe(confirmed =>
				confirmed ? this.partialSave() : this.buttonService.resetLoadingPartialSave()
			);
		}
		else
			this.partialSave()

	}

}
interface FormStudyClosure {
	description: string;
}
