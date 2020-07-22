import { Component, Inject, OnInit } from '@angular/core';
import { EvolutionNoteDto, ImmunizationDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SEMANTICS_CONFIG } from '../../constants/snomed-semantics';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

@Component({
  selector: 'app-add-inmunization',
  templateUrl: './add-inmunization.component.html',
  styleUrls: ['./add-inmunization.component.scss']
})
export class AddInmunizationComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: FormGroup;
	loading = false;
	today: Moment = newMoment();
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	searching = false;
	conceptsResultsTable: TableModel<any>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { internmentEpisodeId: number },
		public dialogRef: MatDialogRef<AddInmunizationComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly snowstormService: SnowstormService
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			date: [null]
		});
	}

	chosenYearHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.year(newDate.year());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	chosenMonthHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.month(newDate.month());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
		this.dialogRef.updateSize('20%');
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
		delete this.conceptsResultsTable;
		this.dialogRef.updateSize('35%');
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: this.SEMANTICS_CONFIG.vaccine})
				.subscribe(
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results);
						this.searching = false;
					}
				);
		}
	}

	submit(): void {
		if (this.snomedConcept) {
			this.loading = true;
			const evolutionNote: EvolutionNoteDto = this.buildEvolutionNote();
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId).subscribe(_ => {
					this.snackBarService.showSuccess('internaciones.internacion-paciente.vacunas-summary.save.SUCCESS');
					this.dialogRef.close(true);
				}, _ => {
					this.snackBarService.showError('internaciones.internacion-paciente.vacunas-summary.save.ERROR');
					this.loading = false;
				}
			);
		}
	}

	private buildEvolutionNote(): EvolutionNoteDto {
		const inmunizationDto: ImmunizationDto = {
			administrationDate: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
			note: null,
			snomed: this.snomedConcept
		};

		return {
			confirmed: true,
			immunizations: [inmunizationDto]
		};

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
						do: concept => this.setConcept(concept)
					}
				},
			],
			data,
			enablePagination: true
		};
	}

}
