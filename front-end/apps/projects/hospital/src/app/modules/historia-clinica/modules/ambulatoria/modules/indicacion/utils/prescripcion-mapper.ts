import { UntypedFormGroup } from "@angular/forms";
import { APatientDto, BMPersonDto, BasicPatientDto, SnomedDto } from "@api-rest/api-model";
import { NewPrescriptionItem } from "@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component";

export function mapToAPatientDto(patientData: BasicPatientDto, person: BMPersonDto, prescriptionForm: UntypedFormGroup): APatientDto {
    const patientDto: APatientDto = {
        comments: null,
        auditType: null,
        generalPractitioner: null,
        identityVerificationStatusId: null,
        personAge: null,
        pamiDoctor: null,
        typeId: patientData.typeId,
        apartment: person.apartment,
        birthDate: person.birthDate,
        cityId: prescriptionForm.get('patientData.city').value,
        countryId: prescriptionForm.get('patientData.country').value,
        cuil: person.cuil,
        departmentId: prescriptionForm.get('patientData.locality').value,
        educationLevelId: person.educationLevelId,
        email: person.email,
        ethnicityId: person.ethnicityId,
        fileIds: person.fileIds,
        firstName: person.firstName,
        floor: person.floor,
        genderId: person.genderId,
        genderSelfDeterminationId: person.genderSelfDeterminationId,
        identificationNumber: person.identificationNumber,
        identificationTypeId: person.identificationTypeId,
        lastName: person.lastName,
        middleNames: person.middleNames,
        mothersLastName: person.mothersLastName,
        nameSelfDetermination: person.nameSelfDetermination,
        number: prescriptionForm.get('patientData.streetNumber').value,
        occupationId: person.occupationId,
        otherLastNames: person.otherLastNames,
        phoneNumber: prescriptionForm.get('patientData.phoneNumber').value,
        phonePrefix: prescriptionForm.get('patientData.phonePrefix').value,
        postcode: person.postcode,
        provinceId: prescriptionForm.get('patientData.province').value,
        quarter: person.quarter,
        religion: person.religion,
        street: prescriptionForm.get('patientData.street').value,
    }
    return patientDto;
}

export function mapToNewPrescriptionItem(snomedDto: SnomedDto): NewPrescriptionItem {
    const newPrescriptionItem: NewPrescriptionItem = {
        id: null,
        snomed: snomedDto,
        healthProblem: null,
        studyCategory: null,
        isDailyInterval: true,
        observations: null,
        unitDose: null,
        dayDose: null,
        quantity: null
    }
    return newPrescriptionItem;
}