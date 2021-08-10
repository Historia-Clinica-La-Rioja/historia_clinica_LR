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

	doses: VaccineDoseInfoDto[];
	schemes: VaccineSchemeDto[];
	conditions: VaccineConditionsDto[];
	billableForm: FormGroup;
	previousForm: FormGroup;
	snomedConcept: SnomedDto;
	today: Moment = newMoment();
	searching = false;
	private readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;
	conceptsResultsTable: TableModel<any>;

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
			proffesional: [null]
		});

		this.billableForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [{ value: null, disabled: true }, Validators.required],
			scheme: [{ value: null, disabled: true }, Validators.required],
			dose: [{ value: null, disabled: true }, Validators.required],
			lot: [null],
			note: [null]
		});

		if (this.data?.edit) { // then load information to form fields and class attributes
			if (this.data.immunization?.billable) { // its the only case for now

				this.billableForm.controls.date.setValue(momentParseDate(this.data.immunization.administrationDate));

				this.snomedConcept = this.data.immunization.snomed;
				this.billableForm.controls.snomed.setValue(this.snomedConcept.pt);

				this.billableForm.controls.note.setValue(this.data.immunization.note);

				if (this.data.immunization.lotNumber)
					this.billableForm.controls.lot.setValue(this.data.immunization.lotNumber);

				this.vaccineService.vaccineInformation(this.snomedConcept.sctid).subscribe(
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

	public chosenYearHandler(newDate: Moment): void {
		if (this.billableForm.controls.date.value !== null) {
			const ctrlDate: Moment = this.billableForm.controls.date.value;
			ctrlDate.year(newDate.year());
			this.billableForm.controls.date.setValue(ctrlDate);
		} else {
			this.billableForm.controls.date.setValue(newDate);
		}
	}

	public chosenMonthHandler(newDate: Moment): void {
		if (this.billableForm.controls.date.value !== null) {
			const ctrlDate: Moment = this.billableForm.controls.date.value;
			ctrlDate.month(newDate.month());
			this.billableForm.controls.date.setValue(ctrlDate);
		} else {
			this.billableForm.controls.date.setValue(newDate);
		}
	}

	public submit(): void {
		if (this.billableForm.valid) {
			const appliedVaccine: ImmunizationDto = {
				snomed: this.snomedConcept,
				administrationDate: this.billableForm.value.date.format(DateFormat.API_DATE),
				billable: true,
				conditionId: this.conditions[this.billableForm.value.condition].id,
				dose: this.doses[this.billableForm.value.dose],
				lotNumber: this.billableForm.value.lot,
				note: this.billableForm.value.note,
				schemeId: this.schemes[this.billableForm.value.scheme].id
			};
			this.dialogRef.close(appliedVaccine);
		}
	}

	public onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({ term: searchValue, ecl: this.SEMANTICS_CONFIG.vaccine })
				.subscribe(
					(results: SnomedResponseDto) => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results.items);
						this.searching = false;
					},
					_ => {
						this.searching = false;
						this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
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
				}
			],
			data,
			enablePagination: true
		};
	}

	private setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.billableForm.controls.snomed.setValue(pt);
		const sctId = selectedConcept ? selectedConcept.sctid : '';
		this.loadConditions(sctId);
	}

	public resetConcept(): void {
		delete this.snomedConcept;
		delete this.conceptsResultsTable;
		delete this.conditions;
		delete this.schemes;
		delete this.doses;
		this.billableForm.get("condition").setValue(null);
		this.billableForm.get("scheme").setValue(null);
		this.billableForm.get("dose").setValue(null);
	}

	private loadConditions(sctid: string): void {
		this.vaccineService.vaccineInformation(sctid).subscribe(
			(vaccineInformation: VaccineInformationDto) => {
				this.conditions = vaccineInformation.conditions;
				this.billableForm.get("condition").enable();
				this.billableForm.get("condition").setValue(null);

				delete this.schemes;
				delete this.doses;
				this.billableForm.get("scheme").setValue(null);
				this.billableForm.get("dose").setValue(null);
			}
		);
	}

	public loadSchemes(conditionIndex: number): void {
		this.schemes = this.conditions[conditionIndex].schemes;
		this.billableForm.get("scheme").enable();
		this.billableForm.get("scheme").setValue(null);

		delete this.doses;
		this.billableForm.get("dose").setValue(null);
	}

	public loadDoses(schemeIndex: number): void {
		this.doses = this.schemes[schemeIndex].doses;
		this.billableForm.get("dose").enable();
		this.billableForm.get("dose").setValue(null);
	}

}
