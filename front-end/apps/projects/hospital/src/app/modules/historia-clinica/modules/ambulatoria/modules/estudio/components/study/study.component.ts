import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, DoctorInfoDto, ReferenceRequestDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { ResultPractice, VerResultadosEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { Content, ReferenceStudy, Title } from '@presentation/components/indication/indication.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { forkJoin } from 'rxjs';
import { ActionsButtonService } from '../../../indicacion/services/actions-button.service';
import { CreatedDuring } from '../study-list-element/study-list-element.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { capitalize } from '@core/utils/core.utils';
import { AppointmentDate, DiagnosticWithTypeReportInfoDto, E_TYPE_ORDER, InfoNewStudyOrderDto } from '../../model/ImageModel';
import { ReferenceCompleteStudyComponent } from '@historia-clinica/modules/ambulatoria/components/reference-complete-study/reference-complete-study.component';
import { ReportReference } from '@historia-clinica/modules/ambulatoria/components/reference-study-closure-information/reference-study-closure-information.component';
import { getColoredIconText } from '@access-management/utils/reference.utils';
import { Color } from '@presentation/colored-label/colored-label.component';
import { PrescriptionStatus } from '@historia-clinica/modules/ambulatoria/components/reference-request-data/reference-request-data.component';
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { CompleteStudyComponent } from '@historia-clinica/modules/ambulatoria/dialogs/complete-study/complete-study.component';
import { StudyInfo, StudyResultsService } from '@historia-clinica/modules/ambulatoria/services/study-results.service';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';

const IMAGE_DIAGNOSIS = 'Diagnóstico por imágenes';
const isImageStudy = (study: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto): boolean => {
	return study && 'typeOrder' in study
}

@Component({
	selector: 'app-study',
	templateUrl: './study.component.html',
	styleUrls: ['./study.component.scss'],
	providers: [ActionsButtonService]
})
export class StudyComponent implements OnInit {
	@Input() set studies(studies: DiagnosticReportInfoDto[] | DiagnosticWithTypeReportInfoDto[]){
		studies.forEach((study: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto) => {
			if (study.category === IMAGE_DIAGNOSIS ) {
				this._studies.push(this.mapToStudyInformationFromImageOrderCases(study as DiagnosticWithTypeReportInfoDto))
			}
			else {
					this._studies.push(this.mapToStudyInformation(study));
			}
		})
	};

	@Input() studyHeader: Title;
	@Input() patientId: number;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<void>();
	Color = Color;
	STUDY_STATUS = STUDY_STATUS;
	IMAGE_DIAGNOSIS = IMAGE_DIAGNOSIS;
	_studies: StudyInformation[] = [];
	selfDeterminationName = false;
	IMAGE_TYPE_ORDER: E_TYPE_ORDER[] = [E_TYPE_ORDER.TRANSCRIPTA, E_TYPE_ORDER.SIN_ORDEN]
	DURING_SOURCE_INSTANCES = new Map<String,CreatedDuring>([ [this.translateService.instant('app.menu.INTERNACION'), CreatedDuring.INTERNMENT],
	[this.translateService.instant('app.menu.GUARDIA'), CreatedDuring.EMERGENCY_CARE]])

	private sameOrderStudies: Map<Number, StudyInformation[]>;
	PATTERN_SPLIT = ', '


	constructor(
		private readonly prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService,
		private readonly dialog: MatDialog,
		private snackBarService: SnackBarService,
		private featureFlagService: FeatureFlagService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private studyResultsService: StudyResultsService,

	) { }

	contentBuilder(diagnosticReport: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto): ContentStudy {
		const reportImageCase = diagnosticReport as DiagnosticWithTypeReportInfoDto
		const associatedStudiesTranscribed: string[] = reportImageCase.infoOrderInstances?.associatedStudies?.map(study => capitalize(study))
		const prescriptionStatus =  diagnosticReport.statusId ? this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId) :
		this.prescripcionesService.renderStatusDescriptionStudyImage(reportImageCase.infoOrderInstances.status)
		const updateDate = diagnosticReport.creationDate;
		const isReferenceStudyPending = diagnosticReport?.referenceRequestDto;

        let extra_info = [];
        diagnosticReport.healthCondition ? extra_info.push({
            title: diagnosticReport.source === this.translateService.instant('app.menu.INTERNACION') ? 'Diagnóstico:' : 'Problema:',
            content:  diagnosticReport.healthCondition.snomed.pt,
        }) : null;
        diagnosticReport.observationsFromServiceRequest ? extra_info.push({
            title: "Observaciones:",
            content:  diagnosticReport.observationsFromServiceRequest,
        }) : null;
		return {
			status: {
				description: prescriptionStatus,
				cssClass: this.setCssClass(prescriptionStatus)
			},
			description:associatedStudiesTranscribed ? associatedStudiesTranscribed.join(this.PATTERN_SPLIT) : capitalize(diagnosticReport.snomed.pt),
			extra_info: extra_info,
			createdBy: diagnosticReport.doctor ? this.getProfessionalName(diagnosticReport.doctor) : "",
			createdOn: updateDate,
			timeElapsed: diagnosticReport.creationDate ? null : '',
			reference:  isReferenceStudyPending ? this.getReference(diagnosticReport.referenceRequestDto) : null,
            observationsFromServiceRequest: diagnosticReport.observationsFromServiceRequest,
			dateAppointment: reportImageCase.infoOrderInstances && this.getAppointmentDate(reportImageCase.infoOrderInstances.dateAppoinment),
		}
	}

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{
			this.selfDeterminationName = isOn;
		})
		this.sameOrderStudies = new Map();
		if (!isImageStudy(this._studies[0]?.diagnosticInformation))
			this._studies = this.classifyStudiesWithTheSameOrder(this._studies);
		this._studies.sort((studyA, studyB) => studyB.diagnosticInformation.creationDate?.getTime() - studyA.diagnosticInformation.creationDate?.getTime())
	}


	private getAppointmentDate( dateAppoinment: AppointmentDate): Date {
		const finalAppointmentDate = dateAppoinment.appointmentDate && dateAppoinment.appointmentHour ?
			dateTimeDtoToDate({date:dateAppoinment?.appointmentDate, time:dateAppoinment?.appointmentHour}) : null
		return finalAppointmentDate
	}


	private mapToStudyInformation(report: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto, appointment?: any): StudyInformation {
        return {
            diagnosticInformation: report,
            hasActiveAppointment: appointment?.hasActiveAppointment,
			appointmentId: appointment?.appointmentId,
			reportStatus: appointment?.documentStatus
        }
    }

	private mapToStudyInformationFromImageOrderCases(report: DiagnosticWithTypeReportInfoDto): StudyInformation {
		return {
            diagnosticInformation: report,
            hasActiveAppointment: (report.infoOrderInstances as InfoNewStudyOrderDto)?.hasActiveAppointment?(report.infoOrderInstances as InfoNewStudyOrderDto).hasActiveAppointment: false ,
			appointmentId: report.infoOrderInstances?.imageId ? report.infoOrderInstances.imageId : null ,
			reportStatus: report.infoOrderInstances?.viewReport
        }
    }

	private getProfessionalName(doctor: DoctorInfoDto): string {
		if (this.selfDeterminationName && doctor.nameSelfDetermination) {
			return doctor.nameSelfDetermination + " " + doctor.lastName
		}
		return doctor.firstName + " " + doctor.lastName
	}


	completeStudy(diagnosticReport: DiagnosticReportInfoDto) {
		let reportOrder = diagnosticReport.serviceRequestId;
		let newCompleteStudy;
		let idOrder: number = diagnosticReport.serviceRequestId;
		let studiesService: StudyInfo[] = this.studyResultsService.getStudies(idOrder);
		if (diagnosticReport?.referenceRequestDto) {

			newCompleteStudy = this.dialog.open(ReferenceCompleteStudyComponent,
				{
					data: {
						reference: diagnosticReport.referenceRequestDto,
						referenceId: diagnosticReport.referenceRequestDto.id,
						order: diagnosticReport.serviceRequestId,
						patientId: this.patientId,
						diagnosticReportId: diagnosticReport.id,
						studies: studiesService,
						status: this.getPrescriptionStatus(diagnosticReport.statusId)
					},
					width: '50%',
					disableClose: true,
				});
		} else {
			newCompleteStudy = this.dialog.open(CompleteStudyComponent,
				{
					data: {
						diagnosticReport: this.sameOrderStudies.has(reportOrder) ? this.sameOrderStudies.get(reportOrder) : [diagnosticReport],
						patientId: this.patientId,
						order: diagnosticReport.serviceRequestId,
						studies: studiesService,
						creationDate: diagnosticReport.creationDate,
						status: this.getPrescriptionStatus(diagnosticReport.statusId)
					},
					width: '90%',
					disableClose: true,
				});
		}

		newCompleteStudy.afterClosed().subscribe((completed: any) => {
			if (completed) {
				if (completed.completed) {
					if (diagnosticReport?.referenceRequestDto) {
						this.ambulatoriaSummaryFacadeService.setFieldsToUpdate( {
							allergies: false,
							medications: false,
							problems: true,
						});
					}
					this.updateCurrentReportsEventEmitter.emit();
				}
			}
		});
	}

	showStudyResults(diagnosticReport: DiagnosticReportInfoDto): void {

		let idOrder: number = diagnosticReport.serviceRequestId;
		let studies: StudyInfo[] = this.studyResultsService.getStudies(idOrder);
		let resultsPractices: ResultPractice[] = this.studyResultsService.getTemplatesStudies(diagnosticReport.serviceRequestId, this.patientId);
		if (diagnosticReport?.referenceRequestDto) {
			this.dialog.open(ReferenceCompleteStudyComponent,
				{
					data: {
						referenceId: diagnosticReport.referenceRequestDto.id,
						reportReference: this.mapperReportReference(diagnosticReport),
						order: diagnosticReport.serviceRequestId,
						patientId: this.patientId,
						diagnosticReportId: diagnosticReport.id,
						status: this.getPrescriptionStatus(diagnosticReport.statusId),
						resultsPractices: resultsPractices,
					},
					width: '50%',
					height: '55%',
				});
		 }
		else {
			this.dialog.open(VerResultadosEstudioComponent,
				{
					data: {
						diagnosticReport: diagnosticReport,
						studies: studies,
						patientId: this.patientId,
						order: diagnosticReport.serviceRequestId,
						creationDate: diagnosticReport.creationDate,
						status: this.getPrescriptionStatus(diagnosticReport.statusId)
					},
					width: '50%',
				});
		}
	}

	downloadOrder(serviceRequestId: number) : void {
		this.prescripcionesService.downloadPrescriptionPdf(this.patientId, [serviceRequestId], PrescriptionTypes.STUDY);
	}

	confirmDeleteStudy(studyToDelete: DiagnosticReportInfoDto): void {
		const dialogConfirmDeleteStudy = this.dialog.open(ConfirmDialogComponent,
			{
				data: {
					title: 'ambulatoria.paciente.studies.dialog.TITLE_DELETE_STUDY_CONFIRM',
					okButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.menu_items.DELETE',
					cancelButtonLabel: 'ambulatoria.paciente.problemas.nueva_opened_confirm_dialog.CANCEL_BUTTON',
					showMatIconError: true,
					okBottonColor: 'warn'
				}
			}
		);
		dialogConfirmDeleteStudy.afterClosed().subscribe( result => {
			if (result) this.deleteStudy(studyToDelete)
		});
	}

	deleteStudy(studyToDelete: DiagnosticReportInfoDto): void {
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
		return this.DURING_SOURCE_INSTANCES.get(source)
	}

	filterImages(study: DiagnosticWithTypeReportInfoDto){
		return this.IMAGE_TYPE_ORDER.includes(study.typeOrder)
	}

	filterImageOrderCases = (study: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto ) => {
		return isImageStudy(study) ? this.filterImages(study as DiagnosticWithTypeReportInfoDto ) : false
	}

	private classifyStudiesWithTheSameOrder(reports: StudyInformation[]): StudyInformation[] {
		let orders = new Set(reports?.map(report => report.diagnosticInformation.serviceRequestId));
		orders.forEach(order => {
			let currentOrderStudies = reports.filter(report => report.diagnosticInformation.serviceRequestId === order);
			let OrderStudiesWithoutImageOrderCases = currentOrderStudies.filter(report => !this.filterImageOrderCases(report.diagnosticInformation))
			if (OrderStudiesWithoutImageOrderCases.length > 1) {
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

	private getReference(reference: ReferenceRequestDto): ReferenceStudy {
		return {
			dto: reference,
			priority: reference.priority,
			coloredIconText: getColoredIconText(reference.closureTypeDescription)
		}
	}

	private mapperReportReference(diagnosticReport: DiagnosticReportInfoDto):ReportReference{
		return {
			doctor: diagnosticReport.referenceRequestDto.professionalInfo,
			observations: diagnosticReport.observations,
			date: diagnosticReport.referenceRequestDto.closureDateTime
		}
	}

	private getPrescriptionStatus(diagnosticReportStatusId:string): PrescriptionStatus {
		const prescriptionStatus = this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReportStatusId);
		this.setCssClass(prescriptionStatus)
		return {
			description: prescriptionStatus,
			color: this.setColor(prescriptionStatus)
		}
	}

	private setCssClass(prescriptionStatus: string): string {
		switch (prescriptionStatus) {
			case STUDY_STATUS.REGISTERED.description:
				return Color.RED;
			case STUDY_STATUS.PARTIAL.description:
				return Color.YELLOW;
			default: return Color.BLUE;
		}
	}

	private setColor(prescriptionStatus: string): Color {
		switch (prescriptionStatus) {
			case STUDY_STATUS.REGISTERED.description:
				return Color.RED;
			case STUDY_STATUS.PARTIAL.description:
				return Color.YELLOW;
			default: return Color.BLUE;
		}
	}
}
export interface StudyInformation {
    diagnosticInformation: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto;
    hasActiveAppointment?: boolean;
	appointmentId?: number | string;
	reportStatus?: boolean
}

export interface ContentStudy extends Content {
	dateAppointment?: Date
}