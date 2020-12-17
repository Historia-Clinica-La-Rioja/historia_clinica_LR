import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PrescriptionDto } from '@api-rest/api-model';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { ServiceRequestService } from '@api-rest/services/service-request.service';

@Injectable({
  providedIn: 'root'
})
export class PrescripcionesService {

	constructor(
		private medicationRequestService: MedicationRequestService,
		private serviceRequestService: ServiceRequestService,
	) { }

	createPrescription(prescriptionType: PrescriptionTypes, newPrescription: PrescriptionDto, patientId: number): Observable<number> {
		switch(prescriptionType) {
			case PrescriptionTypes.MEDICATION: 
				return this.medicationRequestService.create(patientId, newPrescription)
			case PrescriptionTypes.STUDY:
				return this.serviceRequestService.newServiceRequest(patientId, null);
		}

	}
}

export const enum PrescriptionTypes {
    MEDICATION = "Medication",
    STUDY = "Study",
}