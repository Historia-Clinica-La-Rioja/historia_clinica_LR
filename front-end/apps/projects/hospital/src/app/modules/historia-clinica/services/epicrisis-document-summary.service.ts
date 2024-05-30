import { Injectable } from '@angular/core';
import { ResponseEpicrisisDto } from '@api-rest/api-model';

@Injectable({
    providedIn: 'root'
})
export class EpicrisisDocumentSummaryService {

    constructor(
        
    ) { }

    mapEpicrisisAsViewFormat(epicrisis: ResponseEpicrisisDto): EpicrisisViewFormat {
        return {
            
        }
}
}

export interface EpicrisisViewFormat {

}