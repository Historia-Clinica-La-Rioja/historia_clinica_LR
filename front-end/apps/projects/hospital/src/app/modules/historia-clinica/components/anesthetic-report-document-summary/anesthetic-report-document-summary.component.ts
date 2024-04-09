import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportDto } from '@api-rest/api-model';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';

@Component({
    selector: 'app-anesthetic-report-document-summary',
    templateUrl: './anesthetic-report-document-summary.component.html',
    styleUrls: ['./anesthetic-report-document-summary.component.scss']
})
export class AnestheticReportDocumentSummaryComponent implements OnInit {
    @Input() documentId: number;
    @Input() internmentEpisodeId: number;
    anestheticReport: AnestheticReportDto;

    constructor( private readonly anestheticReportService: AnesthethicReportService ) { }

    ngOnInit(): void {
        this.anestheticReportService.getAnestheticReport(this.documentId, this.internmentEpisodeId).subscribe(anestheticReport => {
            this.anestheticReport = anestheticReport;
        })
    }
}