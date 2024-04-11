import { Injectable } from '@angular/core';
import { AnestheticHistoryDto, AnestheticReportDto, AnestheticSubstanceDto, AnthropometricDataDto, DiagnosisDto, HospitalizationProcedureDto, MasterDataDto, MedicationDto, RiskFactorDto } from '@api-rest/api-model';
import { dateTimeDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { capitalize } from '@core/utils/core.utils';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { ANESTHESIA_ZONE_ID, PREVIOUS_ANESTHESIA_STATE_ID } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { TranslateService } from '@ngx-translate/core';
import { take } from 'rxjs';

const CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
const PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportDocumentSummaryService {

    private confirmedStatus: string = '';
    private presumptiveStatus: string = '';
    private viasArray: MasterDataDto[];
    private anestheticPlanViasArray: MasterDataDto[];

    constructor(
		private readonly translateService: TranslateService,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) { 
        this.confirmedStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.CONFIRMED')
        this.presumptiveStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.PRESUMPTIVE')
        this.internacionMasterDataService.getViasPremedication().pipe(take(1)).subscribe(vias => this.viasArray = vias);
        this.internacionMasterDataService.getViasAnestheticPlan().pipe(take(1)).subscribe(vias => this.anestheticPlanViasArray = vias);
    }

    getAnestheticReportAsViewFormat(anestheticReport: AnestheticReportDto): AnestheticReportViewFormat {
        return {
            mainDiagnosis: anestheticReport.mainDiagnosis ? [this.getDescriptionAndStatus(anestheticReport.mainDiagnosis)] : null,
            diagnosis: anestheticReport.diagnosis.length ? this.getDiagnosisAsStringArray(anestheticReport.diagnosis) : null,
            proposedSurgeries: anestheticReport.surgeryProcedures.length ? this.getProposedSurgeriesAsStringArray(anestheticReport.surgeryProcedures) : null,
            anthropometricData: anestheticReport.anthropometricData ? this.getAnthropometricDataAsStrings(anestheticReport.anthropometricData) : null,
            anesthesicClinicalEvaluation: anestheticReport.riskFactors ? this.getAnesthesicClinicalEvaluationAsStrings(anestheticReport.riskFactors) : null,
            anestheticHistory: this.getAnesthesiaHistoryAsStrings(anestheticReport.anestheticHistory),
            usualMedication: anestheticReport.medications.length ? this.getMedicationsAsStringArray(anestheticReport.medications) : null,
            premedicationList: anestheticReport.preMedications.length ? this.getPremedicationData(anestheticReport.preMedications) : null,
            lastFoodIntake: anestheticReport.foodIntake.clockTime ? timeDtoToDate(anestheticReport.foodIntake.clockTime) : null,
            histories: this.hasHistories(anestheticReport) ? this.getHistoriesAsPersonalHistoriesData(anestheticReport) : null,
            anestheticPlanList: anestheticReport.anestheticPlans.length ? this.getAnestheticPlansData(anestheticReport.anestheticPlans) : null,
        }
    }

    private getDiagnosisAsStringArray(diagnosis: DiagnosisDto[]): string[] {
        return diagnosis.map(diag => {
            return this.getDescriptionAndStatus(diag);
        })
    }

    private getDescriptionAndStatus(diagnosis: DiagnosisDto): string {
        if (diagnosis.verificationId === CONFIRMED){
            return diagnosis.snomed.pt + ' ' + this.confirmedStatus
        }
        if (diagnosis.verificationId === PRESUMPTIVE){
            return diagnosis.snomed.pt + ' ' + this.presumptiveStatus
        }
        return diagnosis.snomed.pt
    }

    private getProposedSurgeriesAsStringArray(proposedSurgeries: HospitalizationProcedureDto[]): string[] {
        return proposedSurgeries.map(proposedSurgery => {
            return proposedSurgery.snomed.pt
        })
    }

    private getAnthropometricDataAsStrings(antropometricData: AnthropometricDataDto): AnthropometricData {
        return {
            bloodType: antropometricData.bloodType ? [antropometricData.bloodType.value] : null,
            height: antropometricData.height ? [antropometricData.height.value] : null,
            weight: antropometricData.weight ? [antropometricData.weight.value + 'Kg'] : null,
        }
    }

    private getAnesthesicClinicalEvaluationAsStrings(anesthesicClinicalEvaluation: RiskFactorDto): AnesthesicClinicalEvaluationData {
        return {
            maxBloodPressure: anesthesicClinicalEvaluation.systolicBloodPressure ? [anesthesicClinicalEvaluation.systolicBloodPressure.value] : null,
            minBloodPressure: anesthesicClinicalEvaluation.diastolicBloodPressure ? [anesthesicClinicalEvaluation.diastolicBloodPressure.value] : null,
            hematocrit: anesthesicClinicalEvaluation.hematocrit ? [anesthesicClinicalEvaluation.hematocrit.value + ' %'] : null,
        }
    }

    private getAnesthesiaHistoryAsStrings(anesthesiaHistory: AnestheticHistoryDto): string[] {
        return anesthesiaHistory?.stateId 
            ? ( anesthesiaHistory.zoneId 
                ? [this.getAnesthesiaStateDescription(anesthesiaHistory.stateId, anesthesiaHistory.zoneId)] 
                : [this.getAnesthesiaStateDescription(anesthesiaHistory.stateId)]) 
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

    private getMedicationsAsStringArray(medications: MedicationDto[]): string[] {
        return medications.map(medication => {
            return medication.note ? medication.snomed.pt + ' - ' + medication.note : medication.snomed.pt;
        })
    }

    private getPremedicationData(premedications: AnestheticSubstanceDto[]): PremedicationData[] {
        return premedications.map(premedication => {
            return {
                premedicationDescription: premedication.snomed.pt + ' - Via ' + this.getViaDescription(premedication.viaId) + ' - Dosis: ' + premedication.dosage.quantity.value + ' - Unidad: ' + premedication.dosage.quantity.unit,
                startDateTime: dateTimeDtoToDate(premedication.dosage.startDateTime)
            }
        })
    }

    private getViaDescription(viaId: number): string {
        return this.viasArray.filter(via => via.id == viaId)[0].description;
    }

    private hasHistories(anestheticReport: AnestheticReportDto): boolean {
        return (!!anestheticReport.histories.length || !!anestheticReport.procedureDescription.note || !!anestheticReport.procedureDescription.asa)
    }

    private getHistoriesAsPersonalHistoriesData(anestheticReport: AnestheticReportDto): PersonalHistoriesData {
        return {
            recordList: anestheticReport.histories?.map(history => { return capitalize(history.snomed.pt)}),
            observations: anestheticReport.procedureDescription.note ? [anestheticReport.procedureDescription.note] : null,
            asa: anestheticReport.procedureDescription.asa ? [anestheticReport.procedureDescription.asa.toString()] : null,
        }
    }

    private getAnestheticPlansData(anestheticPlans: AnestheticSubstanceDto[]): AnestheticPlanData[] {
        return anestheticPlans.map(anestheticPlan => {
            return {
                anestheticPlanDescription: anestheticPlan.snomed.pt + ' - Via ' + this.getAnestheticPlanViaDescription(anestheticPlan.viaId) + ' - Dosis: ' + anestheticPlan.dosage.quantity.value + ' - Unidad: ' + anestheticPlan.dosage.quantity.unit,
                startDateTime: dateTimeDtoToDate(anestheticPlan.dosage.startDateTime)
            }
        })
    }
    
    private getAnestheticPlanViaDescription(viaId: number): string {
        return this.anestheticPlanViasArray.filter(via => via.id == viaId)[0].description;
    }
}

export interface AnestheticReportViewFormat {
    mainDiagnosis: string[],
    diagnosis: string[],
    proposedSurgeries: string[],
    anthropometricData: AnthropometricData,
    anesthesicClinicalEvaluation: AnesthesicClinicalEvaluationData,
    anestheticHistory: string[],
    usualMedication: string[],
    premedicationList: PremedicationData[],
    lastFoodIntake: Date,
    histories: PersonalHistoriesData,
    anestheticPlanList: AnestheticPlanData[],
}

export interface AnthropometricData {
    bloodType: string[],
    height: string[],
    weight: string[],
}

export interface AnesthesicClinicalEvaluationData {
    maxBloodPressure: string[],
    minBloodPressure: string[],
    hematocrit: string[],
}

export interface PremedicationData {
    premedicationDescription: string,
    startDateTime: Date,
}

export interface PersonalHistoriesData {
    recordList: string[],
    observations: string[],
    asa: string[]
}

export interface AnestheticPlanData {
    anestheticPlanDescription: string,
    startDateTime: Date,
}