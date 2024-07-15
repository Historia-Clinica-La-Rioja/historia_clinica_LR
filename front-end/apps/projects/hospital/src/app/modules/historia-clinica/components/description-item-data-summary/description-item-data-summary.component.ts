import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-description-item-data-summary',
    templateUrl: './description-item-data-summary.component.html',
    styleUrls: ['./description-item-data-summary.component.scss']
})
export class DescriptionItemDataSummaryComponent {

    @Input() descriptionItemDataSummary: DescriptionItemDataSummary;
    constructor() { }
}

export interface DescriptionItemDataSummary {
    summary: DescriptionItemData[],
    icon: string,
    title: string,
    subtitle: string,
}
