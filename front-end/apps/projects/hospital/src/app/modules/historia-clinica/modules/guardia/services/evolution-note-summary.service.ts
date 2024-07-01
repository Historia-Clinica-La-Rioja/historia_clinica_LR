import { Injectable } from '@angular/core';
import { EmergencyCareEvolutionNoteDocumentDto } from '@api-rest/api-model';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { AnthropometricData, ReferredDescriptionItemData } from '@historia-clinica/utils/document-summary.model';
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
            ...(evolutionNote.clinicalSpecialtyName && 
                { specialty: [this.documentsSummaryService.toDescriptionItemData(evolutionNote.clinicalSpecialtyName)]} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.mainDiagnosis && 
                { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(evolutionNote.emergencyCareEvolutionNoteClinicalData.mainDiagnosis) }] }),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.diagnosis.length && 
                { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(evolutionNote.emergencyCareEvolutionNoteClinicalData.diagnosis)} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.reasons.length && 
                { reasons: this.documentsSummaryService.mapReasonsToDescriptionItemDataSummary(evolutionNote.emergencyCareEvolutionNoteClinicalData.reasons)} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.evolutionNote && 
                { evolution: [this.documentsSummaryService.toDescriptionItemData(evolutionNote.emergencyCareEvolutionNoteClinicalData.evolutionNote)]} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.anthropometricData && 
                { anthropometricData: this.documentsSummaryService.mapToAnthropometricData(evolutionNote.emergencyCareEvolutionNoteClinicalData.anthropometricData)} ),
            ...(this.documentsSummaryService.hasReferredItemContent(evolutionNote.emergencyCareEvolutionNoteClinicalData.familyHistories) && 
                { familyHistories: this.documentsSummaryService.mapFamilyHistoriesToReferredDescriptionItemDataSummary(evolutionNote.emergencyCareEvolutionNoteClinicalData.familyHistories)} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.medications.length && 
                { medications: this.documentsSummaryService.mapMedicationsToDescriptionItemDataSummary(evolutionNote.emergencyCareEvolutionNoteClinicalData.medications)} ),
        }
    }
}

export interface EvolutionNoteAsViewFormat {
    specialty: DescriptionItemData[],
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    reasons: DescriptionItemDataSummary,
    evolution: DescriptionItemData[],
    anthropometricData: AnthropometricData,
    familyHistories: ReferredDescriptionItemData,
    medications: DescriptionItemDataSummary,
}
