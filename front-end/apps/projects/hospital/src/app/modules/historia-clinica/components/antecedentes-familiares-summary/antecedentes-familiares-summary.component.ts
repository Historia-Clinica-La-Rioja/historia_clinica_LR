import { Component, Input, OnChanges } from '@angular/core';
import { TableModel } from '@presentation/components/table/table.component';
import { HealthConditionDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';

@Component({
	selector: 'app-antecedentes-familiares-summary',
	templateUrl: './antecedentes-familiares-summary.component.html',
	styleUrls: ['./antecedentes-familiares-summary.component.scss']
})
export class AntecedentesFamiliaresSummaryComponent implements OnChanges {

	@Input() familyHistories: HealthHistoryConditionDto[];
	@Input() familyHistoriesHeader: SummaryHeader;

	tableModel: TableModel<HealthConditionDto>;

	constructor() {}

	ngOnChanges(): void {
		this.tableModel = this.buildTable(this.familyHistories);
	}

	private buildTable(data: HealthHistoryConditionDto[]): TableModel<HealthHistoryConditionDto> {
		return {
			columns: [
				{
					columnDef: 'tipo',
					header: 'Tipo',
					text: (row) => row.snomed.pt
				}],
			data
		};
	}
}
