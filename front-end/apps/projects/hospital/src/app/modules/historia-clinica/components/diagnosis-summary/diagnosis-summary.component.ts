import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
    selector: 'app-diagnosis-evolution-summary',
    templateUrl: './diagnosis-summary.component.html',
    styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent {

	@Input() customTitle?: string;
    @Input() diagnosis: DescriptionItemData[];
    @Input() mainDiagnosis: DescriptionItemData[];

	readonly DEFAULT_TITLE: string = 'internaciones.anesthesic-report.diagnosis.TITLE';

    constructor() { }
}
