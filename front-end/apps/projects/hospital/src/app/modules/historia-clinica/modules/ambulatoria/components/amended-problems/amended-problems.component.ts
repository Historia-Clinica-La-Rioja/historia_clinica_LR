import { Component, Injector, OnInit, Output } from '@angular/core';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { PROBLEMAS_POR_ERROR } from '@historia-clinica/constants/summaries';
import { Subject } from 'rxjs';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
    selector: 'app-amended-problems',
    templateUrl: './amended-problems.component.html',
    styleUrls: ['./amended-problems.component.scss']
})
export class AmendedProblemsComponent implements OnInit {
    
    @Output() setProblemOnHistoric = new Subject<HCEPersonalHistoryDto>();
    public amendedProblems: ProblemsData[];
	public readonly problemasPorError = PROBLEMAS_POR_ERROR;
    public isFilterExpanded = false;
    public severityTypeMasterData: any[];

    // Injected dependencies
    private readonly internacionMasterDataService: InternacionMasterDataService;
    
    constructor(
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
        private injector: Injector,
    ) { 
		this.internacionMasterDataService = this.injector.get<InternacionMasterDataService>(InternacionMasterDataService);
    }

    ngOnInit(): void {
        this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypeMasterData = healthConditionSeverities;
            this.setAmendedProblems();
		});
    }

    setAmendedProblems() {
		this.ambulatoriaSummaryFacadeService.amendedProblems$.subscribe(amendedProblems => {
            this.amendedProblems = this.mapToProblemsData(amendedProblems)
        })
	}

    getSeverityTypeDisplayByCode(severityCode: string): string {
        return (severityCode && this.severityTypeMasterData) ?
            this.severityTypeMasterData.find(severityType => severityType.code === severityCode).display
            : '';
    }

    mapToProblemsData(problems: HCEPersonalHistoryDto[]) {
		return problems.map((problem: HCEPersonalHistoryDto) => {
			return {
				data: problem,
				startDate: problem.startDate ? momentFormat(momentParseDate(problem.startDate), DateFormat.VIEW_DATE) : undefined,
				endDate: problem.inactivationDate ? momentFormat(momentParseDate(problem.inactivationDate), DateFormat.VIEW_DATE) : undefined,
                severity: problem.severity ? this.getSeverityTypeDisplayByCode(problem.severity) : '',
			};
		});
	}    

    toggleFilter() {
        this.isFilterExpanded = !this.isFilterExpanded;
    }

    viewProblemDetails(problem: HCEPersonalHistoryDto) {
        this.setProblemOnHistoric.next(problem);
    }
}


export interface ProblemsData {
    data: HCEPersonalHistoryDto,
    startDate: string,
    endDate: string,
    severity: string,
}