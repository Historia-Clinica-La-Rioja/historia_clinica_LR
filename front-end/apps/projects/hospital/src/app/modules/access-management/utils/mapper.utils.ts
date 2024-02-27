import { ClinicalSpecialtyDto, ReferenceAppointmentDto, ReferenceDataDto, ReferencePatientDto, ReferenceReportDto, SharedSnomedDto } from "@api-rest/api-model";
import { PatientSummary } from "../../hsi-components/patient-summary/patient-summary.component";
import { getPriority, getState, getAppointmentState } from "./reference.utils";
import { TypeaheadOption } from "@presentation/components/typeahead/typeahead.component";
import { ReferenceReport } from "@shared-appointment-access-management/components/reference-summary/reference-summary.component";
import { AppointmentSummary } from "@access-management/components/appointment-summary/appointment-summary.component";
import { ContactDetails } from "@access-management/components/contact-details/contact-details.component";
import { ReferenceCompleteData } from "@historia-clinica/modules/ambulatoria/components/reference-request-data/reference-request-data.component";

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
        state: getAppointmentState(value.appointmentStateId),
        date: value.date,
        professionalFullName: value.professionalFullName,
        institution: value.institution.description
    }
}

export const toReferenceReport = (report: ReferenceReportDto): ReferenceReport => {
    return {
        dto: report,
        priority: getPriority(report.priority.id),
        state: report.attentionState ? getState(report.attentionState) : null,
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

export const mapToReferenceCompleteData = (referenceData: ReferenceDataDto): ReferenceCompleteData =>{
	return {
		dto: referenceData,
		priority: getPriority(referenceData.priority.id),
		problems: referenceData.problems.join(', ')
	};
}
