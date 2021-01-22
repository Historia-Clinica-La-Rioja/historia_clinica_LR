import {Component, Input, OnChanges} from '@angular/core';
import {AllergyConditionDto, HealthConditionDto} from '@api-rest/api-model';
import {TableModel} from '@presentation/components/table/table.component';
import {InternmentStateService} from '@api-rest/services/internment-state.service';
import {ALERGIAS} from '../../constants/summaries';
import {MatDialog} from '@angular/material/dialog';
import {AddAllergyComponent} from '../../dialogs/add-allergy/add-allergy.component';

@Component({
	selector: 'app-alergias-summary',
	templateUrl: './alergias-summary.component.html',
	styleUrls: ['./alergias-summary.component.scss']
})
export class AlergiasSummaryComponent implements OnChanges {

	@Input() internmentEpisodeId: number;
	@Input() allergies: AllergyConditionDto[];
	@Input() editable = false;

	public readonly alergiasSummary = ALERGIAS;

	tableModel: TableModel<HealthConditionDto>;

	private static buildTable(data: AllergyConditionDto[]): TableModel<AllergyConditionDto> {
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

	constructor(
		private readonly internmentStateService: InternmentStateService,
		public dialog: MatDialog
	) {
	}

	ngOnChanges(): void {
		this.tableModel = AlergiasSummaryComponent.buildTable(this.allergies)
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddAllergyComponent, {
			disableClose: true,
			width: '35%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
				if (submitted) {
					this.internmentStateService.getAllergies(this.internmentEpisodeId)
						.subscribe(data => this.tableModel = AlergiasSummaryComponent.buildTable(data));
				}
			}
		);
	}

}
