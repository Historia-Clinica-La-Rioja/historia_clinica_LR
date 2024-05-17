import { Injectable } from '@angular/core';
import { ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';

@Injectable({
    providedIn: 'root'
})
export class EvolutionNoteDocumentSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapEvolutionNoteAsViewFormat(evolutionNote: ResponseEvolutionNoteDto): EvolutionNoteAsViewFormat {
        return {
            ...(evolutionNote.mainDiagnosis && { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(evolutionNote.mainDiagnosis) }] }),
            ...(evolutionNote.diagnosis.length && { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(evolutionNote.diagnosis)} ),
        }
    }
}

export interface EvolutionNoteAsViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
}