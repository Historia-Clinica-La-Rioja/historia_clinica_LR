import { Component, Input } from '@angular/core';
import { ReferredDescriptionItemData } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-referred-item-summary',
    templateUrl: './referred-item-summary.component.html',
    styleUrls: ['./referred-item-summary.component.scss']
})
export class ReferredItemSummaryComponent {

    @Input() referredItem: ReferredDescriptionItemData;
    constructor() { }
}
