import { Injectable } from '@angular/core';
import { AllergyConditionDto, AnthropometricDataDto, DateTimeDto, DiagnosisDto, DocumentObservationsDto, ExternalCauseDto, HealthConditionDto, HealthHistoryConditionDto, HospitalizationDocumentHeaderDto, HospitalizationProcedureDto, ImmunizationDto, MedicationDto, RiskFactorDto } from '@api-rest/api-model';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { TranslateService } from '@ngx-translate/core';
import { DateFormat, DateToShow, DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { AnthropometricData, ClinicalEvaluationData, DescriptionItemDataInfo, ExternalCauseData, HeaderDescription, HeaderIdentifierData, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { dateTimeDtoToDate, dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { PROCEDURES_DESCRIPTION_ITEM, ALLERGIES_DESCRIPTION_ITEM, VITAL_SIGNS_AND_RISK_FACTORS, VACCINES_DESCRIPTION_ITEM, PERSONAL_HISTORIES_DESCRIPTION_ITEM, FAMILY_HISTORIES_DESCRIPTION_ITEM, USUAL_MEDICATIONS_DESCRIPTION_ITEM, HEADER_DATA_BED, HEADER_DATA_DATE, HEADER_DATA_INSTITUTION, HEADER_DATA_PATIENT, HEADER_DATA_PROFESSIONAL, HEADER_DATA_ROOM, HEADER_DATA_SCOPE, HEADER_DATA_SECTOR, HEADER_DATA_SPECIALTY, OTHER_PROBLEMS_DESCRIPTION_ITEM, ExternalCauseType, EventLocation } from '@historia-clinica/constants/document-summary.constants';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';

const CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
const PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;
const INFO_DIVIDER = ' | ';

@Injectable({
    providedIn: 'root'
})
export class DocumentsSummaryMapperService {

    private confirmedStatus = '';
    private presumptiveStatus = '';

    constructor(
        private readonly translateService: TranslateService,
        private readonly dateFormatPipe: DateFormatPipe
    ) {
        this.confirmedStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.CONFIRMED')
        this.presumptiveStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.PRESUMPTIVE')
    }

    mapToHeaderDescription(header: HospitalizationDocumentHeaderDto, title: string, activeDocument: DocumentSearch): HeaderDescription {
        return {
            title,
            edit: activeDocument.canDoAction.edit,
            delete: activeDocument.canDoAction.delete,
            headerDescriptionData: {
                scope: header.sourceTypeName,
                specialty: header.clinicalSpecialtyName,
                dateTime: this.dateFormatPipe.transform(dateTimeDtotoLocalDate(header.createdOn), 'datetime'),
                professional: header.professionalName,
                institution: header.institutionName,
                sector: header.bed?.room.sector.description,
                room: header.bed?.room.description,
                bed: header.bed?.bedNumber,
            },
        }
    }

    toDescriptionItemData(description: string,  dateToShow?: DateToShow): DescriptionItemData {
        return {
            description,
            ...(dateToShow && { dateToShow }),
        }
    }        

    mapDiagnosisToDescriptionItemData(diagnosis: DiagnosisDto[]): DescriptionItemData[] {
        return diagnosis.map(diag => this.toDescriptionItemData(this.mapDescriptionAndStatusToString(diag)));
    }

    mapDescriptionAndStatusToString(diagnosis: DiagnosisDto): string {
        if (diagnosis.verificationId === CONFIRMED) {
            return `${diagnosis.snomed.pt} ${this.confirmedStatus}`
        }
        if (diagnosis.verificationId === PRESUMPTIVE) {
            return `${diagnosis.snomed.pt} ${this.presumptiveStatus}`
        }
        return diagnosis.snomed.pt
    }

    mapProceduresToDescriptionItemData(procedures: HospitalizationProcedureDto[]): DescriptionItemData[] {
        return procedures.map(procedure => this.toDescriptionItemData(procedure.snomed.pt, this.mapStringToDateToShow(procedure.performedDate)));
    }

    mapStringToDateToShow(date: string): DateToShow {
        return date ? { date: dateISOParseDate(date), format: DateFormat.DATE } : null;
    }

    mapDateTimeDtoToDateToShow(date: DateTimeDto): DateToShow {
        return date ? { date: dateTimeDtoToDate(date), format: DateFormat.DATE } : null;
    }

    mapToAnthropometricData(antropometricData: AnthropometricDataDto): AnthropometricData {
        return {
            ...(antropometricData.height && { height: [this.toDescriptionItemData(antropometricData.height.value)] }),
            ...(antropometricData.bloodType && { bloodType:[ this.toDescriptionItemData(antropometricData.bloodType.value)] }),
            ...(antropometricData.weight && { weight: [this.toDescriptionItemData(`${antropometricData.weight.value} ${VITAL_SIGNS_AND_RISK_FACTORS.KG}`)] }),
        }
    }

    mapToVitalSignsAndRiskFactors(riskFactors: RiskFactorDto): VitalSignsAndRiskFactorsData {
        return {
            ...(riskFactors.bloodGlucose && { bloodGlucose: [this.toDescriptionItemData(`${riskFactors.bloodGlucose.value} ${VITAL_SIGNS_AND_RISK_FACTORS.MG_DL}`)] }),
            ...(riskFactors.bloodOxygenSaturation && { bloodOxygenSaturation: [this.toDescriptionItemData(`${riskFactors.bloodOxygenSaturation.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.cardiovascularRisk && { cardiovascularRisk: [this.toDescriptionItemData(`${riskFactors.cardiovascularRisk.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.diastolicBloodPressure && { diastolicBloodPressure: [this.toDescriptionItemData(`${riskFactors.diastolicBloodPressure.value} ${VITAL_SIGNS_AND_RISK_FACTORS.MILIMITERS}`)] }),
            ...(riskFactors.glycosylatedHemoglobin && { glycosylatedHemoglobin: [this.toDescriptionItemData(`${riskFactors.glycosylatedHemoglobin.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.heartRate && { heartRate: [this.toDescriptionItemData(`${riskFactors.heartRate.value}${VITAL_SIGNS_AND_RISK_FACTORS.MINUTE}`)] }),
            ...(riskFactors.hematocrit && { hematocrit: [this.toDescriptionItemData(`${riskFactors.hematocrit.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.respiratoryRate && { respiratoryRate: [this.toDescriptionItemData(`${riskFactors.respiratoryRate.value}${VITAL_SIGNS_AND_RISK_FACTORS.MINUTE}`)] }),
            ...(riskFactors.systolicBloodPressure && { systolicBloodPressure: [this.toDescriptionItemData(`${riskFactors.systolicBloodPressure.value} ${VITAL_SIGNS_AND_RISK_FACTORS.MILIMITERS}`)] }),
            ...(riskFactors.temperature && { temperature: [this.toDescriptionItemData(`${riskFactors.temperature.value}${VITAL_SIGNS_AND_RISK_FACTORS.TEMPERATURE}`)] }),
        }
    }

    mapAllergiesToDescriptionItemData(allergies: AllergyConditionDto[]): DescriptionItemData[] {
        return allergies.map(allergie => this.toDescriptionItemData(allergie.snomed.pt));
    }

    mapVaccinesToDescriptionItemData(vaccines: ImmunizationDto[]): DescriptionItemData[] {
        return vaccines.map(vaccine => this.toDescriptionItemData(vaccine.snomed.pt, this.mapStringToDateToShow(vaccine.administrationDate)));
    }

    mapHistoriesToDescriptionItemData(histories: HealthHistoryConditionDto[]): DescriptionItemData[] {
        return histories.map(history => this.toDescriptionItemData(history.snomed.pt,  this.mapStringToDateToShow(history.startDate)));
    }    
    
    mapMedicationsToDescriptionItemData(medications: MedicationDto[]): DescriptionItemData[] {
        return medications.map(medication => this.toDescriptionItemData(this.getMedicationDescription(medication)));
    }

    private getMedicationDescription(medication: MedicationDto): string {
        return medication.note ? `${medication.snomed.pt} ${this.isSuspended(medication)} ${INFO_DIVIDER} ${medication.note}` : `${medication.snomed.pt} ${this.isSuspended(medication)}`;
    }

    private isSuspended(medication: MedicationDto): string {
        return medication.suspended ? ' (Suspendida)' : ''
    }

    hasClinicalEvaluations(notes: DocumentObservationsDto): boolean {
        return (
            !!notes.currentIllnessNote?.length 
            || !!notes.physicalExamNote?.length 
            || !!notes.studiesSummaryNote?.length 
            || !!notes.evolutionNote?.length 
            || !!notes.clinicalImpressionNote?.length 
            || !!notes.otherNote?.length)
    }

    mapClinicalEvaluationToDescriptionItemData(notes: DocumentObservationsDto): ClinicalEvaluationData {
        return {
            ...(notes.clinicalImpressionNote && { clinicalImpressionNote: [this.toDescriptionItemData(notes.clinicalImpressionNote)] }),
            ...(notes.currentIllnessNote && { currentIllnessNote: [this.toDescriptionItemData(notes.currentIllnessNote)] }),
            ...(notes.evolutionNote && { evolutionNote: [this.toDescriptionItemData(notes.evolutionNote)] }),
            ...(notes.indicationsNote && { indicationsNote: [this.toDescriptionItemData(notes.indicationsNote)] }),
            ...(notes.otherNote && { otherNote: [this.toDescriptionItemData(notes.otherNote)] }),
            ...(notes.physicalExamNote && { physicalExamNote: [this.toDescriptionItemData(notes.physicalExamNote)] }),
            ...(notes.studiesSummaryNote && { studiesSummaryNote: [this.toDescriptionItemData(notes.studiesSummaryNote)] }),
        }
    }

    getNoInformationAsDescriptionItemData(): DescriptionItemData {
        return this.toDescriptionItemData(this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION'));
    }

    mapProceduresToDescriptionItemDataSummary(procedures: HospitalizationProcedureDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapProceduresToDescriptionItemData(procedures),
            ...PROCEDURES_DESCRIPTION_ITEM,
        }
    }

    mapAllergiesToDescriptionItemDataSummary(allergies: AllergyConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAllergiesToDescriptionItemData(allergies),
            ...ALLERGIES_DESCRIPTION_ITEM,
        }
    }

    mapVaccinesToDescriptionItemDataSummary(vaccines: ImmunizationDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapVaccinesToDescriptionItemData(vaccines),
            ...VACCINES_DESCRIPTION_ITEM,
        }
    }

    mapPersonalHistoriesToDescriptionItemDataSummary(personalHistories: HealthHistoryConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapHistoriesToDescriptionItemData(personalHistories),
            ...PERSONAL_HISTORIES_DESCRIPTION_ITEM,
        }
    }

    mapFamilyHistoriesToDescriptionItemDataSummary(familyHistories: HealthHistoryConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapHistoriesToDescriptionItemData(familyHistories),
            ...FAMILY_HISTORIES_DESCRIPTION_ITEM,
        }
    }

    mapMedicationsToDescriptionItemDataSummary(medications: MedicationDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapMedicationsToDescriptionItemData(medications),
            ...USUAL_MEDICATIONS_DESCRIPTION_ITEM,
        }
    }

    mapToHeaderIdentifierData(headerData: HeaderDescription): HeaderIdentifierData {
        return {
            ...headerData,
            headerDescriptionData: [{
                value: headerData.headerDescriptionData?.scope,
                ...HEADER_DATA_SCOPE,
            },
            {
                value: headerData.headerDescriptionData?.specialty,
                ...HEADER_DATA_SPECIALTY,
            },
            {
                value: headerData.headerDescriptionData?.dateTime,
                ...HEADER_DATA_DATE,
            },
            {
                value: headerData.headerDescriptionData?.patient,
                ...HEADER_DATA_PATIENT,
            },
            {
                value: headerData.headerDescriptionData?.professional,
                ...HEADER_DATA_PROFESSIONAL,
            },
            {
                value: headerData.headerDescriptionData?.institution,
                ...HEADER_DATA_INSTITUTION,
            },
            {
                value: headerData.headerDescriptionData?.sector,
                ...HEADER_DATA_SECTOR,
            },
            {
                value: headerData.headerDescriptionData?.room,
                ...HEADER_DATA_ROOM,
            },
            {
                value: headerData.headerDescriptionData?.bed,
                ...HEADER_DATA_BED,
            }]
        }
    }

    mapOtherProblemsToDescriptionItemDataSummary(otherProblems: HealthConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapOtherProblemsToDescriptionItemData(otherProblems),
            ...OTHER_PROBLEMS_DESCRIPTION_ITEM,
        }
    }

    mapOtherProblemsToDescriptionItemData(otherProblems: HealthConditionDto[]): DescriptionItemData[] {
        return otherProblems.map(otherProblem => this.toDescriptionItemData(otherProblem.snomed.pt));
    }

    private mapExternalCauseToDescriptionItemDataSummary(externalCause: ExternalCauseDto): ExternalCauseData {
        return {
            ...(externalCause.externalCauseType && { producedBy: [this.toDescriptionItemData(ExternalCauseType[externalCause.externalCauseType])] }),
            ...(externalCause.eventLocation && { eventLocation: [this.toDescriptionItemData(EventLocation[externalCause.eventLocation])] }),
            ...(externalCause.snomed && { howItHappened: [this.toDescriptionItemData(externalCause.snomed.pt)] }),
        }
    }

    mapExternalCauseToDescriptionItemDataInfo(externalCause: ExternalCauseDto): DescriptionItemDataInfo[] {
        let externalCauseData: ExternalCauseData = this.mapExternalCauseToDescriptionItemDataSummary(externalCause);
        return [{
            title: "internaciones.documents-summary.external-cause.PRODUCED_BY",
            dataId: "produced-by-note",
            descriptionData: externalCauseData.producedBy
        },
        {
            title: "internaciones.documents-summary.external-cause.EVENT_LOCATION",
            dataId: "event-location-note",
            descriptionData: externalCauseData.eventLocation
        },
        {
            title: "internaciones.documents-summary.external-cause.HOW_IT_HAPPENED",
            dataId: "how-it-happened-note",
            descriptionData: externalCauseData.howItHappened
        }]
    }
}