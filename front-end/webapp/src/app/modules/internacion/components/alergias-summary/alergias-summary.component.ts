import { Component, Input, OnInit } from '@angular/core';
import { AllergyConditionDto, HealthConditionDto } from '@api-rest/api-model';
import { TableModel } from '@presentation/components/table/table.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ALERGIAS } from '../../constants/summaries';

@Component({
	selector: 'app-alergias-summary',
	templateUrl: './alergias-summary.component.html',
	styleUrls: ['./alergias-summary.component.scss']
})
export class AlergiasSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	public readonly alergiasSummary = ALERGIAS;

	tableModel: TableModel<HealthConditionDto>;

	constructor(
		private internmentStateService: InternmentStateService
	) {
	}

	ngOnInit(): void {
		this.internmentStateService.getAllergies(this.internmentEpisodeId).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
	}

	private buildTable(data: AllergyConditionDto[]): TableModel<AllergyConditionDto> {
		return {
			columns: [
				{
					columnDef: 'tipo',
					header: 'Tipo de alergia',
					text: (row) => row.snomed.pt
				}],
			data
		};
	}

}
