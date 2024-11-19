import { MasterDataDto, PatientCurrentIsolationAlertDto } from "@api-rest/api-model"
import { convertDateTimeDtoToDate, dateDtoToDate, } from "@api-rest/mapper/date-dto.mapper"
import { IsolationAlertDetail } from "@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component"
import { IsolationAlert } from "@historia-clinica/components/isolation-alert-form/isolation-alert-form.component"
import { IsolationAlertsSummary, IsolationAlertStatus } from "@historia-clinica/components/isolation-alerts-summary-card/isolation-alerts-summary-card.component"
import { RegisterEditor } from "@presentation/components/register-editor-info/register-editor-info.component"

export const toIsolationAlertStatus = (status: MasterDataDto): IsolationAlertStatus => {
    return status;
}

export const mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary = (patientCurrentIsolationAlert: PatientCurrentIsolationAlertDto): IsolationAlertsSummary => {
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

export const toRegisterEditorInfo = (professional: string, date: Date): RegisterEditor => {
    return {
        createdBy: professional,
        date,
    }
}

export const mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail = (isolationAlert: PatientCurrentIsolationAlertDto): IsolationAlertDetail => {
    const creatorFullName = isolationAlert.author.fullName;
    const createdOn = convertDateTimeDtoToDate(isolationAlert.startDate);
    const editorFullName = isolationAlert.isModified ? isolationAlert.updatedBy.fullName : null;
    const updatedOn = isolationAlert.isModified ? convertDateTimeDtoToDate(isolationAlert.updatedOn) : null
    return {
        diagnosis: isolationAlert.healthConditionPt,
        types: isolationAlert.types.map(type => type.description),
        criticality: isolationAlert.criticality.description,
        endDate: dateDtoToDate(isolationAlert.endDate),
        ...(!!isolationAlert.observations && { observations: isolationAlert.observations }),
        creator: toRegisterEditorInfo(creatorFullName, createdOn),
        ...(!!isolationAlert.isModified && { editor: toRegisterEditorInfo(editorFullName, updatedOn) }),
    }
}

export const mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail = (isolationAlerts: PatientCurrentIsolationAlertDto[]): IsolationAlertDetail[] => {
    return isolationAlerts.map(isolationAlert => mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail(isolationAlert));
}

export const mapIsolationAlertToIsolationAlertDetail = (isolationAlert: IsolationAlert): IsolationAlertDetail => {
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
