import { Injectable } from '@angular/core';
import { BasicPatientDto, InternmentSummaryDto } from '@api-rest/api-model';
import { InternmentEpisode } from '../components/internment-episode-summary/internment-episode-summary.component';
import { PatientBasicData } from '../components/patient-card/patient-card.component';

@Injectable({
  providedIn: 'root'
})
export class MapperService {

	toInternmentEpisode: (InternmentSummaryDto) => InternmentEpisode = MapperService._toInternmentEpisode;
	toPatientBasicData: (BasicPatientDto) => PatientBasicData = MapperService._toPatientBasicData;

	constructor() {
	}

	private static _toInternmentEpisode(internmentSummary: InternmentSummaryDto): InternmentEpisode {
		return {
			bed: {
				number: internmentSummary.bed.bedNumber
			},
			room: {
				number: internmentSummary.bed.room.roomNumber
			},
			specialty: {
				name: internmentSummary.specialty.name
			},
			doctor: {
				firstName: internmentSummary.doctor.firstName,
				lastName: internmentSummary.doctor.lastName,
				license: internmentSummary.doctor.licence
			},
			totalInternmentDays: internmentSummary.totalInternmentDays,
			admissionDatetime: internmentSummary.createdOn.toString()
		};
	}

	private static _toPatientBasicData(patient: BasicPatientDto): PatientBasicData {
		return {
			id: patient.id,
			firstName: patient.person.firstName,
			lastName: patient.person.lastName,
			gender: patient.person.gender.description,
			age: patient.person.age
		};
	}

}
