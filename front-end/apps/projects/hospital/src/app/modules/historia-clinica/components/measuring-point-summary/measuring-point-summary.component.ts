import { Component, Input } from '@angular/core';
import { MeasuringPointData } from '@historia-clinica/services/anesthetic-report-document-summary.service';

@Component({
    selector: 'app-measuring-point-summary',
    templateUrl: './measuring-point-summary.component.html',
    styleUrls: ['./measuring-point-summary.component.scss']
})
export class MeasuringPointSummaryComponent {

    @Input() measuringPoint: MeasuringPointData;
    @Input() index: number;

    constructor() { }
}