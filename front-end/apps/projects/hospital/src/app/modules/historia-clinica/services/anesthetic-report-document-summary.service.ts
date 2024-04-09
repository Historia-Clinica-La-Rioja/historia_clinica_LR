import { Injectable } from '@angular/core';
import { AnestheticReportDto, DiagnosisDto } from '@api-rest/api-model';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportDocumentSummaryService {

    private confirmedStatus: string = '';

    constructor(
		private readonly translateService: TranslateService 
    ) { 
        this.confirmedStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.CONFIRMED')
    }

    getAnestheticReportAsViewFormat(anestheticReport: AnestheticReportDto): AnestheticReportViewFormat {
        return {
            mainDiagnosis: [this.getConfirmedStatus(anestheticReport.mainDiagnosis)],
            diagnosis: this.getDiagnosisAsStringArray(anestheticReport.diagnosis)
        }
    }

    private getDiagnosisAsStringArray(diagnosis: DiagnosisDto[]): string[] {
        return diagnosis.map(diag => {
            return this.getConfirmedStatus(diag);
        })
    }

    private getConfirmedStatus(diagnosis: DiagnosisDto): string {
        return diagnosis.verificationId ? diagnosis.snomed.pt + ' ' + this.confirmedStatus : diagnosis.snomed.pt
    }
}

export interface AnestheticReportViewFormat {
    mainDiagnosis: string[],
    diagnosis: string[],
}