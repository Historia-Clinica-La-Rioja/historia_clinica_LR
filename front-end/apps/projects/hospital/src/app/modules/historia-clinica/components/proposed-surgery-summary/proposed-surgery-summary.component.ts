import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-proposed-surgery-summary',
    templateUrl: './proposed-surgery-summary.component.html',
    styleUrls: ['./proposed-surgery-summary.component.scss']
})
export class ProposedSurgerySummaryComponent {

    @Input() proposedSurgeries: DescriptionItemData[];

    constructor() { }
}
