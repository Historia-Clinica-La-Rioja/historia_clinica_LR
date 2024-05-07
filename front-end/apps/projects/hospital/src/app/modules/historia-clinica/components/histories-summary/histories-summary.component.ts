import { Component, Input } from '@angular/core';
import { PersonalHistoriesData } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-histories-summary',
    templateUrl: './histories-summary.component.html',
    styleUrls: ['./histories-summary.component.scss']
})
export class HistoriesSummaryComponent {

    @Input() histories: PersonalHistoriesData;

    constructor() { }
}
