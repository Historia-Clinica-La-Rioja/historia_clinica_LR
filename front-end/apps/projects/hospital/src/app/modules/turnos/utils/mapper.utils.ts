import { ReferenceAppointmentDto, ReferencePatientDto, ReferenceReportDto } from "@api-rest/api-model"
import { PatientSummary } from "../../hsi-components/patient-summary/patient-summary.component"
import { getColoredIconText, getPriority, getState } from "./reference.utils"
import { ContactDetails } from "@turnos/components/contact-details/contact-details.component"
import { AppointmentSummary } from "@turnos/components/appointment-summary/appointment-summary.component";
import { Report } from "../components/report-information/report-information.component";

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

export const toReport = (report: ReferenceReportDto): Report => {
    return {
        dto: report,
        priority: getPriority(report.priority.id),
        coloredIconText: getColoredIconText(report.closureType),
        state: getState(report.appointmentStateId),
        patient: toMinPatientSummary(report.patientFullName, report.identificationType, +report.identificationNumber)
    }
}


export const toMinPatientSummary = (fullName: string, identificationType: string, identificationNumber: number): PatientSummary => {
    return (identificationNumber) ?
        {
            fullName,
            identification: {
                type: identificationType,
                number: identificationNumber
            }
        } : { fullName, }
}