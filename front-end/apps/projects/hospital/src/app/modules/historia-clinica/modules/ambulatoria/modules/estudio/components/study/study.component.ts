import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { CompletarEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { VerResultadosEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { Content, Title } from '@presentation/components/indication/indication.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-study',
	templateUrl: './study.component.html',
	styleUrls: ['./study.component.scss']
})
export class StudyComponent {

	@Input() studies: DiagnosticReportInfoDto[];
	@Input() studyHeader: Title;
	@Input() patientId: number;
	STUDY_STATUS = STUDY_STATUS;

	constructor(
		private readonly prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService,
		private snackBarService: SnackBarService,
		private readonly dialog: MatDialog
	) { }

	contentBuilder(diagnosticReport: DiagnosticReportInfoDto): Content {
		const prescriptionStatus = this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId);
		return {
			status: {
				description: prescriptionStatus,
				cssClass: prescriptionStatus === this.translateService.instant('ambulatoria.paciente.studies.study_state.PENDING') ? 'red' : 'blue'
			},
			description: diagnosticReport.snomed.pt,
			extra_info: [{
				title: 'Razón:',
				content: diagnosticReport.healthCondition.snomed.pt
			}],
			createdBy: diagnosticReport.doctor.firstName + " " + diagnosticReport.doctor.lastName,
			timeElapsed: '06/05/98 - 12:00 hs.'
		}
	}

	completeStudy(diagnosticReport: DiagnosticReportInfoDto) {
		const newCompleteStudy = this.dialog.open(CompletarEstudioComponent,
			{
				data: {
					diagnosticReport,
					patientId: this.patientId,
				},
				width: '35%',
			});

		newCompleteStudy.afterClosed().subscribe((completed: any) => {
				if (completed) {
					if (completed.completed)
						this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_SUCCESS');
					else
						this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_ERROR');
				}
			});
	}

	showStudyResults(diagnosticReport: DiagnosticReportInfoDto) {
		this.dialog.open(VerResultadosEstudioComponent,
			{
				data: {
					diagnosticReport,
					patientId: this.patientId,
				},
				width: '35%',
			});
	}

	downloadOrder(serviceRequestId: number) {
		this.prescripcionesService.downloadPrescriptionPdf(this.patientId, [serviceRequestId], PrescriptionTypes.STUDY);
	}

	deleteStudy(id: number) {
		this.prescripcionesService.deleteStudy(this.patientId, id).subscribe(
			() => {
				this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.DELETE_STUDY_SUCCESS');
				this.studies.splice(this.studies.findIndex(study => study.id === id), 1);
			},
			_ => {
				this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.DELETE_STUDY_ERROR');
			}
		);
	}

}
