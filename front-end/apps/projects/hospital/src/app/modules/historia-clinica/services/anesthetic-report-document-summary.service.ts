import { Injectable } from '@angular/core';
import { AnestheticReportDto, AnthropometricDataDto, DiagnosisDto, HospitalizationProcedureDto } from '@api-rest/api-model';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
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

    private getAnthropometricDataAsStrings(data: AnthropometricDataDto): AnthropometricData {
        return {
            bloodType: data.bloodType ? [data.bloodType.value] : null,
            height: data.height ? [data.height.value] : null,
            weight: data.weight ? [data.weight.value + 'Kg'] : null,
        }
    }
}

export interface AnestheticReportViewFormat {
    mainDiagnosis: string[],
    diagnosis: string[],
    proposedSurgeries: string[],
    anthropometricData: AnthropometricData,
}

interface AnthropometricData {
    bloodType: string[],
    height: string[],
    weight: string[],
}