import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-premedication-and-food-intake-summary',
    templateUrl: './premedication-and-food-intake-summary.component.html',
    styleUrls: ['./premedication-and-food-intake-summary.component.scss']
})
export class PremedicationAndFoodIntakeSummaryComponent {

    @Input() premedicationList: DescriptionItemData[];
    @Input() lastFoodIntake: Date;

    constructor() { }
}
