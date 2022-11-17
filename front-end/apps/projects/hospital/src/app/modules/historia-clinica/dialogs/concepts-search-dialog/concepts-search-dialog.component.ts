import { Component, Inject, OnInit } from '@angular/core';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnomedDto } from '@api-rest/api-model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnowstormService, SNOMED_RESULTS_LIMIT } from '@api-rest/services/snowstorm.service';
import { SnomedSemanticSearch } from '../../services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { FarmacosService } from '@historia-clinica/services/farmacos.service';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-concepts-search-dialog',
	templateUrl: './concepts-search-dialog.component.html',
	styleUrls: ['./concepts-search-dialog.component.scss']
})
export class ConceptsSearchDialogComponent implements OnInit {

	conceptsResultsTable: TableModel<any>;
	conceptsResultsLength: number;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	readonly SNOMED_RESULTS_LIMIT: number = +SNOMED_RESULTS_LIMIT;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: SnomedSemanticSearch,
		public dialogRef: MatDialogRef<ConceptsSearchDialogComponent>,
		private readonly snowstormService: SnowstormService,
		private readonly snackBarService: SnackBarService,
		private readonly farmacosService: FarmacosService,
		private readonly contextService: ContextService
	) { }

	ngOnInit(): void {
		this.setVademecumPharmacos();
	}

	private setVademecumPharmacos() {
		this.farmacosService.getPharmacos({ term: this.data.searchValue, ecl: this.data.eclFilter, institutionId: this.contextService.institutionId }).subscribe(
			result => {
				if (result.total === 0)
					this.setSnomedPharmacos();
				else
					this.buildConcepts(result);
			}, 
			error => {
				this.setSnomedPharmacos();
			}
		);
	}

	private setSnomedPharmacos() {
		this.snowstormService.getSNOMEDConcepts({ term: this.data.searchValue, ecl: this.data.eclFilter }).subscribe(
			result => {
				this.buildConcepts(result);
			},
			error => {
				this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
				this.snowstormServiceErrorMessage = error.text ? error.text : error.message;
				this.snowstormServiceNotAvailable = true;
			}
		);
	}

	private buildConcepts(result) {
		this.conceptsResultsTable = this.buildConceptsResultsTable(result.items);
		this.conceptsResultsLength = result.total;
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
