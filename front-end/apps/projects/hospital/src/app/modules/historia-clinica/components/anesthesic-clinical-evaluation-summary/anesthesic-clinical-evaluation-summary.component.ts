import { Component, Input } from '@angular/core';
import { AnesthesicClinicalEvaluationData } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-anesthesic-clinical-evaluation-summary',
    templateUrl: './anesthesic-clinical-evaluation-summary.component.html',
    styleUrls: ['./anesthesic-clinical-evaluation-summary.component.scss']
})
export class AnesthesicClinicalEvaluationSummaryComponent {

    @Input() anesthesicClinicalEvaluation: AnesthesicClinicalEvaluationData;

    constructor() { }
}
