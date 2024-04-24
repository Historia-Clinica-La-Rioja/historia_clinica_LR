import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-anesthetic-history-summary',
    templateUrl: './anesthetic-history-summary.component.html',
    styleUrls: ['./anesthetic-history-summary.component.scss']
})
export class AnestheticHistorySummaryComponent {

    @Input() anestheticHistory: DescriptionItemData[];

    constructor() { }
}
