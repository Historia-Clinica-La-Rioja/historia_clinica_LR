import {Component, Input, OnChanges} from '@angular/core';
import {HCEPersonalHistoryDto, HealthConditionDto} from "@api-rest/api-model";
import {SummaryHeader} from "@presentation/components/summary-card/summary-card.component";
import {TableModel} from "@presentation/components/table/table.component";

@Component({
	selector: 'app-internacion-antecedentes-personales-summary',
	templateUrl: './internacion-antecedentes-personales-summary.component.html',
	styleUrls: ['./internacion-antecedentes-personales-summary.component.scss']
})
export class InternacionAntecedentesPersonalesSummaryComponent implements OnChanges {

	@Input() personalHistory: HCEPersonalHistoryDto[];
	@Input() personalHistoriesHeader: SummaryHeader;
	tableModel: TableModel<HealthConditionDto>;

	constructor() {
	}

	ngOnChanges(): void {
		this.tableModel = this.buildTable(this.personalHistory);
	}

	private buildTable(data: HCEPersonalHistoryDto[]): TableModel<HCEPersonalHistoryDto> {
		return {
			columns: [
				{
					columnDef: 'tipo',
					header: 'Tipo',
					text: (row) => row.snomed.pt
				}
			],
			data
		};
	}
}