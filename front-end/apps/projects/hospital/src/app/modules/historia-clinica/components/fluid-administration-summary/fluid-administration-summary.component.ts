import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-fluid-administration-summary',
    templateUrl: './fluid-administration-summary.component.html',
    styleUrls: ['./fluid-administration-summary.component.scss']
})
export class FluidAdministrationSummaryComponent {
    @Input() fluidAdministration: DescriptionItemData[];

    constructor() { }
}
