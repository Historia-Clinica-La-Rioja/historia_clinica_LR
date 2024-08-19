import { Component, Injector, OnInit, Output } from '@angular/core';
import { HCEHealthConditionDto } from '@api-rest/api-model';
import { PROBLEMAS_POR_ERROR } from '@historia-clinica/constants/summaries';
import { Subject, map } from 'rxjs';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { dateISOParseDate, } from '@core/utils/moment.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-amended-problems',
	templateUrl: './amended-problems.component.html',
	styleUrls: ['./amended-problems.component.scss']
})
export class AmendedProblemsComponent implements OnInit {

	@Output() setProblemOnHistoric = new Subject<HCEHealthConditionDto>();
	amendedProblems: ProblemsData[];
	readonly problemasPorError = PROBLEMAS_POR_ERROR;
	isFilterExpanded = false;
	severityTypeMasterData: any[];

	// Injected dependencies
	private readonly internacionMasterDataService: InternacionMasterDataService;

	constructor(
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private injector: Injector,
		private readonly dateFormatPipe: DateFormatPipe
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
		this.ambulatoriaSummaryFacadeService.amendedProblems$.pipe(map(problems => this.amendedProblems = this.mapToProblemsData(problems))).subscribe();
	}

	getSeverityTypeDisplayByCode(severityCode: string): string {
		return (severityCode && this.severityTypeMasterData) ?
			this.severityTypeMasterData.find(severityType => severityType.code === severityCode).display
			: '';
	}

	mapToProblemsData(problems: HCEHealthConditionDto[]): ProblemsData[] {
		return problems.map((problem: HCEHealthConditionDto) => {
			return {
				data: problem,
				startDate: problem.startDate ? this.dateFormatPipe.transform(dateISOParseDate(problem.startDate), 'date') : undefined,
				endDate: problem.inactivationDate ? this.dateFormatPipe.transform(dateISOParseDate(problem.inactivationDate), 'date') : undefined,
				severity: problem.severity ? this.getSeverityTypeDisplayByCode(problem.severity) : '',
			};
		});
	}

	toggleChanged() {
		this.isFilterExpanded = !this.isFilterExpanded;
	}

	viewProblemDetails(problem: HCEHealthConditionDto) {
		this.setProblemOnHistoric.next(problem);
	}
}


export interface ProblemsData {
	data: HCEHealthConditionDto,
	startDate: string,
	endDate: string,
	severity: string,
}
