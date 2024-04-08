import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportDto } from '@api-rest/api-model';

@Component({
    selector: 'app-anesthetic-report-document-summary',
    templateUrl: './anesthetic-report-document-summary.component.html',
    styleUrls: ['./anesthetic-report-document-summary.component.scss']
})
export class AnestheticReportDocumentSummaryComponent implements OnInit {
    @Input() anestheticReport: AnestheticReportDto;

    constructor() { }

    ngOnInit(): void {
    }

}
