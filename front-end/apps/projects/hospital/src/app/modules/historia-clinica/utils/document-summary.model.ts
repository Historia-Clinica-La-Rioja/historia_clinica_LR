import { DescriptionItemData } from "@presentation/components/description-item/description-item.component";
import { IDENTIFIER_CASES } from "../../hsi-components/identifier-cases/identifier-cases.component";
import { DescriptionItemDataSummary } from "@historia-clinica/components/description-item-data-summary/description-item-data-summary.component";

export interface IdentifierData {
    value: string,
    dataId: string,
    identifierCase: IDENTIFIER_CASES,
}

export interface HeaderIdentifierData {
    title: string,
    edit?: boolean,
    delete?: boolean,
    download?: boolean,
    headerDescriptionData?: IdentifierData[],
}

export interface HeaderDescription {
    title: string,
    edit?: boolean,
    delete?: boolean,
    headerDescriptionData: HeaderDescriptionData,
}

export interface HeaderDescriptionData {
    scope?: string,
    patient?: string,
    specialty?: string,
    dateTime?: string,
    professional?: string,
    institution?: string,
    sector?: string,
    room?: string,
    bed?: string,
}

export interface AnthropometricData {
    bloodType: DescriptionItemData[],
    height: DescriptionItemData[],
    weight: DescriptionItemData[],
    headCircunference?: DescriptionItemData[],
}

export interface VitalSignsAndRiskFactorsData {
    bloodGlucose?: DescriptionItemData[];
    bloodOxygenSaturation?: DescriptionItemData[];
    cardiovascularRisk?: DescriptionItemData[];
    diastolicBloodPressure?: DescriptionItemData[];
    glycosylatedHemoglobin?: DescriptionItemData[];
    heartRate?: DescriptionItemData[];
    hematocrit?: DescriptionItemData[];
    respiratoryRate?: DescriptionItemData[];
    systolicBloodPressure?: DescriptionItemData[];
    temperature?: DescriptionItemData[];
}


export interface AnesthesicClinicalEvaluationData {
    maxBloodPressure: DescriptionItemData[],
    minBloodPressure: DescriptionItemData[],
    hematocrit: DescriptionItemData[],
}

export interface PersonalHistoriesData {
    recordList: DescriptionItemData[],
    observations: DescriptionItemData[],
    asa: DescriptionItemData[]
}

export interface IntrasurgicalAnestheticProceduresData {
    venousAccess: DescriptionItemData[],
    nasogastricTube: DescriptionItemData[],
    urinaryCatheter: DescriptionItemData[],
}

export interface VitalSignsData {
    startAndEndProceduresDateTime: ProceduresDateTimeData,
    vitalSignsChart: string[],
    measuringPoints: MeasuringPointData[],
}

export interface ProceduresDateTimeData {
    anesthesiaEndDate?: Date;
    anesthesiaEndTime?: Date;
    anesthesiaStartDate?: Date;
    anesthesiaStartTime?: Date;
    surgeryEndDate?: Date;
    surgeryEndTime?: Date;
    surgeryStartDate?: Date;
    surgeryStartTime?: Date;
}

export interface MeasuringPointData {
    bloodPressureMax?: number;
    bloodPressureMin?: number;
    bloodPulse?: number;
    co2EndTidal?: number;
    dateTime: Date;
    o2Saturation?: number;
}

export interface EndOfAnesthesiaStatusData {
    intentionalSensitivity?: DescriptionItemData[];
    cornealReflex?: DescriptionItemData[];
    obeyOrders?: DescriptionItemData[];
    talk?: DescriptionItemData[];
    respiratoryDepression?: DescriptionItemData[];
    circulatoryDepression?: DescriptionItemData[];
    vomiting?: DescriptionItemData[];
    curated?: DescriptionItemData[];
    trachealCannula?: DescriptionItemData[];
    pharyngealCannula?: DescriptionItemData[];
    internment?: DescriptionItemData[];
    note?: DescriptionItemData[];
}

export interface ClinicalEvaluationData {
    clinicalImpressionNote: DescriptionItemData[];
    currentIllnessNote: DescriptionItemData[];
    evolutionNote: DescriptionItemData[];
    indicationsNote: DescriptionItemData[];
    otherNote: DescriptionItemData[];
    physicalExamNote: DescriptionItemData[];
    studiesSummaryNote: DescriptionItemData[];
}

export interface ExternalCauseData {
    producedBy: DescriptionItemData[];
    eventLocation: DescriptionItemData[];
    howItHappened: DescriptionItemData[];
}

export interface DescriptionItemDataInfo {
    title: string;
    dataId: string;
    descriptionData: DescriptionItemData[];
}

export interface ObstetricEventData {
    previousPregnancies?: DescriptionItemData[];
    currentPregnancyEndDate?: DescriptionItemData[];
    gestationalAge?: DescriptionItemData[];
    pregnancyTerminationType?: DescriptionItemData[];
}

export interface NewBornsData {
    birthWeight: DescriptionItemData[];
    birthCondition: DescriptionItemData[];
    gender: DescriptionItemData[];
}

export interface ObstetricEventInfo {
    obstetricEvent: DescriptionItemDataInfo[];
    newBorns: NewBornsSummary[];
}

export interface NewBornsSummary {
    newBornSummary: DescriptionItemDataInfo[];
}

export interface ReferredDescriptionItemData {
    isReferred: boolean,
    notReferredText: string,
    content: DescriptionItemDataSummary,
}