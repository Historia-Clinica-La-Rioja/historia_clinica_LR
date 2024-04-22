import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-antibiotic-prophylaxis-summary',
    templateUrl: './antibiotic-prophylaxis-summary.component.html',
    styleUrls: ['./antibiotic-prophylaxis-summary.component.scss']
})
export class AntibioticProphylaxisSummaryComponent {

    @Input() antibioticProphylaxis: DescriptionItemData[];

    constructor() { }
}
