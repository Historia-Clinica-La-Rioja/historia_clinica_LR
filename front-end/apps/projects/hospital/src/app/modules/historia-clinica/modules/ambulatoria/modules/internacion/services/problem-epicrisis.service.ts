import { Injectable } from '@angular/core';
import { HealthConditionDto } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { BasicTable } from '@material/model/table.model';

@Injectable({
	providedIn: 'root'
})
export class ProblemEpicrisisService {

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
	}

	addProblem(newProblem: HealthConditionDto) {
		const duplicatedProblem = this.table.data.find(diagnosis => diagnosis.snomed.sctid === newProblem.snomed.sctid);
		if (!duplicatedProblem)
			this.table.data = pushTo<HealthConditionDto>(this.table?.data, newProblem);
	}

	remove(index: number) {
		this.table.data = removeFrom<HealthConditionDto>(this.table.data, index);
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
	}
}
