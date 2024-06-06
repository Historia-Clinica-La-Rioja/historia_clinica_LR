import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportDto } from '@api-rest/api-model';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { AnestheticReportDocumentSummaryService, AnestheticReportViewFormat } from '@historia-clinica/services/anesthetic-report-document-summary.service';
import { Observable, tap } from 'rxjs';

@Component({
    selector: 'app-anesthetic-report-document-summary',
    templateUrl: './anesthetic-report-document-summary.component.html',
    styleUrls: ['./anesthetic-report-document-summary.component.scss']
})
export class AnestheticReportDocumentSummaryComponent implements OnInit {
    @Input() set documentId (documentId: number) {
        this._documentId = documentId;
        this.anestheticReport = null;
        if (this.internmentEpisodeId && this._documentId) {
            this.anestheticReport$ = this.anestheticReportService.getAnestheticReport(this._documentId, this.internmentEpisodeId).pipe(tap(anestheticReport => {
                this.anestheticReport = this.anestheticReportDocumentSummaryService.getAnestheticReportAsViewFormat(anestheticReport);
                this.isLoading = false;
            } ))
        }
    };
    @Input() internmentEpisodeId: number;
    anestheticReport: AnestheticReportViewFormat;
    _documentId: number
    anestheticReport$: Observable<AnestheticReportDto>;
    isLoading = true;

    constructor( 
        private readonly anestheticReportService: AnesthethicReportService,
        private readonly anestheticReportDocumentSummaryService: AnestheticReportDocumentSummaryService,
     ) { }

    ngOnInit(): void {
        this.anestheticReport$ = this.anestheticReportService.getAnestheticReport(this._documentId, this.internmentEpisodeId).pipe(tap(anestheticReport => {
            this.anestheticReport = this.anestheticReportDocumentSummaryService.getAnestheticReportAsViewFormat(anestheticReport);
            this.isLoading = false;
        } ))
    }
}