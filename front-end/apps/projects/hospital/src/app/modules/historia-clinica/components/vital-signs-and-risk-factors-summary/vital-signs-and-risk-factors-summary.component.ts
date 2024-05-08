import { Component, Input } from '@angular/core';
import { VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-vital-signs-and-risk-factors-summary',
    templateUrl: './vital-signs-and-risk-factors-summary.component.html',
    styleUrls: ['./vital-signs-and-risk-factors-summary.component.scss']
})
export class VitalSignsAndRiskFactorsSummaryComponent {

    @Input() vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData;
    constructor() { }
}
