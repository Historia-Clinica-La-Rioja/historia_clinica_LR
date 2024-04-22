import { Component, Input } from '@angular/core';
import { IntrasurgicalAnestheticProceduresData } from '@historia-clinica/services/anesthetic-report-document-summary.service';

@Component({
    selector: 'app-intrasurgical-anesthetic-procedures-summary',
    templateUrl: './intrasurgical-anesthetic-procedures-summary.component.html',
    styleUrls: ['./intrasurgical-anesthetic-procedures-summary.component.scss']
})
export class IntrasurgicalAnestheticProceduresSummaryComponent {

    @Input() procedures: IntrasurgicalAnestheticProceduresData;

    constructor() { }
}
