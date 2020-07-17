import { Component, Input, OnChanges } from '@angular/core';
import { ANTECEDENTES_PERSONALES } from '../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { HealthConditionDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';

@Component({
	selector: 'app-antecedentes-personales-summary',
	templateUrl: './antecedentes-personales-summary.component.html',
	styleUrls: ['./antecedentes-personales-summary.component.scss']
})
export class AntecedentesPersonalesSummaryComponent implements OnChanges {

	@Input() personalHistory: HealthHistoryConditionDto[];
	@Input() personalHistoriesHeader: SummaryHeader;

	tableModel: TableModel<HealthConditionDto>;

	constructor(
		private internmentStateService: InternmentStateService
	) {
	}

	ngOnChanges(): void {
		this.tableModel = this.buildTable(this.personalHistory);
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
