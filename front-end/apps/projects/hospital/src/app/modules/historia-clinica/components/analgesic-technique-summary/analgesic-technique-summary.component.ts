import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-analgesic-technique-summary',
    templateUrl: './analgesic-technique-summary.component.html',
    styleUrls: ['./analgesic-technique-summary.component.scss']
})
export class AnalgesicTechniqueSummaryComponent {

    @Input() analgesicTechnique: DescriptionItemData[];

    constructor() { }
}
