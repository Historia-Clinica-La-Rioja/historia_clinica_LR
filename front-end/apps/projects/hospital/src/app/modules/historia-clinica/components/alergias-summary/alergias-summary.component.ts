import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {AllergyConditionDto, HealthConditionDto} from '@api-rest/api-model';
import {TableModel} from '@presentation/components/table/table.component';
import {InternmentStateService} from '@api-rest/services/internment-state.service';
import {ALERGIAS} from '../../constants/summaries';
import {MatDialog} from '@angular/material/dialog';
import {AddAllergyComponent} from '../../dialogs/add-allergy/add-allergy.component';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-alergias-summary',
	templateUrl: './alergias-summary.component.html',
	styleUrls: ['./alergias-summary.component.scss']
})
export class AlergiasSummaryComponent implements OnInit, OnChanges {

	@Input() internmentEpisodeId: number;
	@Input() allergies: AllergyConditionDto[];
	@Input() editable = false;

	public readonly alergiasSummary = ALERGIAS;
	private criticalityMasterData: any[];
	private categoryMasterData: any[];

	tableModel: TableModel<HealthConditionDto>;

	private buildTable(data: AllergyConditionDto[]): TableModel<AllergyConditionDto> {
		return {
			columns: [
				{
					columnDef: 'tipo',
					header: 'Tipo de alergia',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'categoria',
					header: 'Categoría',
					text: (row) => this.getCategoryDisplayName(row.categoryId)
				},
				{
					columnDef: 'criticidad',
					header: 'Criticidad',
					text: (row) => this.getCriticalityDisplayName(row.criticalityId)
				}
				],
			data
		};
	}

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		public dialog: MatDialog
	) {
	}

	ngOnInit(): void {
		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities =>
			this.criticalityMasterData = allergyCriticalities
		);

		this.internacionMasterDataService.getAllergyCategories().subscribe(allergyCategories =>
			this.categoryMasterData = allergyCategories
		);
	}

	getCriticalityDisplayName(criticalityId): string {
		return criticalityId ? this.criticalityMasterData.find(c => c.id === criticalityId).display : '';
	}

	getCategoryDisplayName(categoryId): string {
		return categoryId ? this.categoryMasterData.find(c => c.id === categoryId).display : '';
	}

	ngOnChanges(): void {
		this.tableModel = this.buildTable(this.allergies);
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
						.subscribe(data => this.tableModel = this.buildTable(data));
				}
			}
		);
	}

}
