import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, DoctorInfoDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { CompletarEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { VerResultadosEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { Content, Title } from '@presentation/components/indication/indication.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { forkJoin } from 'rxjs';
import { anyMatch } from "@core/utils/array.utils";
import { PermissionsService } from "@core/services/permissions.service";
import { ActionsButtonService } from '../../../indicacion/services/actions-button.service';
import { CreatedDuring } from '../study-list-element/study-list-element.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { capitalize } from '@core/utils/core.utils';

const IMAGE_DIAGNOSIS = 'Diagnóstico por imágenes';

@Component({
	selector: 'app-study',
	templateUrl: './study.component.html',
	styleUrls: ['./study.component.scss'],
	providers: [ActionsButtonService]
})
export class StudyComponent implements OnInit {
	
	@Input() set studies(studies: DiagnosticReportInfoDto[]){
		studies.forEach(study => {
			if (study.category === IMAGE_DIAGNOSIS) {
				this.prescripcionesService.getPrescriptionStatus(this.patientId, study.serviceRequestId).subscribe( documentInfo => {
					this._studies.push(this.mapToStudyInformation(study, documentInfo.hasActiveAppointment, documentInfo.appointmentId, documentInfo.documentStatus));
				})
			} else {
				this._studies.push(this.mapToStudyInformation(study));
			}
		})
	};
	@Input() studyHeader: Title;
	@Input() patientId: number;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<void>();
	STUDY_STATUS = STUDY_STATUS;
	IMAGE_DIAGNOSIS = IMAGE_DIAGNOSIS;
	private sameOrderStudies: Map<Number, StudyInformation[]>;
	hasPicturesStaffRole = false;
	hasLaboratoryStaffRole = false;
	hasPharmacyStaffRole = false;
	_studies: StudyInformation[] = [];
	selfDeterminationName = false;

	constructor(
		private readonly prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService,
		private snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly permissionsService: PermissionsService,
		private featureFlagService: FeatureFlagService
	) { }

	ngOnInit(): void {
		this.setActionsLayout();
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{
			this.selfDeterminationName = isOn;
		})
		this.sameOrderStudies = new Map();
		this._studies = this.classifyStudiesWithTheSameOrder(this._studies);
		this._studies.sort((studyA, studyB) => studyB.diagnosticInformation.creationDate.getTime() - studyA.diagnosticInformation.creationDate.getTime())
	}

	private mapToStudyInformation(report: DiagnosticReportInfoDto, hasActiveAppointment?: boolean, appointmentId?: number, reportStatus?: boolean): StudyInformation {
        return {
            diagnosticInformation: report,
            hasActiveAppointment,
			appointmentId,
			reportStatus
        }
    }

	contentBuilder(diagnosticReport: DiagnosticReportInfoDto): Content {
		const prescriptionStatus = this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId);
		const updateDate = diagnosticReport.creationDate;
		return {
			status: {
				description: prescriptionStatus,
				cssClass: prescriptionStatus === this.translateService.instant('ambulatoria.paciente.studies.study_state.PENDING') ? 'red' : 'blue'
			},
			description: capitalize(diagnosticReport.snomed.pt),
			extra_info: [{
				title: diagnosticReport.source === this.translateService.instant('app.menu.INTERNACION') ? 'Diagnóstico:' : 'Problema:',
				content: diagnosticReport.healthCondition.snomed.pt
			}],
			createdBy: this.getProfessionalName(diagnosticReport.doctor),
			timeElapsed: updateDate.toLocaleDateString('es-AR') + ' - ' + updateDate.toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit' })
		}
	}
	
	private getProfessionalName(doctor: DoctorInfoDto): string {
		if (this.selfDeterminationName && doctor.nameSelfDetermination) {
			return doctor.nameSelfDetermination + " " + doctor.lastName
		}
		return doctor.firstName + " " + doctor.lastName
	}

	setActionsLayout(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasPicturesStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_IMAGENES]);
			this.hasLaboratoryStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LABORATORIO]);
			this.hasPharmacyStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_FARMACIA]);
		});
	}

	completeStudy(diagnosticReport: DiagnosticReportInfoDto) {
		let reportOrder = diagnosticReport.serviceRequestId;
		const newCompleteStudy = this.dialog.open(CompletarEstudioComponent,
			{
				data: {
					diagnosticReport: this.sameOrderStudies.has(reportOrder) ? this.sameOrderStudies.get(reportOrder) : [diagnosticReport],
					patientId: this.patientId,
				},
				width: '35%',
			});

		newCompleteStudy.afterClosed().subscribe((completed: any) => {
			if (completed) {
				if (completed.completed) {
					this.updateCurrentReportsEventEmitter.emit();
					this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_SUCCESS');
				}
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

	deleteStudy(studyToDelete: DiagnosticReportInfoDto) {
		let deleteQuery = this.sameOrderStudies.has(studyToDelete.serviceRequestId) ?
			this.sameOrderStudies.get(studyToDelete.serviceRequestId).map(report => this.prescripcionesService.deleteStudy(this.patientId, report.diagnosticInformation.id))
			: this.prescripcionesService.deleteStudy(this.patientId, studyToDelete.id);
		forkJoin(deleteQuery).subscribe(
			() => {
				this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.DELETE_STUDY_SUCCESS');
				this.updateCurrentReportsEventEmitter.emit();
			},
			_ => {
				this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.DELETE_STUDY_ERROR');
			}
		);
	}

	wasCreatedDuringSource(source: String): CreatedDuring {
		if (this.translateService.instant('app.menu.INTERNACION') === source)
			return CreatedDuring.INTERNMENT
		if (source === 'Guardia')
			return CreatedDuring.EMERGENCY_CARE
	}

	private classifyStudiesWithTheSameOrder(reports: StudyInformation[]): StudyInformation[] {
		let orders = new Set(reports?.map(report => report.diagnosticInformation.serviceRequestId));
		orders.forEach(order => {
			let currentOrderStudies = reports.filter(report => report.diagnosticInformation.serviceRequestId === order);
			if (currentOrderStudies.length > 1) {
				this.sameOrderStudies.set(order, currentOrderStudies);
				reports = reports.filter(report => report.diagnosticInformation.serviceRequestId !== order);
				this.formatStudiesWithTheSameOrder(reports, currentOrderStudies);
			}
		});
		return reports;
	}

	private formatStudiesWithTheSameOrder(reports: StudyInformation[], studiesToGroup: StudyInformation[]): StudyInformation[] {
		let newStudy = studiesToGroup[0];
		studiesToGroup.slice(1).forEach(studyToGroup => newStudy.diagnosticInformation.snomed.pt = newStudy.diagnosticInformation.snomed.pt.concat(' | ', studyToGroup.diagnosticInformation.snomed.pt));
		reports.push(newStudy);
		return reports;
	}

}

export interface StudyInformation {
    diagnosticInformation: DiagnosticReportInfoDto;
    hasActiveAppointment: boolean;
	appointmentId?: number,
	reportStatus?: boolean
}