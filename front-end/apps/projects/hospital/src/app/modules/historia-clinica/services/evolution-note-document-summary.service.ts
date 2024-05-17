import { Injectable } from '@angular/core';
import { ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { AnthropometricData, ClinicalEvaluationData, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';

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
            ...(this.documentsSummaryService.hasClinicalEvaluations(evolutionNote.notes) && { clinicalEvaluation: this.documentsSummaryService.mapClinicalEvaluationToDescriptionItemData(evolutionNote.notes)} ),
            ...(evolutionNote.procedures.length && { procedures: this.documentsSummaryService.mapProceduresToDescriptionItemDataSummary(evolutionNote.procedures)} ),
            ...(evolutionNote.anthropometricData && { anthropometricData: this.documentsSummaryService.mapToAnthropometricData(evolutionNote.anthropometricData)} ),
            ...(evolutionNote.riskFactors && { vitalSignsAndRiskFactors: this.documentsSummaryService.mapToVitalSignsAndRiskFactors(evolutionNote.riskFactors)} ),
            ...(evolutionNote.allergies.content?.length && { allergies: this.documentsSummaryService.mapAllergiesToDescriptionItemDataSummary(evolutionNote.allergies.content)} ),
        }
    }
}

export interface EvolutionNoteAsViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    clinicalEvaluation: ClinicalEvaluationData,
    procedures: DescriptionItemDataSummary,
    anthropometricData: AnthropometricData,
    vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData,
    allergies: DescriptionItemDataSummary,
}