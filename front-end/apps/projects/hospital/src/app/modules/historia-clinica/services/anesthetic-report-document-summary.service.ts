import { Injectable } from '@angular/core';
import { AnalgesicTechniqueDto, AnestheticHistoryDto, AnestheticReportDto, AnestheticSubstanceDto, AnestheticTechniqueDto, EInternmentPlace, HospitalizationProcedureDto, MasterDataDto, MeasuringPointDto, PostAnesthesiaStatusDto, ProcedureDescriptionDto, RiskFactorDto } from '@api-rest/api-model';
import { dateDtoAndTimeDtoToDate, dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { capitalize } from '@core/utils/core.utils';
import { ANESTHESIA_ZONE_ID, PREVIOUS_ANESTHESIA_STATE_ID } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { TranslateService } from '@ngx-translate/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { take } from 'rxjs';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { AnesthesicClinicalEvaluationData, AnthropometricData, EndOfAnesthesiaStatusData, IntrasurgicalAnestheticProceduresData, MeasuringPointData, PersonalHistoriesData, ProceduresDateTimeData, VitalSignsData } from '@historia-clinica/utils/document-summary.model';
import { ANALGESIC_TECHNIQUE_DESCRIPTION_ITEM, ANESTHESIC_CLINICAL_EVALUATION, ANESTHETIC_AGENTS_DESCRIPTION_ITEM, ANESTHETIC_HISTORY_DESCRIPTION_ITEM, ANESTHETIC_PLAN_DESCRIPTION_ITEM, ANESTHETIC_TECHNIQUE_DESCRIPTION_ITEM, ANTIBIOTIC_PROPHYLAXIS_DESCRIPTION_ITEM, FLUID_ADMINISTRATION_DESCRIPTION_ITEM, NON_ANESTHETIC_DRUGS_DESCRIPTION_ITEM, PROPOSED_SURGERIES_DESCRIPTION_ITEM } from '@historia-clinica/constants/document-summary.constants';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';

const INFO_DIVIDER = ' | ';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportDocumentSummaryService {

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
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) {
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

    mapToAnestheticReportViewFormat(anestheticReport: AnestheticReportDto): AnestheticReportViewFormat {
        return {
            ...(anestheticReport.mainDiagnosis && { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(anestheticReport.mainDiagnosis) }] }),
            ...(anestheticReport.diagnosis.length && { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(anestheticReport.diagnosis) }),
            ...(anestheticReport.surgeryProcedures.length && { proposedSurgeries: this.mapProposedSurgeriesToDescriptionItemDataSummary(anestheticReport.surgeryProcedures) }),
            ...(anestheticReport.anthropometricData && { anthropometricData: this.documentsSummaryService.mapToAnthropometricData(anestheticReport.anthropometricData) }),
            ...(anestheticReport.riskFactors && { anesthesicClinicalEvaluation: this.mapToAnesthesicClinicalEvaluation(anestheticReport.riskFactors) }),
            ...(anestheticReport.anestheticHistory && {anestheticHistory: this.mapAnesthesiaHistoryToDescriptionItemDataSummary(anestheticReport.anestheticHistory)}),
            ...(anestheticReport.medications.length && { usualMedication: this.documentsSummaryService.mapMedicationsToDescriptionItemDataSummary(anestheticReport.medications) }),
            ...(anestheticReport.preMedications.length && { premedicationList: this.mapAnestheticSubstanceToDescriptionItemData(anestheticReport.preMedications, this.premedicationViasArray) }),
            ...(anestheticReport.foodIntake?.clockTime && { lastFoodIntake: timeDtoToDate(anestheticReport.foodIntake.clockTime) }),
            ...(this.hasHistories(anestheticReport) && { histories: this.mapToPersonalHistoriesData(anestheticReport) }),
            ...(anestheticReport.anestheticPlans.length && { anestheticPlanList: this.mapAnestheticPlansToDescriptionItemDataSummary(anestheticReport.anestheticPlans) }),
            ...(anestheticReport.analgesicTechniques.length && { analgesicTechniques: this.mapAnalgesicTechniqueToDescriptionItemDataSummary(anestheticReport.analgesicTechniques) }),
            ...(anestheticReport.anestheticTechniques.length && { anestheticTechniques: this.mapAnestheticTechniqueToDescriptionItemDataSummary(anestheticReport.anestheticTechniques) }),
            ...(anestheticReport.fluidAdministrations.length && { fluidAdministrations: this.mapFluidAdministrationToDescriptionItemDataSummary(anestheticReport.fluidAdministrations) }),
            ...(anestheticReport.anestheticAgents.length && { anestheticAgents: this.mapAnestheticAgentsToDescriptionItemDataSummary(anestheticReport.anestheticAgents) }),
            ...(anestheticReport.nonAnestheticDrugs.length && { nonAnestheticDrugs: this.mapNonAnestheticDrugsToDescriptionItemDataSummary(anestheticReport.nonAnestheticDrugs) }),
            intrasurgicalAnestheticProcedures: this.mapToIntrasurgicalAnestheticProceduresData(anestheticReport.procedureDescription),
            ...(anestheticReport.antibioticProphylaxis.length && { antibioticProphylaxis: this.mapAntibioticProphylaxisToDescriptionItemDataSummary(anestheticReport.antibioticProphylaxis) }),
            ...(!this.isVitalSignsEmpty(anestheticReport) && { vitalSigns: this.mapToVitalSignsData(anestheticReport.procedureDescription, anestheticReport.measuringPoints, anestheticReport.anestheticChart) }),
            endOfAnesthesiaStatus: this.getEndOfAnesthesiaStatusDescription(anestheticReport.postAnesthesiaStatus),
        }
    }

    private mapProposedSurgeriesToDescriptionItemData(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemData[] {
        return proposedSurgeries.map(proposedSurgery => this.documentsSummaryService.toDescriptionItemData(proposedSurgery.snomed.pt))
    }

    private mapToAnesthesicClinicalEvaluation(anesthesicClinicalEvaluation: RiskFactorDto): AnesthesicClinicalEvaluationData {
        return {
            ...(anesthesicClinicalEvaluation.systolicBloodPressure && { maxBloodPressure: [this.documentsSummaryService.toDescriptionItemData(anesthesicClinicalEvaluation.systolicBloodPressure.value)] }),
            ...(anesthesicClinicalEvaluation.diastolicBloodPressure && { minBloodPressure: [this.documentsSummaryService.toDescriptionItemData(anesthesicClinicalEvaluation.diastolicBloodPressure.value)] }),
            ...(anesthesicClinicalEvaluation.hematocrit && { hematocrit: [this.documentsSummaryService.toDescriptionItemData(`${anesthesicClinicalEvaluation.hematocrit.value} ${ANESTHESIC_CLINICAL_EVALUATION.PERCENTAJE}`)] }),
        }
    }

    private mapAnesthesiaHistoryToDescriptionItemData(anesthesiaHistory: AnestheticHistoryDto): DescriptionItemData[] {
        return anesthesiaHistory?.stateId
            ? (anesthesiaHistory.zoneId
                ? [this.documentsSummaryService.toDescriptionItemData(this.getAnesthesiaStateDescription(anesthesiaHistory.stateId, anesthesiaHistory.zoneId))]
                : [this.documentsSummaryService.toDescriptionItemData(this.getAnesthesiaStateDescription(anesthesiaHistory.stateId))])
            : null
    }

    private getAnesthesiaStateDescription(stateId: number, zoneId?: number): string {
        switch (stateId) {
            case PREVIOUS_ANESTHESIA_STATE_ID.YES:
                let stateDescription = this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.YES')
                switch (zoneId) {
                    case ANESTHESIA_ZONE_ID.REGIONAL:
                        return `${stateDescription} (${this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.anesthetic-zone.REGIONAL')})`
                    case ANESTHESIA_ZONE_ID.GENERAL:
                        return `${stateDescription} (${this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.anesthetic-zone.GENERAL')})`
                    default:
                        return `${stateDescription} (${this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.anesthetic-zone.BOTH')})`
                }
            case PREVIOUS_ANESTHESIA_STATE_ID.NO:
                return this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.NO')
            default:
                return this.translateService.instant('internaciones.anesthesic-report.anesthetic-history.anesthetic-history-options.CANT_ANSWER')
        }
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

    private mapToPersonalHistoriesData(anestheticReport: AnestheticReportDto): PersonalHistoriesData {
        return {
            recordList: anestheticReport.histories?.map(history => this.documentsSummaryService.toDescriptionItemData(capitalize(history.snomed.pt))),
            ...(anestheticReport.procedureDescription?.note && { observations: [this.documentsSummaryService.toDescriptionItemData(anestheticReport.procedureDescription.note)] }),
            ...(anestheticReport.procedureDescription?.asa && { asa: [this.documentsSummaryService.toDescriptionItemData(anestheticReport.procedureDescription.asa.toString())] }),
        }
    }

    private getAnestheticReportViaDescription(viasArray: MasterDataDto[], viaId: number): string {
        return viasArray.filter(via => via.id == viaId)[0].description;
    }

    private mapAnalgesicTechniquesToDescriptionItemData(analgesicTechniques: AnalgesicTechniqueDto[]): DescriptionItemData[] {
        return analgesicTechniques.map(analgesicTechnique => this.documentsSummaryService.toDescriptionItemData(this.getAnalgesicTechniqueDescription(analgesicTechnique)));
    }

    private getAnalgesicTechniqueDescription(analgesicTechnique: AnalgesicTechniqueDto): string {
        return `${analgesicTechnique.snomed.pt} ${INFO_DIVIDER} ${analgesicTechnique.injectionNote} ${INFO_DIVIDER} ${this.getDoseTranslate()} ${analgesicTechnique.dosage.quantity.value} 
            ${INFO_DIVIDER} ${this.getUnitTranslate()} ${this.getCatetherValue(analgesicTechnique.catheter)}`;
    }

    private getCatetherValue(catether: boolean): string {
        return catether ? this.translateService.instant('historia-clinica.anesthetic-report.summary.YES') : this.translateService.instant('historia-clinica.anesthetic-report.summary.NO');
    }

    private getAnestheticTechniques(anestheticTechniques: AnestheticTechniqueDto[]): DescriptionItemData[] {
        return anestheticTechniques.map(anestheticTechnique => this.documentsSummaryService.toDescriptionItemData(this.getAnestheticTechnique(anestheticTechnique)));
    }

    private getAnestheticTechnique(anestheticTechnique: AnestheticTechniqueDto): string {
        return `${anestheticTechnique.snomed.pt}
                 ${this.getAnestheticTechniqueDescription(anestheticTechnique.techniqueId, this.translateService.instant('historia-clinica.anesthetic-report.summary.TECHNIQUE'), this.anestheticTechniquesTypes)}
                 ${this.getTrachealIntubationDescription(anestheticTechnique)}
                 ${this.getAnestheticTechniqueDescription(anestheticTechnique.breathingId, this.translateService.instant('historia-clinica.anesthetic-report.summary.BREATHING'), this.breathingTypes)}
                 ${this.getAnestheticTechniqueDescription(anestheticTechnique.circuitId, this.translateService.instant('historia-clinica.anesthetic-report.summary.CIRCUIT'), this.circuitTypes)}`
    }

    private mapToMasterData(typesData: MasterDataDto[], itemId: number): string {
        return typesData.filter(item => item.id == itemId)[0].description;
    }

    private getAnestheticTechniqueDescription(attributeId: number, prefix: string, types: MasterDataDto[]): string {
        let description = '';
        if (attributeId) {
            description = `${INFO_DIVIDER} ${prefix} ${this.mapToMasterData(types, attributeId)}`;
        }
        return description
    }

    private getTrachealIntubationDescription(anestheticTechnique: AnestheticTechniqueDto): string {
        let description = '';
        if (anestheticTechnique.trachealIntubation) {
            description = `${INFO_DIVIDER} ${this.translateService.instant('historia-clinica.anesthetic-report.summary.TRACHEAL_INTUBATION')} 
                ${this.getTrachealIntubationIdsDescription(anestheticTechnique.trachealIntubationMethodIds)}`;
        }
        return description
    }

    private getTrachealIntubationIdsDescription(trachealIntubationIds: number[]): string {
        let description = '';
        trachealIntubationIds.map(id => {
            description = description.length ?
                `${description} , ${this.mapToMasterData(this.trachealIncubationTypes, id)}`
                : this.mapToMasterData(this.trachealIncubationTypes, id);
        })
        return description;
    }

    private mapFluidAdministrationsToDescriptionItemData(fluidAdministrations: AnestheticSubstanceDto[]): DescriptionItemData[] {
        return fluidAdministrations.map(fluidAdministration => this.documentsSummaryService.toDescriptionItemData(this.getFluidAdministration(fluidAdministration)));
    }

    private getFluidAdministration(fluidAdministration: AnestheticSubstanceDto): string {
        return fluidAdministration.dosage.quantity.value ?
            `${fluidAdministration.snomed.pt} ${INFO_DIVIDER} ${this.translateService.instant('historia-clinica.anesthetic-report.summary.QUANTITY_USED')} 
            ${fluidAdministration.dosage.quantity.value} ${fluidAdministration.dosage.quantity.unit}`
            : fluidAdministration.snomed.pt
    }

    private mapToIntrasurgicalAnestheticProceduresData(procedures: ProcedureDescriptionDto): IntrasurgicalAnestheticProceduresData {
        return {
            venousAccess: procedures ? this.getProcedureValue(procedures.venousAccess) 
                : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            nasogastricTube: procedures ? this.getProcedureValue(procedures.nasogastricTube) 
                : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            urinaryCatheter: procedures ? this.getProcedureValue(procedures.urinaryCatheter) 
                : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
        }
    }

    private getProcedureValue(procedure: boolean): DescriptionItemData[] {
        return procedure != undefined ?
            (procedure ?
                [ this.documentsSummaryService.toDescriptionItemData(this.translateService.instant('historia-clinica.anesthetic-report.summary.YES')) ]
                : [ this.documentsSummaryService.toDescriptionItemData(this.translateService.instant('historia-clinica.anesthetic-report.summary.NO')) ])
            : [ this.documentsSummaryService.getNoInformationAsDescriptionItemData() ]
    }

    private mapAnestheticSubstanceToDescriptionItemData(itemArray: AnestheticSubstanceDto[], viasList: MasterDataDto[]): DescriptionItemData[] {
        return itemArray.map(item => this.documentsSummaryService.toDescriptionItemData(
            this.getAnestheticSubstanceDescription(item, viasList), this.documentsSummaryService.mapDateTimeDtoToDateToShow(item.dosage.startDateTime)
        ))
    }

    private getAnestheticSubstanceDescription(item: AnestheticSubstanceDto, viasList: MasterDataDto[]): string {
        return `${item.snomed.pt} ${INFO_DIVIDER} ${this.getViaTranslate()} ${this.getAnestheticReportViaDescription(viasList, item.viaId)} ${INFO_DIVIDER} ${this.getDoseTranslate()} 
            ${item.dosage.quantity.value} ${INFO_DIVIDER} ${this.getUnitTranslate()} ${item.dosage.quantity.unit}`
    }

    private isVitalSignsEmpty(anestheticReport: AnestheticReportDto): boolean {
        return !(!!anestheticReport.measuringPoints?.length || this.hasStartAndEndDateTimes(anestheticReport.procedureDescription))
    }

    private mapToVitalSignsData(procedureDescription: ProcedureDescriptionDto, measuringPoints: MeasuringPointDto[], vitalSignsChart: string): VitalSignsData {
        return {
            ...(this.hasStartAndEndDateTimes(procedureDescription) && { startAndEndProceduresDateTime: this.getDateTimes(procedureDescription) }),
            ...(vitalSignsChart && { vitalSignsChart: [vitalSignsChart] }),
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

    private getDateTimes(procedureDescription: ProcedureDescriptionDto): ProceduresDateTimeData {
        return {
            ...(procedureDescription?.anesthesiaEndDate && { anesthesiaEndDate: dateDtoToDate(procedureDescription.anesthesiaEndDate) }),
            ...(procedureDescription?.anesthesiaEndTime && { anesthesiaEndTime: timeDtoToDate(procedureDescription.anesthesiaEndTime) }),
            ...(procedureDescription?.anesthesiaStartDate && { anesthesiaStartDate: dateDtoToDate(procedureDescription.anesthesiaStartDate) }),
            ...(procedureDescription?.anesthesiaStartTime && { anesthesiaStartTime: timeDtoToDate(procedureDescription.anesthesiaStartTime) }),
            ...(procedureDescription?.surgeryEndDate && { surgeryEndDate: dateDtoToDate(procedureDescription.surgeryEndDate) }),
            ...(procedureDescription?.surgeryEndTime && { surgeryEndTime: timeDtoToDate(procedureDescription.surgeryEndTime) }),
            ...(procedureDescription?.surgeryStartDate && { surgeryStartDate: dateDtoToDate(procedureDescription.surgeryStartDate) }),
            ...(procedureDescription?.surgeryStartTime && { surgeryStartTime: timeDtoToDate(procedureDescription.surgeryStartTime) }),
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
            intentionalSensitivity: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.intentionalSensitivity) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            cornealReflex: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.cornealReflex) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            obeyOrders: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.obeyOrders) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            talk: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.talk) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            respiratoryDepression: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.respiratoryDepression) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            circulatoryDepression: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.circulatoryDepression) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            vomiting: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.vomiting) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            curated: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.curated) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            trachealCannula: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.trachealCannula) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            pharyngealCannula: postAnesthesiaStatus ? this.getProcedureValue(postAnesthesiaStatus.pharyngealCannula) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            internment: postAnesthesiaStatus ? this.mapIntermentDescriptionToDescriptionItemData(postAnesthesiaStatus.internment, postAnesthesiaStatus.internmentPlace) : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
            note: postAnesthesiaStatus && postAnesthesiaStatus.note ? [this.documentsSummaryService.toDescriptionItemData(postAnesthesiaStatus.note)] : [this.documentsSummaryService.getNoInformationAsDescriptionItemData()],
        }
    }

    private mapIntermentDescriptionToDescriptionItemData(interment: boolean, internmentPlace: EInternmentPlace): DescriptionItemData[] {
        let intermentPlaceDescription = interment ? `- ${this.getInternmentPlaceDescription(internmentPlace)}` : '';
        return [ this.documentsSummaryService.toDescriptionItemData(`${this.getProcedureValue(interment)[0].description} ${intermentPlaceDescription}`) ]
    }

    private getInternmentPlaceDescription(internmentPlace: EInternmentPlace): string {
        if (internmentPlace === EInternmentPlace.FLOOR) return this.translateService.instant('historia-clinica.anesthetic-report.summary.FLOOR')
        if (internmentPlace === EInternmentPlace.INTENSIVE_CARE_UNIT) return this.translateService.instant('historia-clinica.anesthetic-report.summary.INTENSIVE_CARE_UNIT')
        return this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION')
    }

    mapProposedSurgeriesToDescriptionItemDataSummary(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapProposedSurgeriesToDescriptionItemData(proposedSurgeries),
            ...PROPOSED_SURGERIES_DESCRIPTION_ITEM,
        }
    }

    mapAnesthesiaHistoryToDescriptionItemDataSummary(anesthesiaHistory: AnestheticHistoryDto): DescriptionItemDataSummary {
        return {
            summary: this.mapAnesthesiaHistoryToDescriptionItemData(anesthesiaHistory),
            ...ANESTHETIC_HISTORY_DESCRIPTION_ITEM,
        }
    }

    mapAnestheticPlansToDescriptionItemDataSummary(itemArray: AnestheticSubstanceDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAnestheticSubstanceToDescriptionItemData(itemArray, this.anestheticPlanViasArray),
            ...ANESTHETIC_PLAN_DESCRIPTION_ITEM,
        }
    }

    mapAnalgesicTechniqueToDescriptionItemDataSummary(analgesicTechniques: AnalgesicTechniqueDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAnalgesicTechniquesToDescriptionItemData(analgesicTechniques),
            ...ANALGESIC_TECHNIQUE_DESCRIPTION_ITEM,
        }
    }

    mapAnestheticTechniqueToDescriptionItemDataSummary(anestheticTechniques: AnestheticTechniqueDto[]): DescriptionItemDataSummary {
        return {
            summary: this.getAnestheticTechniques(anestheticTechniques),
            ...ANESTHETIC_TECHNIQUE_DESCRIPTION_ITEM,
        }
    }

    mapFluidAdministrationToDescriptionItemDataSummary(fluidAdministrations: AnestheticTechniqueDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapFluidAdministrationsToDescriptionItemData(fluidAdministrations),
            ...FLUID_ADMINISTRATION_DESCRIPTION_ITEM,
        }
    }

    mapAnestheticAgentsToDescriptionItemDataSummary(itemArray: AnestheticSubstanceDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAnestheticSubstanceToDescriptionItemData(itemArray, this.anestheticAgentViasArray),
            ...ANESTHETIC_AGENTS_DESCRIPTION_ITEM,
        }
    }

    mapNonAnestheticDrugsToDescriptionItemDataSummary(itemArray: AnestheticSubstanceDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAnestheticSubstanceToDescriptionItemData(itemArray, this.nonAnestheticDrugsViasArray),
            ...NON_ANESTHETIC_DRUGS_DESCRIPTION_ITEM,
        }
    }

    mapAntibioticProphylaxisToDescriptionItemDataSummary(itemArray: AnestheticSubstanceDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAnestheticSubstanceToDescriptionItemData(itemArray, this.antibioticProphylaxisViasArray) ,
            ...ANTIBIOTIC_PROPHYLAXIS_DESCRIPTION_ITEM,
        }
    }
}

export interface AnestheticReportViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    proposedSurgeries: DescriptionItemDataSummary,
    anthropometricData: AnthropometricData,
    anesthesicClinicalEvaluation: AnesthesicClinicalEvaluationData,
    anestheticHistory: DescriptionItemDataSummary,
    usualMedication: DescriptionItemDataSummary,
    premedicationList: DescriptionItemData[],
    lastFoodIntake: Date,
    histories: PersonalHistoriesData,
    anestheticPlanList: DescriptionItemDataSummary,
    analgesicTechniques: DescriptionItemDataSummary,
    anestheticTechniques: DescriptionItemDataSummary,
    fluidAdministrations: DescriptionItemDataSummary,
    anestheticAgents: DescriptionItemDataSummary,
    nonAnestheticDrugs: DescriptionItemDataSummary,
    intrasurgicalAnestheticProcedures: IntrasurgicalAnestheticProceduresData,
    antibioticProphylaxis: DescriptionItemDataSummary,
    vitalSigns: VitalSignsData,
    endOfAnesthesiaStatus: EndOfAnesthesiaStatusData,
}