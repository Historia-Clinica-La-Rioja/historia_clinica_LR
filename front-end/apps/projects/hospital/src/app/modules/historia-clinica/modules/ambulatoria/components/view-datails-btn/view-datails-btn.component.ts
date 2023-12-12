import { Component, Input, Output } from '@angular/core';
import { HCEHealthConditionDto } from '@api-rest/api-model';
import { Subject } from 'rxjs';
import { HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';

@Component({
    selector: 'app-view-datails-btn',
    templateUrl: './view-datails-btn.component.html',
    styleUrls: ['./view-datails-btn.component.scss']
})
export class ViewDatailsBtnComponent {

    @Input() problem: HCEHealthConditionDto;
    @Output() setProblemOnHistoric = new Subject<HCEHealthConditionDto>();
    
    constructor(
		private readonly historicalProblemsFacadeService: HistoricalProblemsFacadeService,)
    { }

    filterByProblemOnProblemClick() {
        this.historicalProblemsFacadeService.sendHistoricalProblemsFilter({
            specialty: null,
            professional: null,
            problem: this.problem.snomed.sctid,
            consultationDate: null,
            referenceStateId: null,
        });
        this.setProblemOnHistoric.next(this.problem);
    }

}
