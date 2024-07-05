import { Injectable } from '@angular/core';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { Item } from '../components/emergency-care-evolutions/emergency-care-evolutions.component';
/* import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service'; */

@Injectable({
    providedIn: 'root'
})
export class TriageSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapTriageAsViewFormat(triage: Item): TriageAsViewFormat {
        return {
            ...(triage.content.reasons.length && 
                { reasons: this.documentsSummaryService.mapReasonsToDescriptionItemDataSummary(triage.content.reasons)} ),
        }
    }
}

export interface TriageAsViewFormat {
    reasons: DescriptionItemDataSummary,
}