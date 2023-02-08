import { Injectable } from '@angular/core';
import { MedicationInfoDto } from '@api-rest/api-model';
import { Subject } from 'rxjs';
import { PrescripcionesService, PrescriptionTypes } from './prescripciones.service';

@Injectable({
  providedIn: 'root'
})
export class MedicacionesService {

	private subject = new Subject<MedicationInfoDto[]>();
	private patientId: number = null;
	private statusId: string = null;
	private medicationStatement: string = null;
	private healthCondition: string = null;

	public medicaments$ = this.subject.asObservable();

	constructor(private prescripcionesService: PrescripcionesService) { }

	updateMedicationFilter(patientId: number, statusId: string, medicationStatement: string, healthCondition: string) {
		this.setInformation(patientId,statusId,medicationStatement,healthCondition);
		this.updateMedication();
	}

	updateMedicationFilterByRoles(patientId: number, statusId: string, medicationStatement: string, healthCondition: string) {
		this.setInformation(patientId,statusId,medicationStatement,healthCondition);
		this.updateMedicationByRoles();
	}

	private setInformation(patientId: number, statusId: string, medicationStatement: string, healthCondition: string){
		this.patientId = patientId;
		this.statusId = statusId;
		this.medicationStatement = medicationStatement;
		this.healthCondition = healthCondition;
	}

	updateMedicationByRoles(){
		if (this.patientId) {
			this.prescripcionesService.getPrescriptionByRoles( PrescriptionTypes.MEDICATION,
				this.patientId,
				this.statusId,
				this.medicationStatement,
				this.healthCondition).subscribe( (response: MedicationInfoDto[]) => {
					this.subject.next(response);
				});
		}
	}

	updateMedication() {
		if (this.patientId) {
			this.prescripcionesService.getPrescription( PrescriptionTypes.MEDICATION,
				this.patientId,
				this.statusId,
				this.medicationStatement,
				this.healthCondition).subscribe( (response: MedicationInfoDto[]) => {
					this.subject.next(response);
				});
		}
	}
}
