import { Injectable } from '@angular/core';
import { EmergencyCareEvolutionNoteDocumentDto } from '@api-rest/api-model';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { AnthropometricData, ReferredDescriptionItemData, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';
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
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.procedures.length && 
                { procedures: this.documentsSummaryService.mapProceduresToDescriptionItemDataSummary(evolutionNote.emergencyCareEvolutionNoteClinicalData.procedures)} ),
            ...(evolutionNote.emergencyCareEvolutionNoteClinicalData.riskFactors && 
                { vitalSignsAndRiskFactors: this.documentsSummaryService.mapToVitalSignsAndRiskFactors(evolutionNote.emergencyCareEvolutionNoteClinicalData.riskFactors)} ),
            ...(this.documentsSummaryService.hasReferredItemContent(evolutionNote.emergencyCareEvolutionNoteClinicalData.allergies) && 
                { allergies: this.documentsSummaryService.mapAllergiesToReferredDescriptionItemDataSummary(evolutionNote.emergencyCareEvolutionNoteClinicalData.allergies)} ),
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
    procedures: DescriptionItemDataSummary,
    vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData,
    allergies: ReferredDescriptionItemData,
}
