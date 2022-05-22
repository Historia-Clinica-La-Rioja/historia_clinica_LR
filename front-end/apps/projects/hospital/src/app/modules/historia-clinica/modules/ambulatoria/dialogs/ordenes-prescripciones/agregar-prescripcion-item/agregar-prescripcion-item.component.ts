import { Component, Inject, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HCEPersonalHistoryDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { hasError } from '@core/utils/form.utils';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { intervalValidation} from "@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/utils/ordenesyprescrip.utils";

@Component({
  selector: 'app-agregar-prescripcion-item',
  templateUrl: './agregar-prescripcion-item.component.html',
  styleUrls: ['./agregar-prescripcion-item.component.scss']
})
export class AgregarPrescripcionItemComponent implements OnInit, AfterViewInit {

	loading = false;
	searching = false;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	snomedConcept: SnomedDto;
	prescriptionItemForm: FormGroup;
	conceptsResultsTable: TableModel<any>;
	healthProblemOptions = [];
	studyCategoryOptions = [];
	DEFAULT_RADIO_OPTION = 1;
	OTHER_RADIO_OPTION = 0;
	hasError = hasError;
	intervalValidation = intervalValidation;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	@ViewChild('intervalHoursInput') intervalHoursInput: ElementRef;
	@ViewChild('administrationTimeDaysInput') administrationTimeDaysInput: ElementRef;

	private MIN_INPUT_LENGTH = 1;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly snackBarService: SnackBarService,
		public dialogRef: MatDialogRef<AgregarPrescripcionItemComponent>,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionItemData) { }

	ngOnInit(): void {
		this.formConfiguration();

		this.hceGeneralStateService.getActiveProblems(this.data.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			const activeProblemsList = activeProblems.map(problem => ({id: problem.id, description: problem.snomed.pt, sctId: problem.snomed.sctid}));

			this.hceGeneralStateService.getChronicConditions(this.data.patientId).subscribe((chronicProblems: HCEPersonalHistoryDto[]) => {
				const chronicProblemsList = chronicProblems.map(problem => ({id: problem.id, description: problem.snomed.pt,  sctId: problem.snomed.sctid}));
				this.healthProblemOptions = activeProblemsList.concat(chronicProblemsList);
				if (this.data.item) {
					this.updateSelectedHealthProblem(this.data.item.healthProblem.sctId);
				}
			});

		});

		this.requestMasterDataService.categories().subscribe(categories => {
			this.studyCategoryOptions = categories;
		});

		if (this.data.item) {
			this.setItemData(this.data.item);
		}
	}


	private updateSelectedHealthProblem(actualSctid: string): void {
		const problemExists = this.healthProblemOptions.find(hpo => hpo.sctId === actualSctid);
		this.prescriptionItemForm.controls.healthProblem.setValue(problemExists ? actualSctid : null);
	}

	ngAfterViewInit(): void {
		this.prescriptionItemForm.controls.interval.valueChanges.subscribe((newValue) => {
			if (newValue !== this.DEFAULT_RADIO_OPTION) {
				this.intervalHoursInput.nativeElement.focus();
				this.prescriptionItemForm.controls.intervalHours.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			} else {
				this.prescriptionItemForm.controls.intervalHours.clearValidators();
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			}
		});

		this.prescriptionItemForm.controls.administrationTime.valueChanges.subscribe((newValue) => {
			if (newValue !== this.DEFAULT_RADIO_OPTION) {
				this.administrationTimeDaysInput.nativeElement.focus();
				this.prescriptionItemForm.controls.administrationTimeDays.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.administrationTimeDays.updateValueAndValidity();
			} else {
				this.prescriptionItemForm.controls.administrationTimeDays.clearValidators();
				this.prescriptionItemForm.controls.administrationTimeDays.updateValueAndValidity();
			}
		});
	}

	getInputNumberWidth(formControl: string) {
		return this.prescriptionItemForm.controls[formControl].value ? this.prescriptionItemForm.controls[formControl].value.toString().length : this.MIN_INPUT_LENGTH;
	}

	addPrescriptionItem() {
		if (this.prescriptionItemForm.valid &&
			!intervalValidation(this.prescriptionItemForm, 'intervalHours','interval') &&
			!intervalValidation(this.prescriptionItemForm, 'administrationTimeDays','administrationTime')) {
			const {item, showDosage, showStudyCategory} = this.data;
			const newItem: NewPrescriptionItem = {
				id: item ? item.id : null,
				snomed: this.snomedConcept,
				healthProblem: {
					id: this.healthProblemOptions.find(hp => hp.sctId === this.prescriptionItemForm.controls.healthProblem.value).id,
					description: this.healthProblemOptions.find(hp => hp.sctId === this.prescriptionItemForm.controls.healthProblem.value).description,
					sctId: this.prescriptionItemForm.controls.healthProblem.value
				},
				studyCategory: {
					id: showStudyCategory ? this.prescriptionItemForm.controls.studyCategory.value : null,
					description: showStudyCategory ? this.studyCategoryOptions.find(sc => sc.id === this.prescriptionItemForm.controls.studyCategory.value).description : null
				},
				isDailyInterval: showDosage ? this.prescriptionItemForm.controls.interval.value === this.DEFAULT_RADIO_OPTION : null,
				isChronicAdministrationTime: showDosage ? this.prescriptionItemForm.controls.administrationTime.value === this.DEFAULT_RADIO_OPTION : null,
				intervalHours: showDosage ? this.prescriptionItemForm.controls.interval.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.intervalHours.value : null : null,
				administrationTimeDays: showDosage ? this.prescriptionItemForm.controls.administrationTime.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.administrationTimeDays.value : null : null,
				observations: this.prescriptionItemForm.controls.observations.value
			};

			this.dialogRef.close(newItem);
		}
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: this.data.eclTerm})
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

		this.prescriptionItemForm.controls.observations.setValue(prescriptionItem.observations);

		if (this.data.showDosage) {
			if (prescriptionItem.isDailyInterval) {
				this.prescriptionItemForm.controls.interval.setValue(this.DEFAULT_RADIO_OPTION);
			} else {
				this.prescriptionItemForm.controls.interval.setValue(this.OTHER_RADIO_OPTION);
				this.prescriptionItemForm.controls.intervalHours.setValue(prescriptionItem.intervalHours);
				this.prescriptionItemForm.controls.intervalHours.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.intervalHours.updateValueAndValidity();
			}

			if (prescriptionItem.isChronicAdministrationTime) {
				this.prescriptionItemForm.controls.administrationTime.setValue(this.DEFAULT_RADIO_OPTION);
			} else {
				this.prescriptionItemForm.controls.administrationTime.setValue(this.OTHER_RADIO_OPTION);
				this.prescriptionItemForm.controls.administrationTimeDays.setValue(prescriptionItem.administrationTimeDays);
				this.prescriptionItemForm.controls.administrationTimeDays.setValidators([Validators.required]);
				this.prescriptionItemForm.controls.administrationTimeDays.updateValueAndValidity();
			}
		}

		if (prescriptionItem.studyCategory?.id) {
			this.prescriptionItemForm.controls.studyCategory.setValue(prescriptionItem.studyCategory.id);
		}

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
			observations: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			studyCategory: [null],
		});

		if (this.data.showDosage) {
			this.prescriptionItemForm.controls.interval.setValidators([Validators.required]);
			this.prescriptionItemForm.controls.administrationTime.setValidators([Validators.required]);
		}

		if (this.data.showStudyCategory) {
			this.prescriptionItemForm.controls.studyCategory.setValidators([Validators.required]);
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
}

export class NewPrescriptionItemData {
	patientId: number;
	titleLabel: string;
	searchSnomedLabel: string;
	showDosage: boolean;
	showStudyCategory: boolean;
	eclTerm: SnomedECL;
	item?: NewPrescriptionItem;
}

export class NewPrescriptionItem {
	id: number;
	snomed: SnomedDto;
	healthProblem: {
		id: number;
		description: string;
		sctId?: string;
	};
	studyCategory: {
		id: string;
		description: string;
	};
	isDailyInterval: boolean;
	isChronicAdministrationTime: boolean;
	intervalHours?: string;
	administrationTimeDays?: string;
	observations: string;
}
