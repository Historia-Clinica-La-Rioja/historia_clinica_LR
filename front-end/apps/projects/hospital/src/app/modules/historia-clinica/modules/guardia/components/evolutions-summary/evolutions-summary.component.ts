import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-evolutions-summary',
    templateUrl: './evolutions-summary.component.html',
    styleUrls: ['./evolutions-summary.component.scss']
})
export class EvolutionsSummaryComponent {

    @Input() evolution: DescriptionItemData[];
    constructor() { }
}
