import { Component, OnInit } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { MatDialogRef } from '@angular/material/dialog';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MIN_DATE } from "@core/utils/date.utils";

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

	searching = false;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	conceptsResultsTable: TableModel<any>;

	minDate = MIN_DATE;

	constructor(
		public dialogRef: MatDialogRef<AddInmunizationComponent>,
		private readonly formBuilder: FormBuilder,
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

	selectedDateIsTodayOrBefore(): boolean {
		return (this.form.value.date) ? this.today.isSameOrAfter(this.form.value.date, 'day') : true;
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
			administrationDate: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
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
