import { Component, OnInit } from '@angular/core';
import { InmunizationDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { MatDialogRef } from '@angular/material/dialog';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-aplicar-vacuna',
	templateUrl: './aplicar-vacuna.component.html',
	styleUrls: ['./aplicar-vacuna.component.scss']
})
export class AplicarVacunaComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: FormGroup;
	loading = false;
	today: Moment = newMoment();
	searching = false;
	conceptsResultsTable: TableModel<any>;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		public dialogRef: MatDialogRef<AplicarVacunaComponent>,
		private readonly snowstormService: SnowstormService,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			date: [null],
			snomed: [null, Validators.required],
			note: [null]
		});
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
		delete this.conceptsResultsTable;
	}

	save() {
		if (this.form.valid && this.snomedConcept) {
			this.loading = true;
			const vacuna: InmunizationDto = {
				administrationDate: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
				note: this.form.value.note,
				snomed: this.snomedConcept
			};
			this.loading = false;
			this.dialogRef.close(vacuna);
			this.snackBarService.showSuccess('ambulatoria.paciente.vacunas.aplicar.save.SUCCESS');
		}
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

}
