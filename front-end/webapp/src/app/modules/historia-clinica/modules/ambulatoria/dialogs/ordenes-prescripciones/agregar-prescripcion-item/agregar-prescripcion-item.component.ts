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
  selector: 'app-agregar-prescripcion-item',
  templateUrl: './agregar-prescripcion-item.component.html',
  styleUrls: ['./agregar-prescripcion-item.component.scss']
})
export class AgregarPrescripcionItemComponent implements OnInit {

	loading = false;
	searching = false;
	snomedConcept: SnomedDto;
	prescriptionItemForm: FormGroup;
	conceptsResultsTable: TableModel<any>;
	healthProblemOptions = [];
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
		public dialogRef: MatDialogRef<AgregarPrescripcionItemComponent>,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionItemData) { }

	ngOnInit(): void {
		this.formConfiguration();
	
		this.hceGeneralStateService.getActiveProblems(this.data.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			this.healthProblemOptions = activeProblems.map(problem => {return {id: problem.id, description: problem.snomed.pt}});
		});

		if (this.data.item) {
			this.setItemData(this.data.item);
		} 
	}

	getInputNumberWidth(formControl: string) {
		return this.prescriptionItemForm.controls[formControl].value ? this.prescriptionItemForm.controls[formControl].value.toString().length : this.MIN_INPUT_LENGTH;
	}

	addPrescriptionItem() {
		if(this.prescriptionItemForm.valid) {
			const {item, showDosage} = this.data;
			let newItem: NewPrescriptionItem = {
				id: item ? item.id : null,
				snomed: this.snomedConcept,
				healthProblem: {
					id: this.prescriptionItemForm.controls.healthProblem.value,
					description: this.healthProblemOptions.find(hp => hp.id === this.prescriptionItemForm.controls.healthProblem.value).description
				},
				isDailyInterval: showDosage ? this.prescriptionItemForm.controls.interval.value === this.DEFAULT_RADIO_OPTION : null,
				isChronicAdministrationTime: showDosage ? this.prescriptionItemForm.controls.administrationTime.value === this.DEFAULT_RADIO_OPTION : null,
				intervalHours: showDosage ? this.prescriptionItemForm.controls.interval.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.intervalHours.value : null : null,
				administrationTimeDays: showDosage ? this.prescriptionItemForm.controls.administrationTime.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.administrationTimeDays.value : null : null,
				observations: this.prescriptionItemForm.controls.observations.value
			}

			this.dialogRef.close(newItem);
		}
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: this.SEMANTICS_CONFIG[this.data.eclTerm]})
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

	private setItemData(prescriptionItem: NewPrescriptionItem): void {
		this.prescriptionItemForm.controls.healthProblem.setValue(prescriptionItem.healthProblem.id);
		this.prescriptionItemForm.controls.observations.setValue(prescriptionItem.observations);
		prescriptionItem.isDailyInterval ? this.prescriptionItemForm.controls.interval.setValue(this.DEFAULT_RADIO_OPTION) : this.prescriptionItemForm.controls.intervalHours.setValue(prescriptionItem.intervalHours);
		prescriptionItem.isChronicAdministrationTime ? this.prescriptionItemForm.controls.administrationTime.setValue(this.DEFAULT_RADIO_OPTION) : this.prescriptionItemForm.controls.administrationTimeDays.setValue(prescriptionItem.administrationTimeDays);

		this.snomedConcept = prescriptionItem.snomed;
		const pt = prescriptionItem.snomed ? prescriptionItem.snomed.pt : '';
		this.prescriptionItemForm.controls.snomed.setValue(pt);
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
			if(newValue !== this.DEFAULT_RADIO_OPTION) {
				this.intervalHoursInput.nativeElement.focus();
				this.prescriptionItemForm.controls.intervalHours.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			} else {
				this.prescriptionItemForm.controls.intervalHours.clearValidators();
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			}
		});

		this.prescriptionItemForm.controls.administrationTime.valueChanges.subscribe((newValue) => {
			if(newValue !== this.DEFAULT_RADIO_OPTION) {
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
	eclTerm: string;
	item?: NewPrescriptionItem;
}

export class NewPrescriptionItem {
	id: number;
	snomed: SnomedDto;
	healthProblem: {
		id: number;
		description: string;
	};
	isDailyInterval: boolean;
	isChronicAdministrationTime: boolean;
	intervalHours?: string;
	administrationTimeDays?: string;
	observations: string;
}
