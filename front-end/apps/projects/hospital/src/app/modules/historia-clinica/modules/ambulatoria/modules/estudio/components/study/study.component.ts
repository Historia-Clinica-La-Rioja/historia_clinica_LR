import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, DoctorInfoDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { CompletarEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { VerResultadosEstudioComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { Content, Title } from '@presentation/components/indication/indication.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { forkJoin } from 'rxjs';
import { ActionsButtonService } from '../../../indicacion/services/actions-button.service';
import { CreatedDuring } from '../study-list-element/study-list-element.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { capitalize } from '@core/utils/core.utils';
import { AppointmentOrderImageExistCheckDto } from '../../../../../../../api-rest/api-model';
import { DiagnosticWithTypeReportInfoDto, E_TYPE_ORDER } from '../../model/ImageModel';
import { ActivatedRoute } from '@angular/router';
import { getParam } from '../../utils/utils';

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
				if ((study as DiagnosticWithTypeReportInfoDto).typeOrder === E_TYPE_ORDER.COMPLETA)
					this.prescripcionesService.getPrescriptionStatus(Number(getParam(this.route.snapshot,'idPaciente')), study.id).subscribe( documentInfo => {
						this._studies.push(this.mapToStudyInformation(study, documentInfo));
					})
				else this._studies.push(this.mapToStudyInformationFromImageOrderCases(study as DiagnosticWithTypeReportInfoDto))
			}
			else {
					this._studies.push(this.mapToStudyInformation(study));
			}
		})
	};

	@Input() studyHeader: Title;
	@Input() patientId: number;
	@Output() updateCurrentReportsEventEmitter = new EventEmitter<void>();
	STUDY_STATUS = STUDY_STATUS;
	IMAGE_DIAGNOSIS = IMAGE_DIAGNOSIS;
	_studies: StudyInformation[] = [];
	selfDeterminationName = false;
	IMAGE_TYPE_ORDER: E_TYPE_ORDER[] = [E_TYPE_ORDER.TRANSCRIPTA, E_TYPE_ORDER.SIN_ORDEN]
	DURING_SOURCE_INSTANCES = new Map<String,CreatedDuring>([ [this.translateService.instant('app.menu.INTERNACION'), CreatedDuring.INTERNMENT],
	[this.translateService.instant('app.menu.GUARDIA'), CreatedDuring.EMERGENCY_CARE]])

	private sameOrderStudies: Map<Number, StudyInformation[]>;

	constructor(
		private readonly prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService,
		private readonly dialog: MatDialog,
		private readonly route: ActivatedRoute,
		private snackBarService: SnackBarService,
		private featureFlagService: FeatureFlagService
	) { }

	contentBuilder(diagnosticReport: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto): Content {
		const reportImageCase = diagnosticReport as DiagnosticWithTypeReportInfoDto
		const prescriptionStatus =  diagnosticReport.statusId ? this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId) :
		this.prescripcionesService.renderStatusDescriptionStudyImage(reportImageCase.infoOrderInstances.status)
		const updateDate = diagnosticReport.creationDate;
		return {
			status: {
				description: prescriptionStatus,
				cssClass: prescriptionStatus === this.translateService.instant('ambulatoria.paciente.studies.study_state.PENDING') ? 'red' : 'blue'
			},
			description: capitalize(diagnosticReport.snomed.pt),
			extra_info: diagnosticReport.healthCondition ? [{
				title: diagnosticReport.source === this.translateService.instant('app.menu.INTERNACION') ? 'Diagnóstico:' : 'Problema:',
				content:  diagnosticReport.healthCondition.snomed.pt,
			}]: null  ,
			createdBy: diagnosticReport.doctor ? this.getProfessionalName(diagnosticReport.doctor) : "",
			createdOn: updateDate
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


	private mapToStudyInformation(report: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto, appointment?: AppointmentOrderImageExistCheckDto): StudyInformation {
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
            hasActiveAppointment: null,
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


	completeStudy(diagnosticReport: DiagnosticReportInfoDto) : void {
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

	showStudyResults(diagnosticReport: DiagnosticReportInfoDto) : void {
		this.dialog.open(VerResultadosEstudioComponent,
			{
				data: {
					diagnosticReport,
					patientId: this.patientId,
				},
				width: '35%',
			});
	}

	downloadOrder(serviceRequestId: number) : void {
		this.prescripcionesService.downloadPrescriptionPdf(this.patientId, [serviceRequestId], PrescriptionTypes.STUDY);
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

}

export interface StudyInformation {
    diagnosticInformation: DiagnosticReportInfoDto | DiagnosticWithTypeReportInfoDto;
    hasActiveAppointment?: boolean;
	appointmentId?: number | string;
	reportStatus?: boolean
}

