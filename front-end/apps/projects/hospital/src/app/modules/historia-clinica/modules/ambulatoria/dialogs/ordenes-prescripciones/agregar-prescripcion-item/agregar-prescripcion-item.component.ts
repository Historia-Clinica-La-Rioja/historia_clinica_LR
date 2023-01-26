import { Component, Inject, OnInit, ViewChild, ElementRef, AfterViewInit, AfterContentChecked, ChangeDetectorRef } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppFeature, CreateOutpatientDto, HCEPersonalHistoryDto, OutpatientProblemDto, SnomedDto, SnomedECL } from '@api-rest/api-model.d';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { hasError } from '@core/utils/form.utils';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { intervalValidation} from "@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/utils/ordenesyprescrip.utils";
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';

@Component({
  selector: 'app-agregar-prescripcion-item',
  templateUrl: './agregar-prescripcion-item.component.html',
  styleUrls: ['./agregar-prescripcion-item.component.scss']
})
export class AgregarPrescripcionItemComponent implements OnInit, AfterViewInit, AfterContentChecked {

	loading = false;
	searching = false;
	conceptsView: boolean = false;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	severityTypes: any[];
	reportFFIsOn;
	searchConceptsLocallyFFIsOn;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	snomedConcept: SnomedDto;
	prescriptionItemForm: FormGroup;
	conceptsResultsTable: TableModel<any>;
	healthProblemOptions = [];
	studyCategoryOptions = [];
	DEFAULT_RADIO_OPTION = 1;
	OTHER_RADIO_OPTION = 0;
	MONTHS_QUANTITY: number[] = [1, 2, 3, 4];
	hasError = hasError;
	intervalValidation = intervalValidation;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	@ViewChild('intervalHoursInput') intervalHoursInput: ElementRef;

	private MIN_INPUT_LENGTH = 1;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly snackBarService: SnackBarService,
		public dialogRef: MatDialogRef<AgregarPrescripcionItemComponent>,
		private changeDetector: ChangeDetectorRef,
		private readonly featureFlagService: FeatureFlagService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly snomedService: SnomedService,
		private readonly snvsMasterDataService: SnvsMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionItemData) {
			this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(formBuilder, this.snomedService, this.snackBarService, this.snvsMasterDataService, this.dialog);
		}

	ngOnInit(): void {
		this.formConfiguration();

		this.getProblems();

		this.requestMasterDataService.categories().subscribe(categories => {
			this.studyCategoryOptions = categories;
		});

		if (this.data.item) {
			this.setItemData(this.data.item);
		}

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.ambulatoryConsultationProblemsService.setSeverityTypes(healthConditionSeverities);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_REPORTE_EPIDEMIOLOGICO).subscribe(isOn => {
			this.reportFFIsOn = isOn;
			this.ambulatoryConsultationProblemsService.setReportFF(isOn);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
			this.ambulatoryConsultationProblemsService.setSearchConceptsLocallyFF(isOn);
		});
	}

	getProblems() {
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
	}

	ngAfterContentChecked(): void {
		this.changeDetector.detectChanges();
	}

	addNewProblem() {
		this.dialog.open(NewConsultationAddProblemFormComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.ambulatoryConsultationProblemsService,
				severityTypes: this.severityTypes,
				epidemiologicalReportFF: this.reportFFIsOn,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
		}).afterClosed().subscribe((data: AmbulatoryConsultationProblem[]) => {
			if (data === undefined || data.length === 0) return;

			// El ultimo que se agregó
			const problem: AmbulatoryConsultationProblem = data[data.length - 1];
			const createOutpatientDto: CreateOutpatientDto = this.createOutpatientDto(problem);
			this.outpatientConsultationService.createOutpatientConsultation(createOutpatientDto, this.data.patientId)
			.subscribe((result: boolean) => {
				if (! result) return;

					this.prescriptionItemForm.controls.healthProblem.setValue(problem.snomed.sctid);
					this.getProblems();
				});
		})
	}

	private createOutpatientDto(problem: AmbulatoryConsultationProblem): CreateOutpatientDto {
		const outpatientProblemDto: OutpatientProblemDto[] = [{
			chronic: problem.cronico,
			severity: problem.codigoSeveridad,
			snomed: problem.snomed,
			startDate: problem.fechaInicio ? momentFormat(problem.fechaInicio, DateFormat.API_DATE) : undefined,
			endDate: problem.fechaFin ? momentFormat(problem.fechaFin, DateFormat.API_DATE) : undefined
		}];

		const createOutpatientDto: CreateOutpatientDto = {
			allergies: [],
			anthropometricData: null,
			clinicalSpecialtyId: null,
			evolutionNote: null,
			familyHistories: [],
			medications: [],
			problems: outpatientProblemDto,
			procedures: [],
			reasons: [],
			references: [],
			riskFactors: null
		};
		return createOutpatientDto;
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
	}

	getInputNumberWidth(formControl: string) {
		return this.prescriptionItemForm.controls[formControl].value ? this.prescriptionItemForm.controls[formControl].value.toString().length : this.MIN_INPUT_LENGTH;
	}

	addPrescriptionItem() {
		if ( ! this.prescriptionItemForm.valid) return;

		const {item, showDosage, showStudyCategory} = this.data;
		const newItem: NewPrescriptionItem = {
			id: item ? item.id : null,
			snomed: this.snomedConcept,
			healthProblem: {
				id: this.healthProblemOptions.find(hp => hp.sctId === this.prescriptionItemForm.controls.healthProblem.value).id,
				description: this.healthProblemOptions.find(hp => hp.sctId === this.prescriptionItemForm.controls.healthProblem.value).description,
				sctId: this.prescriptionItemForm.controls.healthProblem.value
			},
			unitDose: this.prescriptionItemForm.controls.unitDose.value,
			dayDose: this.prescriptionItemForm.controls.dayDose.value,
			treatmentDays: this.prescriptionItemForm.controls.treatmentDays.value,
			posdatadas: this.prescriptionItemForm.controls.posdatadas.value,
			studyCategory: {
				id: showStudyCategory ? this.prescriptionItemForm.controls.studyCategory.value : null,
				description: showStudyCategory ? this.studyCategoryOptions.find(sc => sc.id === this.prescriptionItemForm.controls.studyCategory.value).description : null
			},
			isDailyInterval: showDosage ? this.prescriptionItemForm.controls.interval.value === this.DEFAULT_RADIO_OPTION : null,
			intervalHours: showDosage ? this.prescriptionItemForm.controls.interval.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.intervalHours.value : null : null,
			observations: this.prescriptionItemForm.controls.observations.value
		};

		this.dialogRef.close(newItem);
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
		this.conceptsView = ! this.conceptsView;
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.prescriptionItemForm.controls.snomed.setValue(pt);
		this.prescriptionItemForm.controls.snomed.disable();
	}

	private setItemData(prescriptionItem: NewPrescriptionItem): void {

		this.prescriptionItemForm.controls.unitDose.setValue(prescriptionItem.unitDose);
		this.prescriptionItemForm.controls.dayDose.setValue(prescriptionItem.dayDose);
		this.prescriptionItemForm.controls.treatmentDays.setValue(prescriptionItem.treatmentDays);
		this.prescriptionItemForm.controls.posdatadas.setValue(prescriptionItem.posdatadas);
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
		}

		if (prescriptionItem.studyCategory?.id) {
			this.prescriptionItemForm.controls.studyCategory.setValue(prescriptionItem.studyCategory.id);
		}

		this.snomedConcept = prescriptionItem.snomed;
		const pt = prescriptionItem.snomed ? prescriptionItem.snomed.pt : '';
		this.prescriptionItemForm.controls.snomed.setValue(pt);
		this.prescriptionItemForm.controls.snomed.disable();
	}

	private  formConfiguration() {
		this.prescriptionItemForm = this.formBuilder.group({
			snomed: [null, Validators.required],
			unitDose: [null, Validators.required],
			dayDose: [null, Validators.required],
			treatmentDays: [null, Validators.required],
			healthProblem: [null, Validators.required],
			posdatadas: [this.MONTHS_QUANTITY[0], Validators.required],
			interval: [this.DEFAULT_RADIO_OPTION],
			intervalHours: [null],
			observations: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			studyCategory: [null],
		});

		if (this.data.showDosage) {
			this.prescriptionItemForm.controls.interval.setValidators([Validators.required]);
		}

		if (this.data.showStudyCategory) {
			this.prescriptionItemForm.controls.studyCategory.setValidators([Validators.required]);
		}
	}

	isValidForm() {
		return ! this.prescriptionItemForm.valid || this.snomedConcept === undefined;
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
	unitDose: number;
	dayDose: number;
	treatmentDays: number;
	posdatadas: number
	studyCategory: {
		id: string;
		description: string;
	};
	isDailyInterval: boolean;
	isChronicAdministrationTime?: boolean;
	intervalHours?: string;
	administrationTimeDays?: string;
	observations: string;
}
