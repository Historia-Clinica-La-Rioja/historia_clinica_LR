import { Injectable } from '@angular/core';
import { ResponseEvolutionNoteDto } from '@api-rest/api-model';

@Injectable({
    providedIn: 'root'
})
export class EvolutionNoteDocumentSummaryService {

    constructor() { }

    mapEvolutionNoteAsViewFormat(evolutionNote: ResponseEvolutionNoteDto): EvolutionNoteAsViewFormat {
        return {
            
        }
    }
}

export interface EvolutionNoteAsViewFormat {
    
}