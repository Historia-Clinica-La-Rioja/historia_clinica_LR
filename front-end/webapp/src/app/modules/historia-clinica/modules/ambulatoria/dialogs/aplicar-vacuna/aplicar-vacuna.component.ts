import { Component, OnInit, Inject } from '@angular/core';
import { SnomedDto, OutpatientImmunizationDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HceImmunizationService } from '@api-rest/services/hce-immunization.service';
import { MatDialog } from "@angular/material/dialog";
import { TranslateService } from '@ngx-translate/core';
import { ConfirmDialogComponent } from "@core/dialogs/confirm-dialog/confirm-dialog.component";

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
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number },
		public dialogRef: MatDialogRef<AplicarVacunaComponent>,
		private readonly snowstormService: SnowstormService,
		private readonly hceImmunizationService: HceImmunizationService,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly translator: TranslateService
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			date: [null, Validators.required],
			snomed: [null, Validators.required],
			note: [null, Validators.required]
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
			this.translator.get('ambulatoria.paciente.vacunas.aplicar.save.CHANGE_STATE').subscribe((res: string) => {
				const finishAppointment = this.dialog.open(ConfirmDialogComponent, {
					width: '450px',
					data: {
						title: 'Aplicar vacuna',
						content: `${res}`,
						okButtonLabel: 'buttons.YES',
						cancelButtonLabel: 'buttons.NO'
					}
				});

				finishAppointment.afterClosed().subscribe(result => {
						this.loading = true;
						const vacuna: OutpatientImmunizationDto = {
							administrationDate: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
							note: this.form.value.note,
							snomed: this.snomedConcept
						};
						this.hceImmunizationService.gettingVaccine(vacuna, this.data.patientId, !result)
						.subscribe((response: boolean) => {
							this.loading = false;
							this.dialogRef.close(vacuna);
							this.snackBarService.showSuccess('ambulatoria.paciente.vacunas.aplicar.save.SUCCESS');
						}, _ => {
							this.snackBarService.showError('ambulatoria.paciente.vacunas.aplicar.save.ERROR');
							this.loading = false;
						});

				});
			});
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
