import { Component, Inject, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HCEPersonalHistoryDto, SnomedDto } from '@api-rest/api-model';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SEMANTICS_CONFIG } from 'src/app/modules/historia-clinica/constants/snomed-semantics';
import { hasError } from '@core/utils/form.utils';

@Component({
  selector: 'app-agregar-preinscripcion-item',
  templateUrl: './agregar-preinscripcion-item.component.html',
  styleUrls: ['./agregar-preinscripcion-item.component.scss']
})
export class AgregarPreinscripcionItemComponent implements OnInit {

	loading = false;
	searching = false;
	snomedConcept: SnomedDto;
	prescriptionItemForm: FormGroup;
	conceptsResultsTable: TableModel<any>;
	healthProblemOptions;
	DEFAULT_RADIO_OPTION = 1;
	OTHER_RADIO_OPTION = 0;
	hasError = hasError;

	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	@ViewChild("intervalHoursInput") intervalHoursInput: ElementRef;
	@ViewChild("administrationTimeDaysInput") administrationTimeDaysInput: ElementRef;

	private MIN_INPUT_LENGTH = 1;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		public dialogRef: MatDialogRef<AgregarPreinscripcionItemComponent>,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionItemData) { }

	ngOnInit(): void {
		this.formConfiguration();
	
		this.hceGeneralStateService.getActiveProblems(this.data.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			this.healthProblemOptions = activeProblems.map(problem => {return {id: problem.id, description: problem.snomed.pt}});
		});
	}

	dynamicInputNumberWidth(formControl: string) {
		return this.prescriptionItemForm.controls[formControl].value ? this.prescriptionItemForm.controls[formControl].value.toString().length : this.MIN_INPUT_LENGTH;
	}

	addPreinscripcionItem() {
		if(this.prescriptionItemForm.valid) {
			let newItem: NewPrescriptionItem = {
				snomed: this.snomedConcept.id,
				healthProblem: this.prescriptionItemForm.controls.healthProblem.value,
				interval: null,
				administrationTime: null,
				observations: this.prescriptionItemForm.controls.observations.value
			}

			this.dialogRef.close(newItem);
		}
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: this.SEMANTICS_CONFIG.medicine})
				.subscribe(
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results.items);
						this.searching = false;
					}
				);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.prescriptionItemForm.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		this.snomedConcept = undefined;
		this.prescriptionItemForm.reset({
			interval: this.DEFAULT_RADIO_OPTION,
			administrationTime: this.DEFAULT_RADIO_OPTION
		});
	}

	private formConfiguration() {
		this.prescriptionItemForm = this.formBuilder.group({
			snomed: [null, Validators.required],
			healthProblem: [null, Validators.required],
			interval: [this.DEFAULT_RADIO_OPTION],
			intervalHours: [null],
			administrationTime: [this.DEFAULT_RADIO_OPTION],
			administrationTimeDays: [null],
			observations: [null],
		});

		if (this.data.showDosage) {
			this.prescriptionItemForm.controls.interval.setValidators([Validators.required]);
			this.prescriptionItemForm.controls.administrationTime.setValidators([Validators.required]);
		}

		this.prescriptionItemForm.controls.interval.valueChanges.subscribe((newValue) => {
			if(newValue === this.OTHER_RADIO_OPTION) {
				this.intervalHoursInput.nativeElement.focus();
				this.prescriptionItemForm.controls.intervalHours.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			} else {
				this.prescriptionItemForm.controls.intervalHours.clearValidators();
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			}
		});

		this.prescriptionItemForm.controls.administrationTime.valueChanges.subscribe((newValue) => {
			if(newValue === this.OTHER_RADIO_OPTION) {
				this.administrationTimeDaysInput.nativeElement.focus();
				this.prescriptionItemForm.controls.administrationTimeDays.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.administrationTimeDays.updateValueAndValidity();
			} else {
				this.prescriptionItemForm.controls.administrationTimeDays.clearValidators();
				this.prescriptionItemForm.controls.administrationTimeDays.updateValueAndValidity();
			}
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
}

export class NewPrescriptionItemData {
	patientId: number;
	titleLabel: string;
	searchSnomedLabel: string;
	showDosage: boolean;
}

export class NewPrescriptionItem {
	snomed: string;
	healthProblem: number;
	interval: string;
	administrationTime: string;
	observations: string;
}
