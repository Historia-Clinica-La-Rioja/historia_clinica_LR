import { Component, Input } from '@angular/core';
import { AnthropometricData } from '@historia-clinica/services/anesthetic-report-document-summary.service';

@Component({
    selector: 'app-anthropometric-data-summary',
    templateUrl: './anthropometric-data-summary.component.html',
    styleUrls: ['./anthropometric-data-summary.component.scss']
})
export class AnthropometricDataSummaryComponent {

    @Input() anthropometricData: AnthropometricData

    constructor() { }
}
