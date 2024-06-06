import { ReferenceMedicalConceptsInformation } from "@access-management/components/reference-medical-concepts-information/reference-medical-concepts-information.component"
import { HomeInstitutionInformation } from "@access-management/components/home-institution-information/home-institution-information.component"
import { PatientPhone } from "@access-management/components/patient-phone/patient-phone.component"
import { DestinationInstitutionInformation } from "@access-management/components/destination-institution-information/destination-institution-information.component"
import { OldReferenceInformation } from "@access-management/dialogs/reference-edition-pop-up/reference-edition-pop-up.component"
import { ReferenceCounterReferenceFileDto, ReferenceDataDto, ReferencePatientDto } from "@api-rest/api-model"
import { AddressProjection } from "@api-rest/services/address-master-data.service";
import { deepClone } from "@core/utils/core.utils"

export function toOldReferenceInformation(referenceDataDto: ReferenceDataDto, referencePatientDto: ReferencePatientDto): OldReferenceInformation {
	const files: ReferenceCounterReferenceFileDto[] = referenceDataDto.files ? deepClone(referenceDataDto.files) : [];
	return {
		homeInstitutionInfo: toHomeInstitutionInformation(referenceDataDto),
		problems: referenceDataDto.problems,
		priorityId: referenceDataDto.priority.id,
		patientPhone: toPatientPhone(referencePatientDto),
		referenceMedicalConceptsInfo: toReferenceMedicalConceptsInformation(referenceDataDto),
		destinationInstitutionInfo: todestinationInstitutionInfo(referenceDataDto),
		note: referenceDataDto.note,
		files
	}
}

export function toHomeInstitutionInformation(referenceData: ReferenceDataDto): HomeInstitutionInformation {
	return {
		province: referenceData.institutionOrigin.provinceName,
		department: referenceData.institutionOrigin.departmentName,
		institution: referenceData.institutionOrigin.description
	}
}

export function toPatientPhone(referencePatientDto: ReferencePatientDto): PatientPhone {
	return {
		phonePrefix: referencePatientDto.phonePrefix,
		phoneNumber: referencePatientDto.phoneNumber
	}
}

export function toReferenceMedicalConceptsInformation(referenceData: ReferenceDataDto): ReferenceMedicalConceptsInformation {
	return {
		careLine: referenceData.careLine?.description,
		specialties: referenceData.destinationClinicalSpecialties?.map(clinicalSpecialties => clinicalSpecialties.name),
		studyCategory: referenceData?.procedureCategory,
		practice: referenceData.procedure?.pt,
		consultation: referenceData.consultation
	}
}

export function todestinationInstitutionInfo(referenceData: ReferenceDataDto): DestinationInstitutionInformation {
	return {
		referenceInstitution: referenceData.institutionDestination,
		careLineId: referenceData.careLine?.id,
		practiceId: referenceData.procedure?.id,
		clinicalSpecialtiesIds: referenceData.destinationClinicalSpecialties?.map(clinicalSpecialties => clinicalSpecialties.id),
	}
}

export function toAdressProjection(id: number, description: string): AddressProjection {
	return { id, description }
}