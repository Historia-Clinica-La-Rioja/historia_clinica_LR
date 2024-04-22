import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-non-anesthetic-drugs-summary',
    templateUrl: './non-anesthetic-drugs-summary.component.html',
    styleUrls: ['./non-anesthetic-drugs-summary.component.scss']
})
export class NonAnestheticDrugsSummaryComponent {

    @Input() nonAnestheticDrugs: DescriptionItemData[];

    constructor() { }
}
