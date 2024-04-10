import { Injectable } from '@angular/core';
import { AnestheticHistoryDto, AnestheticReportDto, AnthropometricDataDto, DiagnosisDto, HospitalizationProcedureDto, RiskFactorDto } from '@api-rest/api-model';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { ANESTHESIA_ZONE_ID, PREVIOUS_ANESTHESIA_STATE_ID } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { TranslateService } from '@ngx-translate/core';

const CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
const PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportDocumentSummaryService {

    private confirmedStatus: string = '';
    private presumptiveStatus: string = '';

    constructor(
		private readonly translateService: TranslateService 
    ) { 
        this.confirmedStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.CONFIRMED')
        this.presumptiveStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.PRESUMPTIVE')
    }

    getAnestheticReportAsViewFormat(anestheticReport: AnestheticReportDto): AnestheticReportViewFormat {
        return {
            mainDiagnosis: anestheticReport.mainDiagnosis ? [this.getDescriptionAndStatus(anestheticReport.mainDiagnosis)] : null,
            diagnosis: anestheticReport.diagnosis.length ? this.getDiagnosisAsStringArray(anestheticReport.diagnosis) : null,
            proposedSurgeries: anestheticReport.surgeryProcedures.length ? this.getProposedSurgeriesAsStringArray(anestheticReport.surgeryProcedures) : null,
            anthropometricData: anestheticReport.anthropometricData ? this.getAnthropometricDataAsStrings(anestheticReport.anthropometricData) : null,
            anesthesicClinicalEvaluation: anestheticReport.riskFactors ? this.getAnesthesicClinicalEvaluationAsStrings(anestheticReport.riskFactors) : null,
            anestheticHistory: this.getAnesthesiaHistoryAsStrings(anestheticReport.anestheticHistory),
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
}

export interface AnestheticReportViewFormat {
    mainDiagnosis: string[],
    diagnosis: string[],
    proposedSurgeries: string[],
    anthropometricData: AnthropometricData,
    anesthesicClinicalEvaluation: AnesthesicClinicalEvaluationData,
    anestheticHistory: string[],
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