import { Component, Input } from '@angular/core';
import { TitleDescriptionListItem } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-titled-grid-summary',
    templateUrl: './titled-grid-summary.component.html',
    styleUrls: ['./titled-grid-summary.component.scss']
})
export class TitledGridSummaryComponent {

    @Input() itemsList: TitleDescriptionListItem;
    constructor() { }
}