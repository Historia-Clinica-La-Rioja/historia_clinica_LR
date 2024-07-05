import { Injectable } from '@angular/core';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { Item } from '../components/emergency-care-evolutions/emergency-care-evolutions.component';
import { TriageCategory } from '../components/triage-chip/triage-chip.component';
import { TriageCategoryDto } from '@api-rest/api-model';
import { VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';

@Injectable({
    providedIn: 'root'
})
export class TriageSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapTriageAsViewFormat(triage: Item): TriageAsViewFormat {
        return {
            ...(triage.content.category && 
                { triageLevel: this.mapTriageCategoryDtoToTriageCategory(triage.content.category)} ),
            ...(triage.content.reasons.length && 
                { reasons: this.documentsSummaryService.mapReasonsToDescriptionItemDataSummary(triage.content.reasons)} ),
            ...(triage.content.riskFactors && 
                { vitalSignsAndRiskFactors: this.documentsSummaryService.mapToVitalSignsAndRiskFactors(triage.content.riskFactors)} ),
            ...(triage.content.notes && 
                { observations: triage.content.notes} ),
        }
    }

    private mapTriageCategoryDtoToTriageCategory(triageCategory: TriageCategoryDto): TriageCategory {
        return {
            colorHex: triageCategory.color.code,
            ...triageCategory
        }
    }
}

export interface TriageAsViewFormat {
    triageLevel: TriageCategory,
    reasons: DescriptionItemDataSummary,
    vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData,
    observations: string,
}