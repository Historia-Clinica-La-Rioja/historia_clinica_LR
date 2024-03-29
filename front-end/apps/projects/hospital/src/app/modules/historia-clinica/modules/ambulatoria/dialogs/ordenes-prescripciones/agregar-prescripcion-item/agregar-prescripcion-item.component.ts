import { AfterContentChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppFeature, CreateOutpatientDto, ERole, HCEHealthConditionDto, OutpatientProblemDto, QuantityDto, SnomedDto, SnomedECL } from '@api-rest/api-model.d';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { hasError, NUMBER_PATTERN } from '@core/utils/form.utils';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { intervalValidation } from "@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/utils/ordenesyprescrip.utils";
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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
	isHabilitarRecetaDigitalFFActive: boolean = false;
	PRESCRIPTOR: ERole = ERole.PRESCRIPTOR;
	severityTypes: any[];
	reportFFIsOn;
	searchConceptsLocallyFFIsOn;
	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	snomedConcept: SnomedDto;
	prescriptionItemForm: UntypedFormGroup;
	conceptsResultsTable: TableModel<any>;
	healthProblemOptions: HCEHealthConditionDto[] = [];
	studyCategoryOptions = [];
	DEFAULT_RADIO_OPTION = 1;
	OTHER_RADIO_OPTION = 0;
	hasError = hasError;
	intervalValidation = intervalValidation;
	MAX_VALUE: number = 99;
	MAX_QUANTITY: number = this.MAX_VALUE * this.MAX_VALUE;
	MIN_VALUE: number = 1;
	HABILITAR_RECETA_DIGITAL: AppFeature = AppFeature.HABILITAR_RECETA_DIGITAL;
	pharmaceuticalForm: string[] = ["óvulo", "cápsula", "comprimido", "supositorio"];
	enableFields: boolean = false;

	private hasPrescriptorRole: boolean;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	@ViewChild('intervalHoursInput') intervalHoursInput: ElementRef;
	@ViewChild('administrationTimeDaysInput') administrationTimeDaysInput: ElementRef;

	private MIN_INPUT_LENGTH = 1;

	constructor(
		private readonly snowstormService: SnowstormService,
		private readonly formBuilder: UntypedFormBuilder,
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
		private readonly permissionService: PermissionsService,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionItemData) {
			this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
				.subscribe((isFFActive: boolean) => {
					this.isHabilitarRecetaDigitalFFActive = isFFActive;
					if (this.isHabilitarRecetaDigitalFFActive) {
						this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(formBuilder, this.snomedService, this.snackBarService, this.snvsMasterDataService, this.dialog);

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
				});
			this.permissionService.contextAssignments$().subscribe((userRoles: ERole[]) => this.hasPrescriptorRole = anyMatch<ERole>(userRoles, [ERole.PRESCRIPTOR]));
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
	}

	getProblems() {
		if (this.hasPrescriptorRole)
			this.hceGeneralStateService.getPatientProblemsByRole(this.data.patientId)
				.subscribe((result: HCEHealthConditionDto[]) => this.healthProblemOptions = result);
		else
			this.hceGeneralStateService.getPatientProblems(this.data.patientId).subscribe((result: HCEHealthConditionDto[]) => this.healthProblemOptions = result);
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
				isFromRecetaDigital: true
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

	resetForm(): void {
		this.snomedConcept = undefined;
		this.prescriptionItemForm.reset({
			interval: this.DEFAULT_RADIO_OPTION,
			administrationTime: this.DEFAULT_RADIO_OPTION
		});
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
		if ( ! this.prescriptionItemForm.valid
			|| intervalValidation(this.prescriptionItemForm, 'intervalHours','interval')
			|| intervalValidation(this.prescriptionItemForm, 'administrationTimeDays','administrationTime')) {
				return this.prescriptionItemForm.markAllAsTouched();
			}

		const {item, showDosage, showStudyCategory} = this.data;
		const newItem: NewPrescriptionItem = {
			id: item ? item.id : null,
			snomed: this.snomedConcept,
			healthProblem: {
				id: this.healthProblemOptions.find(hpo => hpo.snomed.sctid === this.prescriptionItemForm.controls.healthProblem.value).id,
				description: this.healthProblemOptions.find(hpo => hpo.snomed.sctid === this.prescriptionItemForm.controls.healthProblem.value).snomed.pt,
				sctId: this.prescriptionItemForm.controls.healthProblem.value
			},
			administrationTimeDays: showDosage ? this.prescriptionItemForm.controls.administrationTime.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.administrationTimeDays.value : null : null,
			isChronicAdministrationTime: showDosage ? this.prescriptionItemForm.controls.administrationTime.value === this.DEFAULT_RADIO_OPTION : null,
			studyCategory: {
				id: showStudyCategory ? this.prescriptionItemForm.controls.studyCategory.value : null,
				description: showStudyCategory ? this.studyCategoryOptions.find(sc => sc.id === this.prescriptionItemForm.controls.studyCategory.value).description : null
			},
			isDailyInterval: showDosage ? this.prescriptionItemForm.controls.interval.value === this.DEFAULT_RADIO_OPTION : null,
			intervalHours: showDosage ? this.prescriptionItemForm.controls.interval.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.intervalHours.value : null : null,
			observations: this.prescriptionItemForm.controls.observations.value,
			unitDose: this.prescriptionItemForm.controls.unitDose.value,
			dayDose: this.prescriptionItemForm.controls.dayDose.value,
			quantity: {
				value: this.prescriptionItemForm.controls.quantity.value,
				unit: this.prescriptionItemForm.controls.unit.value
			}

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

		if (this.isHabilitarRecetaDigitalFFActive) {
			if (this.pharmaceuticalForm.some(value => pt?.includes(value))) {
				this.enableUnitDoseAndDayDose();
				this.prescriptionItemForm.controls.unit.setValue(this.pharmaceuticalForm.filter(value => pt.includes(value))[0]);
			} else {
				this.disableUnitDoseAndDayDose();
			}
		}
	}

	setQuantityMultiplication() {
		if (this.isHabilitarRecetaDigitalFFActive) {
			if (this.pharmaceuticalForm.some(value => this.snomedConcept?.pt.includes(value))) {
				const dayDose = this.prescriptionItemForm.controls.dayDose.value;
				const administrationTimeDays = this.prescriptionItemForm.controls.administrationTimeDays.value;
				if (dayDose && administrationTimeDays)
					this.prescriptionItemForm.controls.quantity.setValue(dayDose * administrationTimeDays)
			}
		}
	}

	private enableUnitDoseAndDayDose() {
		this.prescriptionItemForm.controls.unitDose.setValidators([Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]);
		this.prescriptionItemForm.controls.dayDose.setValidators([Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]);
		this.enableFields = true;
	}

	private disableUnitDoseAndDayDose() {
		this.prescriptionItemForm.controls.unitDose.clearValidators();
		this.prescriptionItemForm.controls.unitDose.setValue(null);
		this.prescriptionItemForm.controls.dayDose.clearValidators();
		this.prescriptionItemForm.controls.dayDose.setValue(null);
		this.enableFields = false;
	}

	private setItemData(prescriptionItem: NewPrescriptionItem): void {

		if (this.isHabilitarRecetaDigitalFFActive) {
			if (this.pharmaceuticalForm.some(value => prescriptionItem.snomed.pt.includes(value))) {
				this.enableUnitDoseAndDayDose();
				this.prescriptionItemForm.controls.unit.setValue(this.pharmaceuticalForm.filter(value => prescriptionItem.snomed.pt.includes(value))[0]);
			} else {
				this.disableUnitDoseAndDayDose();
			}
			this.prescriptionItemForm.controls.unitDose.setValue(prescriptionItem.unitDose);
			this.prescriptionItemForm.controls.dayDose.setValue(prescriptionItem.dayDose);
			this.prescriptionItemForm.controls.observations.setValue(prescriptionItem.observations);
			this.prescriptionItemForm.controls.quantity.setValue(prescriptionItem.quantity?.value);
		}

		this.prescriptionItemForm.controls.healthProblem.setValue(prescriptionItem.healthProblem?.sctId);

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
		this.prescriptionItemForm.controls.snomed.disable();
	}

	private formConfiguration() {
		this.prescriptionItemForm = this.formBuilder.group({
			snomed: [null, Validators.required],
			unitDose: [null],
			dayDose: [null],
			healthProblem: [null, Validators.required],
			interval: [this.DEFAULT_RADIO_OPTION],
			intervalHours: [null],
			administrationTime: [null],
			administrationTimeDays: [null],
			observations: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			studyCategory: [null],
			quantity: [null],
			unit: [null]
		});

		if (! this.isHabilitarRecetaDigitalFFActive)
			this.prescriptionItemForm.controls.administrationTime.setValue(this.DEFAULT_RADIO_OPTION);

		if (this.data.showDosage) {
			this.prescriptionItemForm.controls.interval.setValidators([Validators.required]);
		}

		if (this.data.showStudyCategory) {
			this.prescriptionItemForm.controls.studyCategory.setValidators([Validators.required]);
		}

		if (this.isHabilitarRecetaDigitalFFActive) {
			this.prescriptionItemForm.controls.administrationTimeDays.setValidators([Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]);
			this.prescriptionItemForm.controls.quantity.setValidators([Validators.required, Validators.pattern(NUMBER_PATTERN), Validators.max(this.MAX_QUANTITY), Validators.min(this.MIN_VALUE)]);
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
	studyCategory: {
		id: string;
		description: string;
	};
	isDailyInterval: boolean;
	isChronicAdministrationTime?: boolean;
	intervalHours?: string;
	administrationTimeDays?: string;
	observations: string;
	unitDose: number;
	dayDose: number;
	quantity: QuantityDto;
}
