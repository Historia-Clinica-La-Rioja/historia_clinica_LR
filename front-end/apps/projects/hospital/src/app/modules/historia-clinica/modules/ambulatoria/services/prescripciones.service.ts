import { Injectable } from '@angular/core';
import {
	AddDiagnosticReportObservationsCommandDto,
	CompleteRequestDto,
	DiagnosticReportInfoDto,
	DiagnosticReportInfoWithFilesDto,
	DocumentRequestDto,
	GetDiagnosticReportObservationGroupDto,
	MedicationInfoDto,
	PrescriptionDto,
	ProfessionalLicenseNumberValidationResponseDto,
	TranscribedServiceRequestDto,
	TranscribedServiceRequestSummaryDto,
} from '@api-rest/api-model';
import { DocumentService } from '@api-rest/services/document.service';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { ServiceRequestService } from '@api-rest/services/service-request.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { Observable, switchMap } from 'rxjs';
import { MedicationStatusChange, MEDICATION_STATUS, PRESCRIPTION_STATES, STUDY_STATUS } from '../constants/prescripciones-masterdata';
import { NewPrescriptionItem } from '../dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PrescriptionLineState } from '../modules/indicacion/components/item-prescripciones/item-prescripciones.component';

@Injectable({
  providedIn: 'root'
})

export class PrescripcionesService {

	public readonly STUDY_STATUS = STUDY_STATUS;
	public readonly MEDICATION_STATUS = MEDICATION_STATUS;

	constructor(
		private medicationRequestService: MedicationRequestService,
		private serviceRequestService: ServiceRequestService,
		private readonly documentService: DocumentService,
	) { }

	createPrescription(prescriptionType: PrescriptionTypes, newPrescription: PrescriptionDto, patientId: number): Observable<DocumentRequestDto[] | number[]> {
		switch (prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.medicationRequestService.create(patientId, newPrescription);
			case PrescriptionTypes.STUDY:
				return this.serviceRequestService.create(patientId, newPrescription);
		}
	}

	createTranscribedOrder(patientId: number, transcribedInfo: TranscribedServiceRequestDto): Observable<number>{
		return this.serviceRequestService.createTranscribedOrder(patientId, transcribedInfo);
	}

	deleteTranscribedOrder(patientId: number, serviceRequestId: number): Observable<void> {
		return this.serviceRequestService.deleteTranscribedOrder(patientId, serviceRequestId);
	}

	getMedicalOrders(patientId: number, statusId: string, categoryId: string): Observable<DiagnosticReportInfoDto[]> {
		return this.serviceRequestService.getMedicalOrders(patientId, statusId, categoryId);
	}

	getTranscribedOrders(patientId: number): Observable<TranscribedServiceRequestSummaryDto[]> {
		return this.serviceRequestService.getTranscribedOrders(patientId);
	}

	saveAttachedFiles(patientId: number,serviceRequestId: number, selectedFiles: File[]): Observable<void> {
		return this.serviceRequestService.saveAttachedFiles(patientId, serviceRequestId, selectedFiles);
	}

	getPrescription(prescriptionType: PrescriptionTypes, patientId: number, statusId: string, medicationStatement: string,
		healthCondition: string, study?: string, categoryId?: string, categoryExcluded?: string): Observable<any> {
		switch (prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.medicationRequestService.medicationRequestList(patientId, statusId, medicationStatement, healthCondition);
			case PrescriptionTypes.STUDY:
				return this.serviceRequestService.getList(patientId, statusId, study, healthCondition, categoryId, categoryExcluded);
		}
	}

	getPrescriptionByRoles(patientId: number, statusId: string, medicationStatement: string, healthCondition: string): Observable<any> {
		return this.medicationRequestService.medicationRequestListByRoles(patientId, statusId, medicationStatement, healthCondition);
	}


	changeMedicationStatus(statusChange: string, patientId: number, medicationsIds: number[], dayQuantity?: number, observations?: string): Observable<void> {
		switch (statusChange) {
			case MedicationStatusChange.FINALIZE:
				return this.medicationRequestService.finalize(patientId, medicationsIds);
			case MedicationStatusChange.REACTIVATE:
				return this.medicationRequestService.reactivate(patientId, medicationsIds);
			case MedicationStatusChange.SUSPEND:
				return this.medicationRequestService.suspend(patientId, dayQuantity, observations, medicationsIds);
		}
	}

	downloadPrescriptionPdf(patientId: number, prescriptionPdfInfo: number[], prescriptionType: PrescriptionTypes, fileName?: string): void {
		switch (prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				const documentId = prescriptionPdfInfo[0];
				if (fileName)
					this.documentService.downloadFile({ id: documentId, filename: fileName });
				else
					this.documentService.downloadUnnamedFile(documentId);
				break;
			case PrescriptionTypes.STUDY:
				prescriptionPdfInfo.forEach(orderId => {
					this.serviceRequestService.downloadPdf(patientId, orderId);
				});
				break;
		}
	}

	downloadTranscribedOrderPdf(patientId: number, prescriptionPdfInfo: number[], appointmentId: number): void {
		prescriptionPdfInfo.forEach(orderId => {
			this.serviceRequestService.downloadTranscribedOrderPdf(patientId, orderId, appointmentId);
		});
	}

	getTranscribedAttachedFileUrl(patientId: number, documentId: number): string{
		return this.documentService.getTranscribedFileUrl(documentId, patientId)
	}

	deleteStudy(patientId: number, serviceRequestId: number): Observable<string> {
		return this.serviceRequestService.delete(patientId, serviceRequestId);
	}

	completeStudy(patientId: number, diagnosticReportId: number, completeRequestDto: CompleteRequestDto, files: File[]): Observable<void> {
		return this.serviceRequestService.complete(patientId, diagnosticReportId, completeRequestDto, files);
	}

	completeStudyTemplateWhithForm(patientId: number, diagnosticReportId: number, completeRequestDto: CompleteRequestDto, files: File[], reportObservations: AddDiagnosticReportObservationsCommandDto):
		Observable<void> {
		return this.serviceRequestService.addObservations(patientId, diagnosticReportId, reportObservations).pipe
			(switchMap(fileIds => {
				return this.completeStudy(patientId, diagnosticReportId, completeRequestDto, files)}
			));
	}

	completeStudyByRdi(patientId: number, appointmentId: number): Observable<void> {
		return this.serviceRequestService.completeByRdi(patientId, appointmentId);
	}

	showStudyResults(patientId: number, diagnosticReportId: number): Observable<DiagnosticReportInfoWithFilesDto> {
		return this.serviceRequestService.get(patientId, diagnosticReportId);
	}

	showStudyResultsWithFormTempalte(patientId: number, diagnosticReportId: number): Observable<GetDiagnosticReportObservationGroupDto> {
		return this.serviceRequestService.getObservations(patientId, diagnosticReportId);
	}

	downloadStudyFile(patientId: number, fileId: number, fileName: string) {
		this.serviceRequestService.download(patientId, fileId, fileName);
	}

	validateProfessional(patientId: number): Observable<ProfessionalLicenseNumberValidationResponseDto> {
		return this.medicationRequestService.validateProfessional(patientId);
	}

	toNewPrescriptionItem(prescriptionType: PrescriptionTypes, prescriptionItem: any): NewPrescriptionItem {
		switch (prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.mapMedication(prescriptionItem);
			case PrescriptionTypes.STUDY:
				return this.mapStudy(prescriptionItem);
		}
	}

	cancelPrescriptionLineState(medicationStatementId: number, patientId: number): Observable<void> {
		return this.medicationRequestService.cancelPrescriptionLineState(medicationStatementId, patientId);
	}

	private mapMedication(medicationItem: MedicationInfoDto): NewPrescriptionItem {
		return {
			id: medicationItem.id,
			snomed: medicationItem.snomed,
			healthProblem: {
				id: medicationItem.healthCondition.id,
				description: medicationItem.healthCondition.snomed.pt,
				sctId: medicationItem.healthCondition.snomed.sctid
			},
			studyCategory: null,

			isDailyInterval: medicationItem.dosage ? medicationItem.dosage.dailyInterval : null,
			isChronicAdministrationTime: medicationItem.dosage ? medicationItem.dosage.chronic : null,
			intervalHours: medicationItem.dosage?.frequency ? String(medicationItem.dosage.frequency) : null,
			administrationTimeDays: medicationItem.dosage?.duration ? String(medicationItem.dosage.duration) : null,
			observations: medicationItem.observations,
			unitDose: medicationItem.dosage.dosesByUnit,
			dayDose: medicationItem.dosage.dosesByDay,
			quantity: medicationItem.dosage.quantityDto

		};
	}

	private mapStudy(studyItem: DiagnosticReportInfoDto): NewPrescriptionItem {
		return {
			id: studyItem.serviceRequestId,
			snomed: studyItem.snomed,
			healthProblem: {
				id: studyItem.healthCondition.id,
				description: studyItem.healthCondition.snomed.pt,
			},
			studyCategory: null,
			isDailyInterval: null,
			isChronicAdministrationTime: null,
			observations: studyItem.observations,
			unitDose: null,
			dayDose: null,
			quantity: null
		};
	}

	renderStatusDescription(prescriptionType: PrescriptionTypes, statusId: string): string {
		switch (prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.renderStatusDescriptionMedication(statusId);
			case PrescriptionTypes.STUDY:
				return this.renderStatusDescriptionStudy(statusId);
		}
	}

	private renderStatusDescriptionMedication(statusId: string): string {
		switch (statusId) {
			case this.MEDICATION_STATUS.ACTIVE.id:
				return this.MEDICATION_STATUS.ACTIVE.description;
			case this.MEDICATION_STATUS.STOPPED.id:
				return this.MEDICATION_STATUS.STOPPED.description;
			case this.MEDICATION_STATUS.SUSPENDED.id:
				return this.MEDICATION_STATUS.SUSPENDED.description;
		}
	}

	private renderStatusDescriptionStudy(statusId: string): string {
		switch (statusId) {
			case this.STUDY_STATUS.REGISTERED.id:
				return this.STUDY_STATUS.REGISTERED.description;
			case this.STUDY_STATUS.FINAL.id:
				return this.STUDY_STATUS.FINAL.description;
			case this.STUDY_STATUS.FINAL_RDI.id:
				return this.STUDY_STATUS.FINAL_RDI.description;
			case this.STUDY_STATUS.ERROR.id:
				return this.STUDY_STATUS.ERROR.description;
		}
	}

	renderStatusDescriptionStudyImage(stateComplete: boolean): string {
		return stateComplete ? this.STUDY_STATUS.FINAL.description : this.STUDY_STATUS.REGISTERED.description
	}

	renderPrescriptionLineState(prescriptionLineState: number): PrescriptionLineState {
		let state: PrescriptionLineState = {
			description: PRESCRIPTION_STATES.INDICADA.description,
			color: Color.BLUE
		}

		if (PRESCRIPTION_STATES.ANULADA.id === prescriptionLineState) {
			state.description = PRESCRIPTION_STATES.ANULADA.description;
			state.color = Color.RED;
		}

		if (PRESCRIPTION_STATES.DISPENSADA.id === prescriptionLineState) {
			state.description = PRESCRIPTION_STATES.DISPENSADA.description;
			state.color = Color.GREEN;
		}

		if (PRESCRIPTION_STATES.PROVISORIO_DISPENSADA.id === prescriptionLineState) {
			state.description = PRESCRIPTION_STATES.PROVISORIO_DISPENSADA.description;
			state.color = Color.GREEN;
		}
		return state;
	}
}

export const enum PrescriptionTypes {
	MEDICATION = 'Medication',
	STUDY = 'Study',
}
