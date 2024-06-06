import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-anesthetic-plan-summary',
    templateUrl: './anesthetic-plan-summary.component.html',
    styleUrls: ['./anesthetic-plan-summary.component.scss']
})
export class AnestheticPlanSummaryComponent {

    @Input() anestheticPlanList: DescriptionItemData[];

    constructor() { }
}
