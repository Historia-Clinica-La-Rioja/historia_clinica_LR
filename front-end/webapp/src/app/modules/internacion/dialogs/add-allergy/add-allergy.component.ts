import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SEMANTICS_CONFIG } from '../../constants/snomed-semantics';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnowstormService } from '@api-rest/services/snowstorm.service';

@Component({
	selector: 'app-add-allergy',
	templateUrl: './add-allergy.component.html',
	styleUrls: ['./add-allergy.component.scss']
})
export class AddAllergyComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: FormGroup;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	searchClicked = false;
	conceptsResultsTable: TableModel<any>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { searchValue: string, eclFilter: string },
		public dialogRef: MatDialogRef<AddAllergyComponent>,
		private formBuilder: FormBuilder,
		private readonly snowstormService: SnowstormService
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

	submit(): void {
		this.dialogRef.close(this.snomedConcept);
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searchClicked = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: this.SEMANTICS_CONFIG.allergy})
				.subscribe(
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results);
						this.searchClicked = false;
					}
				);
		}
	}

}
