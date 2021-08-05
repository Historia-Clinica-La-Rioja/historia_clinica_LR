import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { MatDialogRef } from '@angular/material/dialog';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { ImmunizationDto, SnomedDto, VaccineConditionsDto, VaccineDoseInfoDto, VaccineSchemeDto } from '@api-rest/api-model';
import { VaccineService } from '@api-rest/services/vaccine.service';


@Component({
	selector: 'app-aplicar-vacuna-2',
	templateUrl: './aplicar-vacuna-2.component.html',
	styleUrls: ['./aplicar-vacuna-2.component.scss']
})
export class AplicarVacuna2Component implements OnInit {

	doses: VaccineDoseInfoDto[];
	schemes: VaccineSchemeDto[];
	conditions: VaccineConditionsDto[];
	billableForm: FormGroup;
	previousForm: FormGroup;
	today: Moment = newMoment();
	searching = false;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;
	conceptsResultsTable: TableModel<any>;
	snomedConcept: SnomedDto;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snowstormService: SnowstormService,
		private readonly vaccineService: VaccineService,
		public dialogRef: MatDialogRef<AplicarVacuna2Component>
	) { }

	ngOnInit(): void {
		this.billableForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [null, Validators.required],
			scheme: [null, Validators.required],
			dose: [null, Validators.required],
			lot: [null],
			note: [null]
		});

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
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results.items);
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
			vaccineInformation => {
				this.conditions = vaccineInformation.conditions;
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
		this.billableForm.get("scheme").setValue(null);

		delete this.doses;
		this.billableForm.get("dose").setValue(null);
	}

	public loadDoses(schemeIndex: number): void {
		this.doses = this.schemes[schemeIndex].doses;
		this.billableForm.get("dose").setValue(null);
	}

}
