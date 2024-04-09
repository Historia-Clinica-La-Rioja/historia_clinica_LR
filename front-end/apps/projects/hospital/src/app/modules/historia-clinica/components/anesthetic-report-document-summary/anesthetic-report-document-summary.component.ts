import { Component, Input, OnInit } from '@angular/core';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { AnestheticReportDocumentSummaryService, AnestheticReportViewFormat } from '@historia-clinica/services/anesthetic-report-document-summary.service';

@Component({
    selector: 'app-anesthetic-report-document-summary',
    templateUrl: './anesthetic-report-document-summary.component.html',
    styleUrls: ['./anesthetic-report-document-summary.component.scss']
})
export class AnestheticReportDocumentSummaryComponent implements OnInit {
    @Input() set documentId (documentId: number) {
        this._documentId = documentId;
        if (this.internmentEpisodeId && this._documentId) {
            this.anestheticReportService.getAnestheticReport(this._documentId, this.internmentEpisodeId).subscribe(anestheticReport => {
                this.anestheticReport = this.anestheticReportDocumentSummaryService.getAnestheticReportAsViewFormat(anestheticReport);
            })
        }
    };
    @Input() internmentEpisodeId: number;
    anestheticReport: AnestheticReportViewFormat;
    _documentId: number

    constructor( 
        private readonly anestheticReportService: AnesthethicReportService,
        private readonly anestheticReportDocumentSummaryService: AnestheticReportDocumentSummaryService,
     ) { }

    ngOnInit(): void {
        this.anestheticReportService.getAnestheticReport(this._documentId, this.internmentEpisodeId).subscribe(anestheticReport => {
            this.anestheticReport = this.anestheticReportDocumentSummaryService.getAnestheticReportAsViewFormat(anestheticReport);
        })
    }
}