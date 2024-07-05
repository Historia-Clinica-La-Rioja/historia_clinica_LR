import { Injectable } from '@angular/core';
import { TriageDocumentDto } from '@api-rest/api-model';
/* import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service'; */

@Injectable({
    providedIn: 'root'
})
export class TriageSummaryService {

    constructor(
        /* private readonly documentsSummaryService: DocumentsSummaryMapperService, */
    ) { }

    mapTriageAsViewFormat(triage: TriageDocumentDto): TriageAsViewFormat {
        return {
        }
    }
}

export interface TriageAsViewFormat {

}