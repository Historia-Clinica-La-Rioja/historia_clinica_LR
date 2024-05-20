import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { AnestheticReportDocumentSummaryService, AnestheticReportViewFormat } from '@historia-clinica/services/anesthetic-report-document-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { Observable, forkJoin, map } from 'rxjs';

@Component({
    selector: 'app-anesthetic-report-document-summary',
    templateUrl: './anesthetic-report-document-summary.component.html',
    styleUrls: ['./anesthetic-report-document-summary.component.scss']
})
export class AnestheticReportDocumentSummaryComponent implements OnInit {
    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument (activeDocument: DocumentSearch) {
        this._activeDocument = activeDocument;
        this.fetchSummaryInfo();
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    _activeDocument: DocumentSearch;
    documentSummary$: Observable<{headerDescription: HeaderDescription, anestheticReport: AnestheticReportViewFormat}>;

    constructor( 
        private readonly anestheticReportService: AnesthethicReportService,
        private readonly anestheticReportDocumentSummaryService: AnestheticReportDocumentSummaryService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
     ) { }

    ngOnInit(): void {
        this.fetchSummaryInfo();
    }

    private fetchSummaryInfo(){
        if (this._activeDocument?.document?.id) {
            let anestheticReport$ = this.anestheticReportService.getAnestheticReport(this._activeDocument.document.id, this.internmentEpisodeId);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.documentSummary$ = forkJoin([header$, anestheticReport$]).pipe(map(([headerData, anestheticReportData]) => {
                return {
                    headerDescription: this.documentSummaryMapperService.mapToHeaderDescription(headerData, 'Parte anestésico', this._activeDocument),
                    anestheticReport: this.anestheticReportDocumentSummaryService.mapToAnestheticReportViewFormat(anestheticReportData),
            }}));
        }
    }
}