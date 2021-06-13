import { Component, Inject, OnInit } from '@angular/core';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnomedDto } from '@api-rest/api-model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnowstormService, SNOMED_RESULTS_LIMIT } from '@api-rest/services/snowstorm.service';
import { SnomedSemanticSearch } from '../../services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-concepts-search-dialog',
	templateUrl: './concepts-search-dialog.component.html',
	styleUrls: ['./concepts-search-dialog.component.scss']
})
export class ConceptsSearchDialogComponent implements OnInit {

	conceptsResultsTable: TableModel<any>;
	conceptsResultsLength: number;
	snowstormServiceNotAvailable = false;
	readonly SNOMED_RESULTS_LIMIT: number = +SNOMED_RESULTS_LIMIT;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: SnomedSemanticSearch,
		public dialogRef: MatDialogRef<ConceptsSearchDialogComponent>,
		private readonly snowstormService: SnowstormService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.snowstormService.getSNOMEDConcepts({ term: this.data.searchValue, ecl: this.data.eclFilter }).subscribe(
			result => {
					this.conceptsResultsTable = this.buildConceptsResultsTable(result.items);
					this.conceptsResultsLength = result.total;
			},
			error => {
				this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
				this.snowstormServiceNotAvailable = true;
			}
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
