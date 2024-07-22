import { Injectable } from '@angular/core';
import { BasicPatientDto, PersonPhotoDto } from '@api-rest/api-model';
import { forkJoin, map, Observable } from 'rxjs';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';

@Injectable({
  providedIn: 'root'
})
export class PatientSummaryDataService {

	constructor(
		private readonly patientService: PatientService,
		private readonly patientNameService: PatientNameService,
	) { }

	loadPatient(patientId: number) {
		const patientBasicData$: Observable<BasicPatientDto> = this.patientService.getPatientBasicData(patientId);
		const patientPhoto$: Observable<PersonPhotoDto> = this.patientService.getPatientPhoto(patientId);
		return forkJoin([patientBasicData$, patientPhoto$]).pipe(
			map(([patientBasicData, patientPhoto]) => this.toPatientSummary(patientBasicData, patientPhoto))
		);
	}

	private toPatientSummary(basicData: BasicPatientDto, photo: PersonPhotoDto): PatientSummary {
		const { firstName, nameSelfDetermination, lastName, middleNames, otherLastNames } = basicData.person;
		return {
			fullName: this.patientNameService.completeName(
				firstName,
				nameSelfDetermination,
				lastName,
				middleNames,
				otherLastNames),
				...(basicData.identificationType
					&& {
						identification: {
							type: basicData.identificationType,
							number: +basicData.identificationNumber
						}
					}),
			id: basicData.id,
			gender: basicData.person.gender?.description || null,
			age: basicData.person.age || null,
			photo: photo.imageData
		}
	}
}
