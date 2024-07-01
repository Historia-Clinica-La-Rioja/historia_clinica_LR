import { Injectable } from '@angular/core';
import { EmergencyCareEvolutionNoteDocumentDto } from '@api-rest/api-model';
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
        }
    }
}

export interface EvolutionNoteAsViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
}
