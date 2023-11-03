import { Injectable } from '@angular/core';
import { HealthConditionDto } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { BasicTable } from '@material/model/table.model';

@Injectable()
export class ProblemEpicrisisService {

	constructor(
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
	) { }

	private table: BasicTable<HealthConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'Problemas asociados',
				display: ap => ap.snomed.pt
			}
		],
		displayedColumns: ['pt', 'remove'],

	};

	initTable(problem?: HealthConditionDto[]) {
		this.table.data = problem ? [...problem] : [];
		this.componentEvaluationManagerService.otherProblems = this.table.data;
	}

	addProblem(newProblem: HealthConditionDto) {
		const duplicatedProblem = this.table.data.find(diagnosis => diagnosis.snomed.sctid === newProblem.snomed.sctid);
		if (!duplicatedProblem) {
			this.table.data = pushTo<HealthConditionDto>(this.table?.data, newProblem);
			this.componentEvaluationManagerService.otherProblems = this.table.data;
		}
	}

	remove(index: number) {
		this.table.data = removeFrom<HealthConditionDto>(this.table.data, index);
		this.componentEvaluationManagerService.otherProblems = this.table.data;
	}

	getTable(): BasicTable<HealthConditionDto> {
		return {
			data: this.table.data,
			columns: this.table.columns,
			displayedColumns: this.table.displayedColumns
		};
	}

	getProblems(): HealthConditionDto[] {
		return this.table.data
	}

	setProblems(problems: HealthConditionDto[]) {
		this.table.data = problems;
		this.componentEvaluationManagerService.otherProblems = this.table.data;
	}

	isEmpty(): boolean {
		return (!this.table?.data || this.table.data?.length === 0);
	}
}
