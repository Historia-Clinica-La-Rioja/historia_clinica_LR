import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-usual-medication-summary',
    templateUrl: './usual-medication-summary.component.html',
    styleUrls: ['./usual-medication-summary.component.scss']
})
export class UsualMedicationSummaryComponent {

    @Input() usualMedication: DescriptionItemData[];

    constructor() { }
}
