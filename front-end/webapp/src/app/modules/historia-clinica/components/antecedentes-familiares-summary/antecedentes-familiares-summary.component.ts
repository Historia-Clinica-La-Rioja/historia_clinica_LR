import { Component, Input, OnInit } from '@angular/core';
import { ANTECEDENTES_FAMILIARES } from '../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { HealthConditionDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';

@Component({
  selector: 'app-antecedentes-familiares-summary',
  templateUrl: './antecedentes-familiares-summary.component.html',
  styleUrls: ['./antecedentes-familiares-summary.component.scss']
})
export class AntecedentesFamiliaresSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	public readonly antecedentesFamiliaresSummary = ANTECEDENTES_FAMILIARES;

	tableModel: TableModel<HealthConditionDto>;

	constructor(
		private internmentStateService: InternmentStateService
	) {
	}

	ngOnInit(): void {
		this.internmentStateService.getFamilyHistories(this.internmentEpisodeId).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
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
