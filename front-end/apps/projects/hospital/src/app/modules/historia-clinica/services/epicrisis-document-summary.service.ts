import { Injectable } from '@angular/core';
import { ResponseEpicrisisDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { ClinicalEvaluationData } from '@historia-clinica/utils/document-summary.model';

@Injectable({
    providedIn: 'root'
})
export class EpicrisisDocumentSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapEpicrisisAsViewFormat(epicrisis: ResponseEpicrisisDto): EpicrisisViewFormat {
        return {
            ...(epicrisis.mainDiagnosis && { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(epicrisis.mainDiagnosis) }] }),
            ...(epicrisis.diagnosis.length && { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(epicrisis.diagnosis)} ),
            ...(this.documentsSummaryService.hasClinicalEvaluations(epicrisis.notes) && { clinicalEvaluation: this.documentsSummaryService.mapClinicalEvaluationToDescriptionItemData(epicrisis.notes)} ),
        }
}
}

export interface EpicrisisViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    clinicalEvaluation: ClinicalEvaluationData,
}