import { Component, Input, OnInit } from '@angular/core';
import { AllergyConditionDto, EvolutionNoteDto, HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { TableModel } from '@presentation/components/table/table.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ALERGIAS } from '../../constants/summaries';
import { MatDialog } from '@angular/material/dialog';
import { AddAllergyComponent } from '../../dialogs/add-allergy/add-allergy.component';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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
		private internmentStateService: InternmentStateService,
		private evolutionNoteService: EvolutionNoteService,
		private snackBarService: SnackBarService,
		public dialog: MatDialog
	) {
	}

	ngOnInit(): void {
		this.internmentStateService.getAllergies(this.internmentEpisodeId).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddAllergyComponent, {
			disableClose: true,
			width: '35%',
		});

		dialogRef.afterClosed().subscribe((allergy: SnomedDto) => {
				if (allergy) {
					const evolutionNote: EvolutionNoteDto = buildEvolutionNote(allergy);
					this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId).subscribe(_ => {
								this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
								this.internmentStateService.getAllergies(this.internmentEpisodeId)
									.subscribe(data => this.tableModel = this.buildTable(data));
							}, _ => this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR')
						);
				}
			}
		);

		function buildEvolutionNote(allergy: SnomedDto): EvolutionNoteDto {
			const allergyDto: AllergyConditionDto = {
				categoryId: null,
				date: null,
				severity: null,
				verificationId: null,
				id: null,
				snomed: allergy,
				statusId: null
			};

			return {
				confirmed: true,
				allergies: [allergyDto]
			};

		}
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
