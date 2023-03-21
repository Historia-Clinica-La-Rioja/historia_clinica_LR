import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AllergyConditionDto, EvolutionNoteDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL} from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-add-allergy',
	templateUrl: './add-allergy.component.html',
	styleUrls: ['./add-allergy.component.scss']
})
export class AddAllergyComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: FormGroup;
	loading = false;

	searching = false;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage : string;
	conceptsResultsTable: TableModel<any>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { internmentEpisodeId: number },
		public dialogRef: MatDialogRef<AddAllergyComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly snowstormService: SnowstormService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required]
		});
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

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: SnomedECL.ALLERGY})
				.subscribe(
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results.items);
						this.searching = false;
					},
					error => {
						this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
						this.snowstormServiceErrorMessage = error.text ? error.text : error.message;
						this.snowstormServiceNotAvailable = true;
					}
				);
		}
	}

	submit(): void {
		if (this.snomedConcept) {
			this.loading = true;
			const evolutionNote: EvolutionNoteDto = buildEvolutionNote(this.snomedConcept);
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId).subscribe(_ => {
					this.snackBarService.showSuccess('internaciones.internacion-paciente.alergias-summary.save.SUCCESS');
					this.dialogRef.close(true);
					this.internmentSummaryFacadeService.setFieldsToUpdate({evolutionClinical: true});
				}, _ => {
					this.snackBarService.showError('internaciones.internacion-paciente.alergias-summary.save.ERROR');
					this.loading = false;
				}
			);
		}

		function buildEvolutionNote(allergy: SnomedDto): EvolutionNoteDto {
			const allergyDto: AllergyConditionDto = {
				categoryId: null,
				date: null,
				criticalityId: null,
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

}
