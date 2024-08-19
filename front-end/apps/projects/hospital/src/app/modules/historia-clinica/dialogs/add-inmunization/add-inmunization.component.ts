import { Component, OnInit } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { MatDialogRef } from '@angular/material/dialog';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { newDate } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { toApiFormat } from '@api-rest/mapper/date.mapper';

@Component({
	selector: 'app-add-inmunization',
	templateUrl: './add-inmunization.component.html',
	styleUrls: ['./add-inmunization.component.scss']
})
export class AddInmunizationComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: UntypedFormGroup;
	loading = false;
	today = newDate();

	searching = false;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	conceptsResultsTable: TableModel<any>;

	minDate = MIN_DATE;

	constructor(
		public dialogRef: MatDialogRef<AddInmunizationComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snowstormService: SnowstormService,
		private readonly snackBarService: SnackBarService,
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			date: [null]
		});
	}

	dateChanged(date: Date) {
		this.form.controls.date.setValue(date);
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
			this.snowstormService.getSNOMEDConcepts({ term: searchValue, ecl: SnomedECL.VACCINE })
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
			const inmunization: Immunization = this.buildImmunization();
			this.dialogRef.close(inmunization);
		}
	}

	private buildImmunization(): Immunization {
		return {
			administrationDate: this.form.value.date ? toApiFormat(this.form.value.date) : null,
			snomed: this.snomedConcept
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
export interface Immunization {
	administrationDate: string;
	snomed: SnomedDto;
}
