import { ReferenceAppointmentDto, ReferencePatientDto } from "@api-rest/api-model"
import { PatientSummary } from "../../hsi-components/patient-summary/patient-summary.component"
import { getState } from "./reference.utils"
import { ContactDetails } from "@turnos/components/contact-details/contact-details.component"
import { AppointmentSummary } from "@turnos/components/appointment-summary/appointment-summary.component"

export const toPatientSummary = (patient: ReferencePatientDto): PatientSummary => {
    return {
        fullName: patient.patientFullName,
        identification: {
            type: patient.identificationType,
            number: +patient.identificationNumber
        },
        id: patient.patientId,
    }
}

export const toContactDetails = (patient: ReferencePatientDto): ContactDetails => {
    return {
        phonePrefix: patient.phonePrefix,
        phoneNumber: patient.phoneNumber,
        email: patient.email
    }
}

export const toAppointmentSummary = (value: ReferenceAppointmentDto): AppointmentSummary => {
    return {
        state: getState(value.appointmentStateId),
        date: value.date,
        professionalFullName: value.professionalFullName,
        institution: value.institution.description
    }
}