import { Injectable } from '@angular/core';
import { AnalgesicTechniqueDto, AnestheticHistoryDto, AnestheticReportDto, AnestheticSubstanceDto, AnestheticTechniqueDto, AnthropometricDataDto, DiagnosisDto, EInternmentPlace, HospitalizationProcedureDto, MasterDataDto, MeasuringPointDto, MedicationDto, PostAnesthesiaStatusDto, ProcedureDescriptionDto, RiskFactorDto } from '@api-rest/api-model';
import { dateDtoAndTimeDtoToDate, dateDtoToDate, dateTimeDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { capitalize } from '@core/utils/core.utils';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { ANESTHESIA_ZONE_ID, PREVIOUS_ANESTHESIA_STATE_ID } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { TranslateService } from '@ngx-translate/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { take } from 'rxjs';

const CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
const PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;
const INFO_DIVIDER = ' | ';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportDocumentSummaryService {

    private confirmedStatus: string = '';
    private presumptiveStatus: string = '';
    private premedicationViasArray: MasterDataDto[];
    private anestheticPlanViasArray: MasterDataDto[];
    private anestheticAgentViasArray: MasterDataDto[];
    private nonAnestheticDrugsViasArray: MasterDataDto[];
    private antibioticProphylaxisViasArray: MasterDataDto[];
    private anestheticTechniquesTypes: MasterDataDto[];
    private trachealIncubationTypes: MasterDataDto[];
    private breathingTypes: MasterDataDto[];
    private circuitTypes: MasterDataDto[];

    constructor(
        private readonly translateService: TranslateService,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) {
        this.confirmedStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.CONFIRMED')
        this.presumptiveStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.PRESUMPTIVE')
        this.loadMasterData();
    }

    private loadMasterData() {
        this.internacionMasterDataService.getViasPremedication().pipe(take(1)).subscribe(vias => this.premedicationViasArray = vias);
        this.internacionMasterDataService.getViasAnestheticPlan().pipe(take(1)).subscribe(vias => this.anestheticPlanViasArray = vias);
        this.internacionMasterDataService.getViasAnestheticAgent().pipe(take(1)).subscribe(vias => this.anestheticAgentViasArray = vias);
        this.internacionMasterDataService.getViasNonAnestheticDrug().pipe(take(1)).subscribe(vias => this.nonAnestheticDrugsViasArray = vias);
        this.internacionMasterDataService.getViasAntibioticProphylaxis().pipe(take(1)).subscribe(vias => this.antibioticProphylaxisViasArray = vias);
        this.internacionMasterDataService.getAnestheticTechniqueTypes().pipe(take(1)).subscribe(anestheticTechniquesTypes => this.anestheticTechniquesTypes = anestheticTechniquesTypes);
        this.internacionMasterDataService.getTrachealIntubationTypes().pipe(take(1)).subscribe(trachealIncubationTypes => this.trachealIncubationTypes = trachealIncubationTypes);
        this.internacionMasterDataService.getBreathingTypes().pipe(take(1)).subscribe(breathingTypes => this.breathingTypes = breathingTypes);
        this.internacionMasterDataService.getCircuitTypes().pipe(take(1)).subscribe(circuitTypes => this.circuitTypes = circuitTypes);
    }

    getAnestheticReportAsViewFormat(anestheticReport: AnestheticReportDto): AnestheticReportViewFormat {
        return {
            mainDiagnosis: anestheticReport.mainDiagnosis ? [{ description: this.getDescriptionAndStatus(anestheticReport.mainDiagnosis) }] : null,
            diagnosis: anestheticReport.diagnosis.length ? this.getDiagnosisAsStringArray(anestheticReport.diagnosis) : null,
            proposedSurgeries: anestheticReport.surgeryProcedures.length ? this.getProposedSurgeriesAsStringArray(anestheticReport.surgeryProcedures) : null,
            anthropometricData: anestheticReport.anthropometricData ? this.getAnthropometricDataAsStrings(anestheticReport.anthropometricData) : null,
            anesthesicClinicalEvaluation: anestheticReport.riskFactors ? this.getAnesthesicClinicalEvaluationAsStrings(anestheticReport.riskFactors) : null,
            anestheticHistory: this.getAnesthesiaHistoryAsStrings(anestheticReport.anestheticHistory),
            usualMedication: anestheticReport.medications.length ? this.getMedicationsAsStringArray(anestheticReport.medications) : null,
            premedicationList: anestheticReport.preMedications.length ? this.getAnestheticSubstanceDescription(anestheticReport.preMedications, this.premedicationViasArray) : null,
            lastFoodIntake: anestheticReport.foodIntake?.clockTime ? timeDtoToDate(anestheticReport.foodIntake.clockTime) : null,
            histories: this.hasHistories(anestheticReport) ? this.getHistoriesAsPersonalHistoriesData(anestheticReport) : null,
            anestheticPlanList: anestheticReport.anestheticPlans.length ? this.getAnestheticSubstanceDescription(anestheticReport.anestheticPlans, this.anestheticPlanViasArray) : null,
            analgesicTechniques: anestheticReport.analgesicTechniques.length ? this.getAnalgesicTechniques(anestheticReport.analgesicTechniques) : null,
            anestheticTechniques: anestheticReport.anestheticTechniques.length ? this.getAnestheticTechniques(anestheticReport.anestheticTechniques) : null,
            fluidAdministrations: anestheticReport.fluidAdministrations.length ? this.getFluidAdministrations(anestheticReport.fluidAdministrations) : null,
            anestheticAgents: anestheticReport.anestheticAgents.length ? this.getAnestheticSubstanceDescription(anestheticReport.anestheticAgents, this.anestheticAgentViasArray) : null,
            nonAnestheticDrugs: anestheticReport.nonAnestheticDrugs.length ? this.getAnestheticSubstanceDescription(anestheticReport.nonAnestheticDrugs, this.nonAnestheticDrugsViasArray) : null,
            intrasurgicalAnestheticProcedures: this.getIntrasurgicalAnestheticProcedures(anestheticReport.procedureDescription),
            antibioticProphylaxis: anestheticReport.antibioticProphylaxis.length ? this.getAnestheticSubstanceDescription(anestheticReport.antibioticProphylaxis, this.antibioticProphylaxisViasArray) : null,
            vitalSigns: !this.isVitalSignsEmpty(anestheticReport) ? this.getVitalSignsData(anestheticReport.procedureDescription, anestheticReport.measuringPoints, anestheticReport.anestheticChart) : null,
            endOfAnesthesiaStatus: this.getEndOfAnesthesiaStatusDescription(anestheticReport.postAnesthesiaStatus),
        }
    }

    private getDiagnosisAsStringArray(diagnosis: DiagnosisDto[]): DescriptionItemData[] {
        return diagnosis.map(diag => {
            return { description: this.getDescriptionAndStatus(diag) };
        })
    }

    private getDescriptionAndStatus(diagnosis: DiagnosisDto): string {
        if (diagnosis.verificationId === CONFIRMED) {
            return diagnosis.snomed.pt + ' ' + this.confirmedStatus
        }
        if (diagnosis.verificationId === PRESUMPTIVE) {
            return diagnosis.snomed.pt + ' ' + this.presumptiveStatus
        }
        return diagnosis.snomed.pt
    }

    private getProposedSurgeriesAsStringArray(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemData[] {
        return proposedSurgeries.map(proposedSurgery => {
            return { description: proposedSurgery.snomed.pt }
        })
    }

    private getAnthropometricDataAsStrings(antropometricData: AnthropometricDataDto): AnthropometricData {
        return {
            bloodType: antropometricData.bloodType ? [{ description: antropometricData.bloodType.value }] : null,
            height: antropometricData.height ? [{ description: antropometricData.height.value }] : null,
            weight: antropometricData.weight ? [{ description: antropometricData.weight.value + 'Kg' }] : null,
        }
    }

    private getAnesthesicClinicalEvaluationAsStrings(anesthesicClinicalEvaluation: RiskFactorDto): AnesthesicClinicalEvaluationData {
        return {
            maxBloodPressure: anesthesicClinicalEvaluation.systolicBloodPressure ? [{ description: anesthesicClinicalEvaluation.systolicBloodPressure.value }] : null,
            minBloodPressure: anesthesicClinicalEvaluation.diastolicBloodPressure ? [{ description: anesthesicClinicalEvaluation.diastolicBloodPressure.value }] : null,
            hematocrit: anesthesicClinicalEvaluation.hematocrit ? [{ description: anesthesicClinicalEvaluation.hematocrit.value + ' %' }] : null,
        }
    }

    private getAnesthesiaHistoryAsStrings(anesthesiaHistory: AnestheticHistoryDto): DescriptionItemData[] {
        return anesthesiaHistory?.stateId
            ? (anesthesiaHistory.zoneId
                ? [{ description: this.getAnesthesiaStateDescription(anesthesiaHistory.stateId, anesthesiaHistory.zoneId) }]
                : [{ description: this.getAnesthesiaStateDescription(anesthesiaHistory.stateId) }])
            : null
    }

    private getAnesthesiaStateDescription(stateId: number, zoneId?: number): string {
        switch (stateId) {
            case PREVIOUS_ANESTHESIA_STATE_ID.YES:
                let stateDescription = this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.YES')
                switch (zoneId) {
                    case ANESTHESIA_ZONE_ID.REGIONAL:
                        return stateDescription + ' ' + '(' + this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.anesthetic-zone.REGIONAL') + ')'
                    case ANESTHESIA_ZONE_ID.GENERAL:
                        return stateDescription + ' ' + '(' + this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.anesthetic-zone.GENERAL') + ')'
                    default:
                        return stateDescription + ' ' + '(' + this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.anesthetic-zone.BOTH') + ')'
                }
            case PREVIOUS_ANESTHESIA_STATE_ID.NO:
                return this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.NO')
            default:
                return this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.CANT_ANSWER')
        }
    }

    private getMedicationsAsStringArray(medications: MedicationDto[]): DescriptionItemData[] {
        return medications.map(medication => {
            return { description: medication.note ? medication.snomed.pt + INFO_DIVIDER + medication.note : medication.snomed.pt };
        })
    }

    private getViaTranslate(): string {
        return this.translateService.instant('historia-clinica.anesthetic-report.summary.VIA');
    }

    private getUnitTranslate(): string {
        return this.translateService.instant('historia-clinica.anesthetic-report.summary.UNIT');
    }

    private getDoseTranslate(): string {
        return this.translateService.instant('historia-clinica.anesthetic-report.summary.DOSE');
    }

    private hasHistories(anestheticReport: AnestheticReportDto): boolean {
        return (!!anestheticReport.histories.length || !!anestheticReport.procedureDescription?.note || !!anestheticReport.procedureDescription?.asa)
    }

    private getHistoriesAsPersonalHistoriesData(anestheticReport: AnestheticReportDto): PersonalHistoriesData {
        return {
            recordList: anestheticReport.histories?.map(history => { return { description: capitalize(history.snomed.pt) } }),
            observations: anestheticReport.procedureDescription?.note ? [{ description: anestheticReport.procedureDescription.note }] : null,
            asa: anestheticReport.procedureDescription?.asa ? [{ description: anestheticReport.procedureDescription.asa.toString() }] : null,
        }
    }

    private getAnestheticReportViaDescription(viasArray: MasterDataDto[], viaId: number): string {
        return viasArray.filter(via => via.id == viaId)[0].description;
    }

    private getAnalgesicTechniques(analgesicTechniques: AnalgesicTechniqueDto[]): DescriptionItemData[] {
        return analgesicTechniques.map(analgesicTechnique => {
            return {
                description: analgesicTechnique.snomed.pt + INFO_DIVIDER + analgesicTechnique.injectionNote + INFO_DIVIDER + this.getDoseTranslate() + analgesicTechnique.dosage.quantity.value + INFO_DIVIDER + this.getUnitTranslate()
                    + this.getCatetherValue(analgesicTechnique.catheter),
            }
        })
    }

    private getCatetherValue(catether: boolean): string {
        return catether ? this.translateService.instant('historia-clinica.anesthetic-report.summary.YES') : this.translateService.instant('historia-clinica.anesthetic-report.summary.NO');
    }

    private getAnestheticTechniques(anestheticTechniques: AnestheticTechniqueDto[]): DescriptionItemData[] {
        return anestheticTechniques.map(anestheticTechnique => {
            return {
                description: anestheticTechnique.snomed.pt
                    + this.getAnestheticTechniqueDescription(anestheticTechnique.techniqueId, this.translateService.instant('historia-clinica.anesthetic-report.summary.TECHNIQUE') , this.anestheticTechniquesTypes)
                    + this.getTrachealIntubationDescription(anestheticTechnique)
                    + this.getAnestheticTechniqueDescription(anestheticTechnique.breathingId, this.translateService.instant('historia-clinica.anesthetic-report.summary.BREATHING'), this.breathingTypes)
                    + this.getAnestheticTechniqueDescription(anestheticTechnique.circuitId, this.translateService.instant('historia-clinica.anesthetic-report.summary.CIRCUIT'), this.circuitTypes)
            }
        })
    }

    private mapToMasterData(typesData: MasterDataDto[], itemId: number): string {
        return typesData.filter(item => item.id == itemId)[0].description;
    }

    private getAnestheticTechniqueDescription(attributeId: number, prefix: string, types: MasterDataDto[]): string {
        let description = '';
        if (attributeId) {
            description = INFO_DIVIDER + prefix + this.mapToMasterData(types, attributeId);
        }
        return description
    }

    private getTrachealIntubationDescription(anestheticTechnique: AnestheticTechniqueDto): string {
        let description = '';
        if (anestheticTechnique.trachealIntubation) {
            description = INFO_DIVIDER + this.translateService.instant('historia-clinica.anesthetic-report.summary.TRACHEAL_INTUBATION') + this.getTrachealIntubationIdsDescription(anestheticTechnique.trachealIntubationMethodIds);
        }
        return description
    }

    private getTrachealIntubationIdsDescription(trachealIntubationIds: number[]): string {
        let description = '';
        trachealIntubationIds.map(id => {
            description = description.length ?
                description + ', ' + this.mapToMasterData(this.trachealIncubationTypes, id)
                : this.mapToMasterData(this.trachealIncubationTypes, id);
        })
        return description;
    }

    private getFluidAdministrations(fluidAdministrations: AnestheticSubstanceDto[]): DescriptionItemData[] {
        return fluidAdministrations.map(fluidAdministration => {
            return {
                description: fluidAdministration.dosage.quantity.value ?
                    fluidAdministration.snomed.pt + INFO_DIVIDER + this.translateService.instant('historia-clinica.anesthetic-report.summary.QUANTITY_USED') + fluidAdministration.dosage.quantity.value + fluidAdministration.dosage.quantity.unit
                    : fluidAdministration.snomed.pt,
            }
        })
    }

    private getIntrasurgicalAnestheticProcedures(procedures: ProcedureDescriptionDto): IntrasurgicalAnestheticProceduresData {
        return {
            venousAccess: procedures ? this.getProcedureValue(procedures.venousAccess) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            nasogastricTube: procedures ? this.getProcedureValue(procedures.nasogastricTube) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            urinaryCatheter: procedures ? this.getProcedureValue(procedures.urinaryCatheter) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
        }
    }

    private getProcedureValue(procedure: boolean): DescriptionItemData[]{
        return procedure != undefined ? 
                (procedure ? 
                    [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.YES') }] 
                    : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO') }])
                : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }]
    }

    private getAnestheticSubstanceDescription(itemArray: AnestheticSubstanceDto[], viasList: MasterDataDto[]):  DescriptionItemData[] {
        return itemArray.map(item => {
            return {
                description: item.snomed.pt + INFO_DIVIDER + this.getViaTranslate() + this.getAnestheticReportViaDescription(viasList, item.viaId) + INFO_DIVIDER + this.getDoseTranslate() + item.dosage.quantity.value + INFO_DIVIDER + this.getUnitTranslate() + item.dosage.quantity.unit,
                dateOrTime: { dateTime: dateTimeDtoToDate(item.dosage.startDateTime) }
            }
        })
    }

    private isVitalSignsEmpty(anestheticReport: AnestheticReportDto): boolean {
        return !(!!anestheticReport.measuringPoints?.length || this.hasStartAndEndDateTimes(anestheticReport.procedureDescription))
    }

    private getVitalSignsData(procedureDescription: ProcedureDescriptionDto, measuringPoints: MeasuringPointDto[], chart: string): VitalSignsData {
        return {
            startAndEndProceduresDateTime: this.hasStartAndEndDateTimes(procedureDescription) ? this.getDateTimes(procedureDescription) : null,
            chart: chart ? [chart] : null,
            measuringPoints: this.mapToMeasuringPointData(measuringPoints),
        }
    }

    private hasStartAndEndDateTimes(procedureDescription: ProcedureDescriptionDto): boolean {
        return !!procedureDescription?.anesthesiaEndDate 
            || !!procedureDescription?.anesthesiaEndTime 
            || !!procedureDescription?.anesthesiaStartDate 
            || !!procedureDescription?.anesthesiaStartTime 
            || !!procedureDescription?.surgeryEndDate 
            || !!procedureDescription?.surgeryEndTime 
            || !!procedureDescription?.surgeryStartDate 
            || !!procedureDescription?.surgeryStartTime 
    }

    private getDateTimes(procedureDescription: ProcedureDescriptionDto): StartAndEndProceduresDateTimeData {
        return {
            anesthesiaEndDate: procedureDescription?.anesthesiaEndDate ? dateDtoToDate(procedureDescription.anesthesiaEndDate) : null,
            anesthesiaEndTime: procedureDescription?.anesthesiaEndTime ? timeDtoToDate(procedureDescription.anesthesiaEndTime) : null,
            anesthesiaStartDate: procedureDescription?.anesthesiaStartDate ? dateDtoToDate(procedureDescription.anesthesiaStartDate) : null,
            anesthesiaStartTime: procedureDescription?.anesthesiaStartTime ? timeDtoToDate(procedureDescription.anesthesiaStartTime) : null,
            surgeryEndDate: procedureDescription?.surgeryEndDate ? dateDtoToDate(procedureDescription.surgeryEndDate) : null,
            surgeryEndTime: procedureDescription?.surgeryEndTime ? timeDtoToDate(procedureDescription.surgeryEndTime) : null,
            surgeryStartDate: procedureDescription?.surgeryStartDate ? dateDtoToDate(procedureDescription.surgeryStartDate) : null,
            surgeryStartTime: procedureDescription?.surgeryStartTime ? timeDtoToDate(procedureDescription.surgeryStartTime) : null,
        }
    }

    private mapToMeasuringPointData(measuringPoints: MeasuringPointDto[]): MeasuringPointData[] {
        return measuringPoints.map(measuringPoint => {
            return {
                dateTime: dateDtoAndTimeDtoToDate(measuringPoint.date, measuringPoint.time),
                ...measuringPoint
            }
        })
    }

    private getEndOfAnesthesiaStatusDescription(postAnesthesiaStatus: PostAnesthesiaStatusDto): EndOfAnesthesiaStatusData {
        return {
            intentionalSensitivity: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.intentionalSensitivity) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            cornealReflex: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.cornealReflex) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            obeyOrders: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.obeyOrders) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            talk: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.talk) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            respiratoryDepression: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.respiratoryDepression) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            circulatoryDepression: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.circulatoryDepression) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            vomiting: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.vomiting) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            curated: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.curated) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            trachealCannula: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.trachealCannula) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            pharyngealCannula: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.pharyngealCannula) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            internment: postAnesthesiaStatus ? this.getIntermentDescription(postAnesthesiaStatus.internment, postAnesthesiaStatus.internmentPlace) : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
            note: postAnesthesiaStatus && postAnesthesiaStatus.note ? [{ description: postAnesthesiaStatus.note }] : [{ description: this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION') }],
        }
    }

    private getIntermentDescription(interment: boolean, internmentPlace: EInternmentPlace): DescriptionItemData[] {
        let intermentPlaceDescription = interment ? ' - ' + this.getInternmentPlaceDescription(internmentPlace) : '';
        return [{
            description: this.getProcedureValue(interment)[0].description + intermentPlaceDescription
        }]
    }

    private getInternmentPlaceDescription(internmentPlace: EInternmentPlace): string {
        if (internmentPlace === EInternmentPlace.FLOOR) return this.translateService.instant('historia-clinica.anesthetic-report.summary.FLOOR')
        if (internmentPlace === EInternmentPlace.INTENSIVE_CARE_UNIT) return this.translateService.instant('historia-clinica.anesthetic-report.summary.INTENSIVE_CARE_UNIT')
        return this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION')
    }
}

export interface AnestheticReportViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    proposedSurgeries: DescriptionItemData[],
    anthropometricData: AnthropometricData,
    anesthesicClinicalEvaluation: AnesthesicClinicalEvaluationData,
    anestheticHistory: DescriptionItemData[],
    usualMedication: DescriptionItemData[],
    premedicationList: DescriptionItemData[],
    lastFoodIntake: Date,
    histories: PersonalHistoriesData,
    anestheticPlanList: DescriptionItemData[],
    analgesicTechniques: DescriptionItemData[],
    anestheticTechniques: DescriptionItemData[],
    fluidAdministrations: DescriptionItemData[],
    anestheticAgents: DescriptionItemData[],
    nonAnestheticDrugs: DescriptionItemData[],
    intrasurgicalAnestheticProcedures: IntrasurgicalAnestheticProceduresData,
    antibioticProphylaxis: DescriptionItemData[],
    vitalSigns: VitalSignsData,
    endOfAnesthesiaStatus: EndOfAnesthesiaStatusData, 
}

export interface AnthropometricData {
    bloodType: DescriptionItemData[],
    height: DescriptionItemData[],
    weight: DescriptionItemData[],
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