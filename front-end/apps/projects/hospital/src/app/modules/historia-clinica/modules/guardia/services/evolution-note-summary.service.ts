import { Injectable } from '@angular/core';
import { EmergencyCareEvolutionNoteDocumentDto } from '@api-rest/api-model';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Injectable({
    providedIn: 'root'
})
export class EvolutionNoteSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapEvolutionNoteAsViewFormat(evolutionNote: EmergencyCareEvolutionNoteDocumentDto): EvolutionNoteAsViewFormat {
        return {
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.mainDiagnosis && 
                { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(evolutionNote.emergencyCareEvolutionNoteClinicalData.mainDiagnosis) }] }),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.diagnosis.length && 
                { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(evolutionNote.emergencyCareEvolutionNoteClinicalData.diagnosis)} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.reasons.length && 
                { reasons: this.documentsSummaryService.mapReasonsToDescriptionItemDataSummary(evolutionNote.emergencyCareEvolutionNoteClinicalData.reasons)} ),
        }
    }
}

export interface EvolutionNoteAsViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    reasons: DescriptionItemDataSummary,
}
