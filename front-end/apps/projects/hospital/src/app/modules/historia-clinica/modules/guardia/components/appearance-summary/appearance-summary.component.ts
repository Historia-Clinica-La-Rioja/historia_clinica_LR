import { Component, Input } from '@angular/core';
import { TitleDescriptionListItem } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-appearance-summary',
    templateUrl: './appearance-summary.component.html',
    styleUrls: ['./appearance-summary.component.scss']
})
export class AppearanceSummaryComponent {

    @Input() appearance: TitleDescriptionListItem;
    constructor() { }
}
