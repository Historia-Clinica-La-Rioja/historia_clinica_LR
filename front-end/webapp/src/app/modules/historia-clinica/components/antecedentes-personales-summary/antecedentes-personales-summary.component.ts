import { Component, Input, OnInit } from '@angular/core';
import { ANTECEDENTES_PERSONALES } from '../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { HealthConditionDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';

@Component({
  selector: 'app-antecedentes-personales-summary',
  templateUrl: './antecedentes-personales-summary.component.html',
  styleUrls: ['./antecedentes-personales-summary.component.scss']
})
export class AntecedentesPersonalesSummaryComponent implements OnInit {
	@Input() internmentEpisodeId: number;

	public readonly antecedentesPersonalesSummary = ANTECEDENTES_PERSONALES;

	tableModel: TableModel<HealthConditionDto>;

	constructor(
		private internmentStateService: InternmentStateService
	) {
	}

	ngOnInit(): void {
		this.internmentStateService.getPersonalHistories(this.internmentEpisodeId).subscribe(
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
