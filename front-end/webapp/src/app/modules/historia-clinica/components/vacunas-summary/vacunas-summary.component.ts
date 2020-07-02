import { Component, Input, OnInit } from '@angular/core';
import { VACUNAS } from '../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { InmunizationDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { AddInmunizationComponent } from '../../dialogs/add-inmunization/add-inmunization.component';

@Component({
  selector: 'app-vacunas-summary',
  templateUrl: './vacunas-summary.component.html',
  styleUrls: ['./vacunas-summary.component.scss']
})
export class VacunasSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	public readonly vacunasSummary = VACUNAS;
	@Input() editable = false;


	tableModel: TableModel<InmunizationDto>;

	constructor(
		public dialog: MatDialog,
		private readonly internmentStateService: InternmentStateService
	) {
	}

	ngOnInit(): void {
		this.internmentStateService.getInmunizations(this.internmentEpisodeId).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
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
					this.internmentStateService.getInmunizations(this.internmentEpisodeId)
						.subscribe(data => this.tableModel = this.buildTable(data));
				}
			}
		);
	}

	private buildTable(data: InmunizationDto[]): TableModel<InmunizationDto> {
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
