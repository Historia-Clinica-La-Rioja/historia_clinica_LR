import { DescriptionItemData } from "@presentation/components/description-item/description-item.component";

export interface HeaderDescription {
    title: string,
    edit: boolean,
    delete: boolean,
    headerDescriptionData: HeaderDescriptionData,
}

export interface HeaderDescriptionData {
    scope: string,
    specialty: string,
    dateTime: string,
    professional: string,
    institution: string,
    sector: string,
    room: string,
    bed: string,
}

export interface AnthropometricData {
    bloodType: DescriptionItemData[],
    height: DescriptionItemData[],
    weight: DescriptionItemData[],
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
    startAndEndProceduresDateTime: StartAndEndProceduresDateTimeData,
    chart: string[],
    measuringPoints: MeasuringPointData[],
}

export interface StartAndEndProceduresDateTimeData {
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