import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-anesthetic-technique-summary',
    templateUrl: './anesthetic-technique-summary.component.html',
    styleUrls: ['./anesthetic-technique-summary.component.scss']
})
export class AnestheticTechniqueSummaryComponent {

    @Input() anestheticTechnique: DescriptionItemData[];

    constructor() { }

}
