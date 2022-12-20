import {Component, Input} from '@angular/core';
import {HCEPersonalHistoryDto} from '@api-rest/api-model';
import {SummaryHeader} from '@presentation/components/summary-card/summary-card.component';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-antecedentes-personales-summary',
	templateUrl: './antecedentes-personales-summary.component.html',
	styleUrls: ['./antecedentes-personales-summary.component.scss']
})
export class AntecedentesPersonalesSummaryComponent {
	problems: Problem[] = [];
	private severityTypesMasterData: any[] = [];
	@Input() personalHistoriesHeader: SummaryHeader;
	@Input() 
	set personalHistory(personalHistory: HCEPersonalHistoryDto[]){
		if (personalHistory.length){
			this.setSeverityMasterData(personalHistory);
		}
	};

	constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService
	) {}

	getSeverityTypeDisplayByCode(severityCode): string {
		return (severityCode && this.severityTypesMasterData) ?
			this.severityTypesMasterData.find(severityType => severityType.code === severityCode).display
			: '';
	}

	private setProblems(problems: HCEPersonalHistoryDto[]){
		this.problems = problems.map(prob => {
			return this.mapToProblem(prob);
		});
	}

	private setSeverityMasterData(problems: HCEPersonalHistoryDto[]){
		if (!this.severityTypesMasterData.length){
			this.internacionMasterDataService.getHealthSeverity().subscribe(
				severityTypes => {
					this.severityTypesMasterData = severityTypes;
					this.setProblems(problems);
				}
			);
		}
	}

	private mapToProblem(problem: HCEPersonalHistoryDto): Problem {
		return {
			data: problem,
			severityName: this.getSeverityTypeDisplayByCode(problem.severity)
		}
	}
}

interface Problem {
	data: HCEPersonalHistoryDto;
	severityName: string;
}