import { Injectable } from '@angular/core';
import { InternmentEpisodeSummary } from '../components/internment-episode-summary/internment-episode-summary.component';
import { BasicPatientDto, CompletePatientDto, InternmentSummaryDto, PatientType, PersonalInformationDto } from '@api-rest/api-model';
import { PatientBasicData } from '../components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { DateFormat, momentParseDate, momentParseDateTime } from '@core/utils/moment.utils';

@Injectable({
	providedIn: 'root'
})
export class MapperService {

	toInternmentEpisodeSummary: (o: InternmentSummaryDto) => InternmentEpisodeSummary = MapperService._toInternmentEpisodeSummary;
	toPatientBasicData: (o: BasicPatientDto) => PatientBasicData = MapperService._toPatientBasicData;
	toPersonalInformationData: (o1: CompletePatientDto, o2: PersonalInformationDto) => PersonalInformation = MapperService._toPersonalInformationData;
	toPatientTypeData: (patientType: PatientType) => PatientTypeData = MapperService._toPatientTypeData;

	constructor() {
	}

	private static _toInternmentEpisodeSummary(internmentSummary: InternmentSummaryDto): InternmentEpisodeSummary {
		const internmentEpisodeSummary = {
			bedNumber: internmentSummary.bed.bedNumber,
			roomNumber: internmentSummary.bed.room.roomNumber,
			specialtyName: internmentSummary.specialty.name,
			totalInternmentDays: internmentSummary.totalInternmentDays,
			doctor: null,
			admissionDatetime: momentParseDate(String(internmentSummary.entryDate)).format(DateFormat.VIEW_DATE),
			probableDischargeDate: internmentSummary.probableDischargeDate ? momentParseDateTime(String(internmentSummary.probableDischargeDate)).format(DateFormat.VIEW_DATE) : 'Sin fecha definida',
			responsibleContact: null
		};
		if (internmentSummary.doctor) {
			internmentEpisodeSummary.doctor = {
				firstName: internmentSummary.doctor.firstName,
				lastName: internmentSummary.doctor.lastName,
				license: internmentSummary.doctor.licence
			};
		}
		if (internmentSummary.responsibleContact) {
			internmentEpisodeSummary.responsibleContact = {
				fullName: internmentSummary.responsibleContact?.fullName,
				phoneNumber: internmentSummary.responsibleContact?.phoneNumber,
				relationship: internmentSummary.responsibleContact?.relationship,
			};
		}
		return internmentEpisodeSummary;
	}

	private static _toPatientBasicData<T extends BasicPatientDto>(patient: T): PatientBasicData {
		return {
			id: patient.id,
			firstName: patient.person.firstName,
			lastName: patient.person.lastName,
			gender: patient.person.gender.description,
			age: patient.person.age
		};
	}

	private static _toPersonalInformationData(patient: CompletePatientDto, person: PersonalInformationDto): PersonalInformation {
		const personalInformation = {
			identificationNumber: person.identificationNumber,
			identificationType: person.identificationType,
			cuil: person.cuil,
			address: person.address,
			birthDate: person.birthDate ? momentParseDate(String(person.birthDate)).format(DateFormat.VIEW_DATE) : '',
			email: person.email,
			phoneNumber: person.phoneNumber,
			medicalCoverageName: patient.medicalCoverageName,
			medicalCoverageAffiliateNumber: patient.medicalCoverageAffiliateNumber
		};
		personalInformation.address.id = null;
		return personalInformation;
	}

	private static _toPatientTypeData(patientType: PatientType): PatientTypeData {
		return {
			id: patientType.id,
			description: patientType.description
		};
	}

}
