import { ClinicalSpecialtyDto, ReferenceAppointmentDto, ReferencePatientDto, ReferenceReportDto, SharedSnomedDto } from "@api-rest/api-model";
import { PatientSummary } from "../../hsi-components/patient-summary/patient-summary.component";
import { getColoredIconText, getPriority, getState } from "./reference.utils";
import { TypeaheadOption } from "@presentation/components/typeahead/typeahead.component";
import { AppointmentSummary } from "@turnos/components/appointment-summary/appointment-summary.component";
import { ContactDetails } from "@turnos/components/contact-details/contact-details.component";
import { Report } from "@turnos/components/report-information/report-information.component";

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

export const practicesToTypeaheadOptions = (practices: SharedSnomedDto[]): TypeaheadOption<any>[] => {
    return practices.map(practice => practiceToTypeaheadOption(practice));
}

export const practiceToTypeaheadOption = (practice: SharedSnomedDto): TypeaheadOption<any> => {
    return {
        compareValue: practice.pt,
        value: practice.id
    }
}

export const specialtiesToTypeaheadOptions = (specialties: ClinicalSpecialtyDto[]): TypeaheadOption<any>[] => {
    return specialties.map(practice => specialtyToTypeaheadOption(practice));
}

export const specialtyToTypeaheadOption = (specialty: ClinicalSpecialtyDto): TypeaheadOption<any> => {
    return {
        compareValue: specialty.name,
        value: specialty.id 
    }
}
