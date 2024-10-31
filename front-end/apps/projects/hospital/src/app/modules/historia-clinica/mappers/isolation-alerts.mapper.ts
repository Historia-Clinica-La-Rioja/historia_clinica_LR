import { MasterDataDto, PatientCurrentIsolationAlertDto } from "@api-rest/api-model"
import { dateDtoToDate } from "@api-rest/mapper/date-dto.mapper"
import { IsolationAlertDetail } from "@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component"
import { IsolationAlert } from "@historia-clinica/components/isolation-alert-form/isolation-alert-form.component"
import { IsolationAlertsSummary, IsolationAlertStatus } from "@historia-clinica/components/isolation-alerts-summary-card/isolation-alerts-summary-card.component"

const toIsolationAlertStatus = (status: MasterDataDto): IsolationAlertStatus => {
    return status;
}

const mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary = (patientCurrentIsolationAlert: PatientCurrentIsolationAlertDto): IsolationAlertsSummary => {
    return {
        id: patientCurrentIsolationAlert.isolationAlertId,
        diagnosis: patientCurrentIsolationAlert.healthConditionPt,
        types: patientCurrentIsolationAlert.types.map(type => type.description),
        criticality: patientCurrentIsolationAlert.criticality.description,
        endDate: dateDtoToDate(patientCurrentIsolationAlert.endDate),
        status: toIsolationAlertStatus(patientCurrentIsolationAlert.status),
    }
}

export const mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary = (patientCurrentIsolationAlerts: PatientCurrentIsolationAlertDto[]): IsolationAlertsSummary[] => {
    return patientCurrentIsolationAlerts.map(mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary);
}

const mapIsolationAlertToIsolationAlertDetail = (isolationAlert: IsolationAlert): IsolationAlertDetail => {
    return {
        diagnosis: isolationAlert.diagnosis.snomed.pt,
        types: isolationAlert.types.map(type => type.description),
        criticality: isolationAlert.criticality.description,
        endDate: isolationAlert.endDate,
        observations: isolationAlert.observations,
    }
}

export const mapIsolationAlertsToIsolationAlertsDetails = (isolationAlerts: IsolationAlert[]): IsolationAlertDetail[] => {
    return isolationAlerts.map(mapIsolationAlertToIsolationAlertDetail);

}