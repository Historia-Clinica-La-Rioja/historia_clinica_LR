import { DiagnosticReportSummaryDto, SnomedDto, TranscribedServiceRequestSummaryDto } from "@api-rest/api-model"
import { medicalOrderInfo } from "@turnos/dialogs/new-appointment/new-appointment.component"


export const toTranscribedServiceRequestSummaryDto = (value:medicalOrderInfo):TranscribedServiceRequestSummaryDto => {
    return {
			diagnosticReports: value.associatedStudies ? toDiagnosticReportSummaryDto(value.associatedStudies) : null,
			serviceRequestId: value.serviceRequestId,
    }
}

export const toDiagnosticReportSummaryDto = (value:SnomedDto[]): DiagnosticReportSummaryDto[] => {
    return value.map(val =>{ return { diagnosticReportId: +val.sctid, pt: val.pt}})
}
