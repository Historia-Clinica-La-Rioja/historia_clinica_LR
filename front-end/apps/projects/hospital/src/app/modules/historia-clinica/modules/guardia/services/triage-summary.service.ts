import { Injectable } from '@angular/core';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { Item } from '../components/emergency-care-evolutions/emergency-care-evolutions.component';
import { TriageCategory } from '../components/triage-chip/triage-chip.component';
import { TitleDescriptionListItem, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';

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
                { triageLevel: triage.content.category} ),
            ...(triage.content.reasons.length &&
                { reasons: this.documentsSummaryService.mapReasonsToDescriptionItemDataSummary(triage.content.reasons)} ),
			...(triage.content.clinicalSpecialtySector &&
				{ clinicalSpecialtySector: triage.content.clinicalSpecialtySector.description} ),
            ...(triage.content.riskFactors &&
                { vitalSignsAndRiskFactors: this.documentsSummaryService.mapToVitalSignsAndRiskFactors(triage.content.riskFactors)} ),
            ...(triage.content.notes &&
                { observations: triage.content.notes} ),
            ...(triage.content.appearance &&
                { appearance: this.documentsSummaryService.mapAppearencesToTitleDescriptionListItem(triage.content.appearance)} ),
            ...(triage.content.breathing &&
                { breathing: this.documentsSummaryService.mapBreathingToTitleDescriptionListItem(triage.content.breathing)} ),
            ...(triage.content.circulation &&
                { circulation: this.documentsSummaryService.mapCirculationToTitleDescriptionListItem(triage.content.circulation)} )
        }
    }
}

export interface TriageAsViewFormat {
    triageLevel: TriageCategory,
    reasons: DescriptionItemDataSummary,
	clinicalSpecialtySector: string,
    vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData,
    observations: string,
    appearance: TitleDescriptionListItem,
    breathing: TitleDescriptionListItem,
    circulation: TitleDescriptionListItem,
}
