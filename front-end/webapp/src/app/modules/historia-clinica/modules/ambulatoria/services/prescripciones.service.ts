import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DiagnosticReportInfoDto, MedicationInfoDto, PrescriptionDto } from '@api-rest/api-model';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { ServiceRequestService } from '@api-rest/services/service-request.service';
import { MedicationStatusChange } from '../constants/prescripciones-masterdata';
import { NewPrescriptionItem } from '../dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';

@Injectable({
  providedIn: 'root'
})
export class PrescripcionesService {

	constructor(
		private medicationRequestService: MedicationRequestService,
		private serviceRequestService: ServiceRequestService,
	) { }

	createPrescription(prescriptionType: PrescriptionTypes, newPrescription: PrescriptionDto, patientId: number): Observable<any> {
		switch(prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.medicationRequestService.create(patientId, newPrescription)
			case PrescriptionTypes.STUDY:
				return this.serviceRequestService.create(patientId, newPrescription);
		}
	}

	getPrescription(prescriptionType: PrescriptionTypes, patientId: number, statusId: string, medicationStatement: string, healthCondition: string, diagnosticReport?: string): Observable<any> {
		switch(prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.medicationRequestService.medicationRequestList(patientId, statusId, medicationStatement, healthCondition);
			case PrescriptionTypes.STUDY:
				return this.serviceRequestService.getList(patientId, statusId, diagnosticReport, healthCondition);
		}
	}

	changeMedicationStatus(statusChange: string, patientId: number, medicationsIds: number[], dayQuantity?: number, observations?: string): Observable<void> {
		switch(statusChange) {
			case MedicationStatusChange.FINALIZE:
				return this.medicationRequestService.finalize(patientId, medicationsIds);
			case MedicationStatusChange.REACTIVATE:
				return this.medicationRequestService.reactivate(patientId, medicationsIds);
			case MedicationStatusChange.SUSPEND: 
				return this.medicationRequestService.suspend(patientId, dayQuantity, observations, medicationsIds);
		}
	}

	downloadRecipe(patientId: number, medicationRequestId: number): Observable<string> {
		return this.medicationRequestService.download(patientId, medicationRequestId);
	}

	toNewPrescriptionItem(prescriptionType: PrescriptionTypes, prescriptionItem: any): NewPrescriptionItem {
		switch(prescriptionType) {
			case PrescriptionTypes.MEDICATION:
				return this.mapMedication(prescriptionItem);
			case PrescriptionTypes.STUDY:
				return this.mapStudy(prescriptionItem);
		}
	}

	private mapMedication(medicationItem: MedicationInfoDto): NewPrescriptionItem {
		return {
			id: medicationItem.id,
			snomed: medicationItem.snomed,
			healthProblem: {
				id: medicationItem.healthCondition.id,
				description: medicationItem.healthCondition.snomed.pt,
			},
			studyCategory: null,
			isDailyInterval: medicationItem.dosage.dailyInterval,
			isChronicAdministrationTime: medicationItem.dosage.chronic,
			intervalHours: String(medicationItem.dosage.frequency),
			administrationTimeDays: String(medicationItem.dosage.duration),
			observations: medicationItem.observations,
		}
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
		}
	}
}

export const enum PrescriptionTypes {
    MEDICATION = "Medication",
    STUDY = "Study",
}
