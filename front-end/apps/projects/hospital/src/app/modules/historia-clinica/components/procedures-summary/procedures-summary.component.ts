import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-procedures-summary',
    templateUrl: './procedures-summary.component.html',
    styleUrls: ['./procedures-summary.component.scss']
})
export class ProceduresSummaryComponent {

    @Input() procedures: DescriptionItemData[];
    constructor() { }
}
