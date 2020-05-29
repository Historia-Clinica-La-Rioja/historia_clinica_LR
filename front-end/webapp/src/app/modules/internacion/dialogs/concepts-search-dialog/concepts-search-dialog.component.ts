import { Component, OnInit, Inject } from '@angular/core';
import { TableModel, ActionDisplays } from 'src/app/modules/presentation/components/table/table.component';
import { SnomedDto } from '@api-rest/api-model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnowstormService } from '@api-rest/services/snowstorm.service';

@Component({
	selector: 'app-concepts-search-dialog',
	templateUrl: './concepts-search-dialog.component.html',
	styleUrls: ['./concepts-search-dialog.component.scss']
})
export class ConceptsSearchDialogComponent implements OnInit {

	conceptsResultsTable: TableModel<any>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { searchValue: string, eclFilter: string },
		public dialogRef: MatDialogRef<ConceptsSearchDialogComponent>,
		private readonly snowstormService: SnowstormService,
	) { }

	ngOnInit(): void {
		this.snowstormService.getSNOMEDConcepts({ term: this.data.searchValue, ecl: this.data.eclFilter }).subscribe(
			results => this.conceptsResultsTable = this.buildConceptsResultsTable(results)
		);
	}

	private buildConceptsResultsTable(data: SnomedDto[]): TableModel<SnomedDto> {
		return {
			columns: [
				{
					columnDef: '1',
					header: 'Descripción SNOMED',
					text: concept => concept.pt
				},
				{
					columnDef: 'select',
					action: {
						displayType: ActionDisplays.BUTTON,
						display: 'Seleccionar',
						matColor: 'primary',
						do: concept => this.dialogRef.close(concept)
					}
				},
			],
			data,
			enablePagination: true
		};
	}

}
