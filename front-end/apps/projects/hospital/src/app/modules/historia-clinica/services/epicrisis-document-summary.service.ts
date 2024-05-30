import { Injectable } from '@angular/core';
import { ResponseEpicrisisDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';

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
        }
}
}

export interface EpicrisisViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
}