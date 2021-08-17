import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DateFormat, momentParseDate, newMoment } from '@core/utils/moment.utils';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { ImmunizationDto, SnomedDto, SnomedResponseDto, VaccineConditionsDto, VaccineDoseInfoDto, VaccineInformationDto, VaccineSchemeDto } from '@api-rest/api-model';
import { VaccineService } from '@api-rest/services/vaccine.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-agregar-vacuna',
	templateUrl: './agregar-vacuna.component.html',
	styleUrls: ['./agregar-vacuna.component.scss']
})
export class AgregarVacunaComponent implements OnInit {

	// shared attributtes (used on both forms)
	doses: VaccineDoseInfoDto[];
	schemes: VaccineSchemeDto[];
	conditions: VaccineConditionsDto[];
	private readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;
	today: Moment = newMoment();

	// billable form attributes (new vaccine application)
	billableForm: FormGroup;
	newVaccineSnomedConcept: SnomedDto;
	searchingNew = false;
	conceptsResultsTable: TableModel<any>;

	// previous form attributes (register previous vaccine application)
	previousForm: FormGroup;
	previousVaccineSnomedConcept: SnomedDto;
	searchingPrevious = false;
	conceptsResultsTablePreviousForm: TableModel<any>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data:
			{
				immunization?: ImmunizationDto,
				edit?: boolean
			},
		private readonly formBuilder: FormBuilder,
		private readonly snowstormService: SnowstormService,
		private readonly vaccineService: VaccineService,
		private readonly snackBarService: SnackBarService,
		public dialogRef: MatDialogRef<AgregarVacunaComponent>
	) { }

	ngOnInit(): void {

		this.previousForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [null, Validators.required],
			scheme: [null, Validators.required],
			dose: [null, Validators.required],
			lot: [null],
			institution: [null],
			professional: [null]
		});

		this.billableForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [{ value: null, disabled: true }],
			scheme: [{ value: null, disabled: true }],
			dose: [{ value: null, disabled: true }],
			lot: [null],
			note: [null]
		});

		if (this.data?.edit) { // then load information to form fields and class attributes
			if (this.data.immunization?.billable) { // its the only case for now

				this.billableForm.controls.date.setValue(momentParseDate(this.data.immunization.administrationDate));

				this.newVaccineSnomedConcept = this.data.immunization.snomed;
				this.billableForm.controls.snomed.setValue(this.newVaccineSnomedConcept.pt);

				this.billableForm.controls.note.setValue(this.data.immunization.note);

				if (this.data.immunization.lotNumber)
					this.billableForm.controls.lot.setValue(this.data.immunization.lotNumber);

				this.vaccineService.vaccineInformation(this.newVaccineSnomedConcept.sctid).subscribe(
					(vaccineInformation: VaccineInformationDto) => {
						this.conditions = vaccineInformation.conditions;
						this.billableForm.get("condition").enable();
						const conditionIndex: number = this.conditions.findIndex(condition => condition.id === this.data.immunization.conditionId);
						if (conditionIndex >= 0) // mean that a condition was selected therefore also a scheme and dose
						{
							this.billableForm.get("condition").setValue(conditionIndex);

							this.schemes = this.conditions[conditionIndex].schemes;
							this.billableForm.get("scheme").enable();
							const schemeIndex: number = this.schemes.findIndex(scheme => scheme.id === this.data.immunization.schemeId);
							if (schemeIndex >= 0) {
								this.billableForm.get("scheme").setValue(schemeIndex);

								this.doses = this.schemes[schemeIndex].doses;
								this.billableForm.get("dose").enable();
								const doseIndex: number = this.doses.findIndex(dose => (dose.order == this.data.immunization.dose.order) && (dose.description == this.data.immunization.dose.description));
								if (doseIndex >= 0)
									this.billableForm.get("dose").setValue(doseIndex);
							}
						}

					}
				);

			}
		}

	}

	public chosenYearHandler(newDate: Moment, form: FormGroup): void {
		if (form.controls.date.value !== null) {
			const ctrlDate: Moment = form.controls.date.value;
			ctrlDate.year(newDate.year());
			form.controls.date.setValue(ctrlDate);
		} else {
			form.controls.date.setValue(newDate);
		}
	}

	public chosenMonthHandler(newDate: Moment, form: FormGroup): void {
		if (form.controls.date.value !== null) {
			const ctrlDate: Moment = form.controls.date.value;
			ctrlDate.month(newDate.month());
			form.controls.date.setValue(ctrlDate);
		} else {
			form.controls.date.setValue(newDate);
		}
	}

	public submit(): void {
		if (this.billableForm.valid) {
			const appliedVaccine: ImmunizationDto = {
				snomed: this.newVaccineSnomedConcept,
				administrationDate: this.billableForm.value.date.format(DateFormat.API_DATE),
				billable: true,
				note: this.billableForm.value.note ? this.billableForm.value.note : "",
			};

			if (this.billableForm.controls.lot.value != null)
				appliedVaccine.lotNumber = this.billableForm.value.lot;

			if (this.conditions && this.billableForm.controls.condition.value != null) {
				appliedVaccine.conditionId = this.conditions[this.billableForm.value.condition].id;
				appliedVaccine.schemeId = this.schemes[this.billableForm.value.scheme].id;
				appliedVaccine.dose = this.doses[this.billableForm.value.dose];
			}

			this.dialogRef.close(appliedVaccine);
		}
	}

	public submitPreviousForm(): void {
		if (this.previousForm.valid) {
			const appliedVaccine: ImmunizationDto = {
				snomed: this.previousVaccineSnomedConcept,
				administrationDate: this.previousForm.value.date.format(DateFormat.API_DATE),
				note: "",
			};

			if (this.previousForm.controls.lot.value != null)
				appliedVaccine.lotNumber = this.previousForm.value.lot;

			if (this.conditions && this.previousForm.controls.condition.value != null) {
				appliedVaccine.conditionId = this.conditions[this.previousForm.value.condition].id;
				appliedVaccine.schemeId = this.schemes[this.previousForm.value.scheme].id;
				appliedVaccine.dose = this.doses[this.previousForm.value.dose];
			}

			this.dialogRef.close(appliedVaccine);
		}
	}

	public onSearchBillableForm(searchValue: string): void {
		if (searchValue) {
			this.searchingNew = true;
			this.snowstormService.getSNOMEDConcepts({ term: searchValue, ecl: this.SEMANTICS_CONFIG.vaccine })
				.subscribe(
					(results: SnomedResponseDto) => {
						this.conceptsResultsTable = this.buildConceptsResultsTableBillableForm(results.items);
						this.searchingNew = false;
					},
					_ => {
						this.searchingNew = false;
						this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
					}
				);
		}
	}

	public onSearchPreviousForm(searchValue: string): void {
		if (searchValue) {
			this.searchingPrevious = true;
			this.snowstormService.getSNOMEDConcepts({ term: searchValue, ecl: this.SEMANTICS_CONFIG.vaccine })
				.subscribe(
					(results: SnomedResponseDto) => {
						this.conceptsResultsTablePreviousForm = this.buildConceptsResultsTablePreviousForm(results.items);
						this.searchingPrevious = false;
					},
					_ => {
						this.searchingPrevious = false;
						this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
					}
				);
		}
	}

	private buildConceptsResultsTableBillableForm(data: SnomedDto[]): TableModel<SnomedDto> {
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
						do: concept => this.setConceptBillableForm(concept)
					}
				}
			],
			data,
			enablePagination: true
		};
	}

	private buildConceptsResultsTablePreviousForm(data: SnomedDto[]): TableModel<SnomedDto> {
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
						do: concept => this.setConceptPreviousForm(concept)
					}
				}
			],
			data,
			enablePagination: true
		};
	}

	private setConceptBillableForm(selectedConcept: SnomedDto): void {
		this.newVaccineSnomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.billableForm.controls.snomed.setValue(pt);
		const sctId = selectedConcept ? selectedConcept.sctid : '';
		this.loadConditions(sctId, this.billableForm);
	}

	private setConceptPreviousForm(selectedConcept: SnomedDto): void {
		this.previousVaccineSnomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.previousForm.controls.snomed.setValue(pt);
		const sctId = selectedConcept ? selectedConcept.sctid : '';
		this.loadConditions(sctId, this.previousForm);
	}

	public resetConceptBillableForm(): void {
		delete this.newVaccineSnomedConcept;
		delete this.conceptsResultsTable;
		this.disableDoses(this.billableForm);
		this.disableSchemes(this.billableForm);
		this.disableConditions(this.billableForm);
	}

	public resetConceptPreviousForm(): void {
		delete this.previousVaccineSnomedConcept;
		delete this.conceptsResultsTablePreviousForm;
		this.disableDoses(this.previousForm);
		this.disableSchemes(this.previousForm);
		this.disableConditions(this.previousForm);
	}

	private loadConditions(sctid: string, form: FormGroup): void {
		this.vaccineService.vaccineInformation(sctid).subscribe(
			(vaccineInformation: VaccineInformationDto) => {
				this.disableDoses(form);
				this.disableSchemes(form);
				if (vaccineInformation.conditions.length > 0) {
					this.conditions = vaccineInformation.conditions;
					form.get("condition").enable();
					form.get("condition").setValue(null);
				}
				else
					this.disableConditions(form);
			}
		);
	}

	public loadSchemes(conditionIndex: number, form: FormGroup): void {
		this.disableDoses(form);
		if (form.value.condition != null) {
			this.schemes = this.conditions[conditionIndex].schemes;
			form.get("scheme").enable();
			form.get("scheme").setValue(null);
		}
		else
			this.disableSchemes(form);
	}

	public loadDoses(schemeIndex: number, form: FormGroup): void {
		this.doses = this.schemes[schemeIndex].doses;
		form.get("dose").enable();
		form.get("dose").setValue(null);
	}

	private disableConditions(form: FormGroup): void {
		delete this.conditions;
		form.get("condition").setValue(null);
		form.get("condition").disable();
	}

	private disableSchemes(form: FormGroup): void {
		delete this.schemes;
		form.get("scheme").setValue(null);
		form.get("scheme").disable();
	}

	private disableDoses(form: FormGroup): void {
		delete this.doses;
		form.get("dose").setValue(null);
		form.get("dose").disable();
	}

}
