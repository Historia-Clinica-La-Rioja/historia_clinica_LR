import { Component, Input } from '@angular/core';
import { DescriptionItemDataInfo } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-external-cause-summary',
    templateUrl: './external-cause-summary.component.html',
    styleUrls: ['./external-cause-summary.component.scss']
})
export class ExternalCauseSummaryComponent {

    @Input() externalCause: DescriptionItemDataInfo[];
    constructor() { }
}
