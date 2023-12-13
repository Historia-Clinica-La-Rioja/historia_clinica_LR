import { Component, Input, OnInit } from '@angular/core';
import { HealthHistoryConditionDto } from '@api-rest/api-model';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';

@Component({
    selector: 'app-personal-histories-summary',
    templateUrl: './personal-histories-summary.component.html',
    styleUrls: ['./personal-histories-summary.component.scss']
})
export class PersonalHistoriesSummaryComponent implements OnInit {

    @Input() personalHistories: HealthHistoryConditionDto[];
    @Input() personalHistoriesHeader: SummaryHeader;

    constructor() { }

    ngOnInit(): void {
    }

}
