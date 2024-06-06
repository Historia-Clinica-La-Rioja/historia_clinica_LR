import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-diagnosis-summary',
    templateUrl: './diagnosis-summary.component.html',
    styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent {

    @Input() diagnosis: DescriptionItemData[];
    @Input() mainDiagnosis: DescriptionItemData[];

    constructor() { }
}
