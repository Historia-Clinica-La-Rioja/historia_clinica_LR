import { Component, Input } from '@angular/core';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { Observable, map } from 'rxjs';
import { TriageAsViewFormat, TriageSummaryService } from '../../services/triage-summary.service';
import { Item } from '../emergency-care-evolutions/emergency-care-evolutions.component';
import { DocumentService } from '@api-rest/services/document.service';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-triage-summary',
    templateUrl: './triage-summary.component.html',
    styleUrls: ['./triage-summary.component.scss']
  })
  export class TriageSummaryComponent  { 

	@Input() set content(triage: Item) {
        this._triage = triage;
        this.fetchSummaryInfo();
    }
	@Input() set episodeId(episodeId: number) {
		this._episodeId = episodeId;
        this.fetchSummaryInfo();
	};
    private _episodeId: number;
    triageSummary: TriageAsViewFormat;
    _triage: Item;
    private readonly documentName = this.translateService.instant('guardia.documents-summary.document-name.TRIAGE');
    documentSummary$: Observable<HeaderDescription>;
    isPopUpOpened = false;

	constructor(
        private readonly triageSummaryService: TriageSummaryService,
        private readonly documentService: DocumentService,
        private readonly translateService: TranslateService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
    ) { }

    private fetchSummaryInfo(){
        if (this._triage && this._episodeId) {
            this.triageSummary = this.triageSummaryService.mapTriageAsViewFormat(this._triage);
            let header$ = this.documentSummaryService.getEmergencyCareDocumentHeader(this._triage.summary.docId, this._episodeId);
            this.documentSummary$ = header$.pipe(map((headerData) => {
                return this.documentSummaryMapperService.mapEmergencyCareToHeaderDescription(headerData, this.documentName, false, false, true);
            }));
        }
    }  
  
    downloadDocument() {
		this.documentService.downloadFile({ filename: this._triage.summary.docFileName, id: this._triage.summary.docId });
	}
}