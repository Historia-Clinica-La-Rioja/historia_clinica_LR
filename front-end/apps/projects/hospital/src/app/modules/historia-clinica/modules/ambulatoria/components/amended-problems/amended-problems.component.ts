import { Component, OnInit } from '@angular/core';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { PROBLEMAS_POR_ERROR } from '@historia-clinica/constants/summaries';
import { Observable } from 'rxjs/internal/Observable';

@Component({
    selector: 'app-amended-problems',
    templateUrl: './amended-problems.component.html',
    styleUrls: ['./amended-problems.component.scss']
})
export class AmendedProblemsComponent implements OnInit {
    
    public amendedProblems$: Observable<HCEPersonalHistoryDto[]>;
	public readonly problemasPorError = PROBLEMAS_POR_ERROR;
    isFilterExpanded = false;

    constructor(
    ) { }

    ngOnInit(): void {
    }

    toggleFilter() {
        this.isFilterExpanded = !this.isFilterExpanded;
    }
}
