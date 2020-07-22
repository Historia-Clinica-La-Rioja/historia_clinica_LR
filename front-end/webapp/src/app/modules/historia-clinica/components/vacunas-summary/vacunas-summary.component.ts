import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { VACUNAS } from '../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { ImmunizationDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { AddInmunizationComponent } from '../../dialogs/add-inmunization/add-inmunization.component';

@Component({
  selector: 'app-vacunas-summary',
  templateUrl: './vacunas-summary.component.html',
  styleUrls: ['./vacunas-summary.component.scss']
})
export class VacunasSummaryComponent implements OnChanges {

	public readonly vacunasSummary = VACUNAS;
	@Input() internmentEpisodeId: number;
	@Input() immunizations: ImmunizationDto[];
	@Input() editable = false;


	tableModel: TableModel<ImmunizationDto>;

	constructor(
		public dialog: MatDialog,
		private readonly internmentStateService: InternmentStateService
	) {
	}

	ngOnChanges(changes: SimpleChanges) {
		this.tableModel = this.buildTable(this.immunizations);
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddInmunizationComponent, {
			disableClose: true,
			width: '35%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
				if (submitted) {
					this.internmentStateService.getImmunizations(this.internmentEpisodeId)
						.subscribe(data => this.tableModel = this.buildTable(data));
				}
			}
		);
	}

	private buildTable(data: ImmunizationDto[]): TableModel<ImmunizationDto> {
		return {
			columns: [
				{
					columnDef: 'vacuna',
					header: 'Vacuna',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'fecha',
					header: 'Fecha de vacunación',
					text: (row) => row.administrationDate ? momentFormat(momentParseDate(row.administrationDate), DateFormat.VIEW_DATE) : undefined
				}
			],
			data
		};
	}

}
