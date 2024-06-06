import { Component, Input } from '@angular/core';
import { EndOfAnesthesiaStatusData } from '@historia-clinica/services/anesthetic-report-document-summary.service';

@Component({
    selector: 'app-end-of-anesthesia-status-summary',
    templateUrl: './end-of-anesthesia-status-summary.component.html',
    styleUrls: ['./end-of-anesthesia-status-summary.component.scss']
})
export class EndOfAnesthesiaStatusSummaryComponent {

    @Input() endOfAnesthesiaStatus: EndOfAnesthesiaStatusData;

    constructor() { }
}
