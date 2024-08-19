import { Component, Input } from '@angular/core';
import { ClinicalEvaluationData } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-clinical-evaluation-summary',
    templateUrl: './clinical-evaluation-summary.component.html',
    styleUrls: ['./clinical-evaluation-summary.component.scss']
})
export class ClinicalEvaluationSummaryComponent {

    @Input() clinicalEvaluation: ClinicalEvaluationData;
    constructor() { }
}
