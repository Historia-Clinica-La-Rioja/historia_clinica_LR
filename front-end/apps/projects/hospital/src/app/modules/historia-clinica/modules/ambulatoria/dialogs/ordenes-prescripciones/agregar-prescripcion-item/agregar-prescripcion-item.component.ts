import { AfterContentChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppFeature, CommercialMedicationPrescriptionDto, CreateOutpatientDto, ERole, GetCommercialMedicationSnomedDto, HCEHealthConditionDto, OutpatientProblemDto, QuantityDto, SnomedDto, SnomedECL } from '@api-rest/api-model.d';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { CommercialMedicationService } from '@api-rest/services/commercial-medication.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { PresentationUnitsService } from '@api-rest/services/presentation-units.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { hasError, NUMBER_PATTERN } from '@core/utils/form.utils';
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { intervalValidation } from "@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/utils/ordenesyprescrip.utils";
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-agregar-prescripcion-item',
  templateUrl: './agregar-prescripcion-item.component.html',
  styleUrls: ['./agregar-prescripcion-item.component.scss']
})
export class AgregarPrescripcionItemComponent implements OnInit, AfterViewInit, AfterContentChecked {

	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	isHabilitarRecetaDigitalFFActive: boolean = false;
	PRESCRIPTOR: ERole = ERole.PRESCRIPTOR;
	severityTypes: any[];
	reportFFIsOn: boolean;
	searchConceptsLocallyFFIsOn: boolean;
	snomedRelationsFFIsOn: boolean;
	commercialPrescriptionFFIsOn: boolean;
	snomedConcept: SnomedDto;
	prescriptionItemForm: UntypedFormGroup;
	suggestedCommercialMedicationOptions: TypeaheadOption<SnomedDto>[];
	presentationUnitsOptions: number[];
	initialSuggestCommercialMedication: TypeaheadOption<SnomedDto>;
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
		private readonly commercialMedicationService: CommercialMedicationService,
		private readonly presentationUnitsService: PresentationUnitsService,
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

						this.featureFlagService.isActive(AppFeature.HABILITAR_RELACIONES_SNOMED).subscribe(isOn => {
							this.snomedRelationsFFIsOn = isOn;
						});

						this.featureFlagService.isActive(AppFeature.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO).subscribe(isOn => {
							this.commercialPrescriptionFFIsOn = isOn;
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

		this.prescriptionItemForm.controls.pharmacoSearchType.valueChanges.subscribe(value => {
			this.snomedConcept = undefined
			this.resetCommercialMedication();

		});
	}

	resetCommercialMedication(): void {
		this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.setValue(false);
		this.prescriptionItemForm.controls.suggestedCommercialMedication.setValue(null);
		this.initialSuggestCommercialMedication = undefined;
		this.suggestedCommercialMedicationOptions = [];
		this.presentationUnitsOptions = [];
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
			startDate: problem.fechaInicio ? toApiFormat(problem.fechaInicio) : undefined,
			endDate: problem.fechaFin ? toApiFormat(problem.fechaFin) : undefined
		}];

		const createOutpatientDto: CreateOutpatientDto = {
			allergies: {
				isReferred: null,
				content: []
			},
			anthropometricData: null,
			clinicalSpecialtyId: null,
			evolutionNote: null,
			familyHistories: {
				isReferred: null,
				content: [],
			},
			medications: [],
			problems: outpatientProblemDto,
			procedures: [],
			reasons: [],
			references: [],
			riskFactors: null,
			involvedHealthcareProfessionalIds: [],
			personalHistories: {
				isReferred: null,
				content: []
			},
			completeForms: []
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

	scrollToFirstError(): void {
		const dialogContent = document.getElementById('prescription-dialog-content');
		const formControls = this.prescriptionItemForm.controls;
		if (dialogContent) {
			const invalidElements = Object.keys(formControls)
				.filter(key => formControls[key].invalid)
				.map(key => document.querySelector(`[formControlName="${key}"]`))
				.filter(element => element !== null) as HTMLElement[];
			if (invalidElements.length > 0) {
				invalidElements.sort((a, b) => a.getBoundingClientRect().top - b.getBoundingClientRect().top);
				const firstInvalidElement = invalidElements[0];
				const elementPosition = firstInvalidElement.getBoundingClientRect().top + dialogContent.scrollTop;
				const offsetPosition = elementPosition - dialogContent.getBoundingClientRect().top - 30;
				dialogContent.scrollTo({ top: offsetPosition, behavior: 'smooth' });
			}
		}
	}

	addPrescriptionItem() {
		if ( ! this.prescriptionItemForm.valid
			|| this.snomedConcept === undefined || this.snomedConcept === null || this.snomedConcept.pt === ''
			|| intervalValidation(this.prescriptionItemForm, 'intervalHours','interval')
			|| intervalValidation(this.prescriptionItemForm, 'administrationTimeDays','administrationTime')) {
				this.scrollToFirstError();
				return this.prescriptionItemForm.markAllAsTouched();
			}

		const {item, showDosage, showStudyCategory} = this.data;
		const commercialMedicationPrescription = !(this.snomedRelationsFFIsOn && this.commercialPrescriptionFFIsOn) ? null :
			{
				medicationPackQuantity: this.prescriptionItemForm.controls.medicationPackQuantity.value,
				presentationUnitQuantity: this.prescriptionItemForm.controls.presentationUnit.value
			};
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
			},
			commercialMedicationPrescription: commercialMedicationPrescription,
			suggestedCommercialMedication: this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.value ? this.prescriptionItemForm.controls.suggestedCommercialMedication.value : null
		};
		this.dialogRef.close(newItem);
	}

	setConcept(selectedConcept: SnomedDto, commercialPt?: string): void {
		if (selectedConcept) {
			this.snomedConcept = selectedConcept;
			const pt = selectedConcept ? selectedConcept.pt : '';
			this.prescriptionItemForm.controls.snomed.setValue(pt);
			this.prescriptionItemForm.controls.snomed.disable();
			this.setPresentationUnits(selectedConcept?.sctid);
			this.setSuggestedCommercialMedicationOptions(commercialPt);

			if (this.isHabilitarRecetaDigitalFFActive) {
				if (this.pharmaceuticalForm.some(value => pt?.includes(value))) {
					this.enableUnitDoseAndDayDose();
					this.prescriptionItemForm.controls.unit.setValue(this.pharmaceuticalForm.filter(value => pt.includes(value))[0]);
				} else {
					this.disableUnitDoseAndDayDose();
				}
			}
		}
		else {
			this.snomedConcept = undefined
			this.resetCommercialMedication();
		}
	}

	setCommercialMedicationConcept(selectedConcept: GetCommercialMedicationSnomedDto): void {
		this.setConcept(selectedConcept.genericMedication, selectedConcept.commercialPt);
	}

	setSuggestedCommercialMedication(snomed: SnomedDto): void {
		const conceptSctid = snomed ? snomed.sctid : this.snomedConcept?.sctid;
		this.setPresentationUnits(conceptSctid);

		this.prescriptionItemForm.controls.suggestedCommercialMedication.setValue(snomed);
		this.initialSuggestCommercialMedication = this.suggestedCommercialMedicationOptions.find(option => option.compareValue === snomed?.pt);

	}

	setSuggestedCommercialMedicationOptions(commercialPt?: string): void {
		if (this.snomedRelationsFFIsOn && this.searchConceptsLocallyFFIsOn) {
			this.commercialMedicationService.getSuggestedCommercialMedicationSnomedListByGeneric(this.snomedConcept?.sctid).subscribe(result => {
				this.suggestedCommercialMedicationOptions = result.map(snomed => ({
					value: snomed,
					compareValue: snomed.pt,
					viewValue: snomed.pt
				}));

				if (commercialPt) {
					this.initialSuggestCommercialMedication = this.suggestedCommercialMedicationOptions.find(option => option.compareValue === commercialPt);
					this.setPresentationUnits(this.initialSuggestCommercialMedication?.value.sctid);
				}

				const checkboxValue = !!(this.snomedConcept && this.suggestedCommercialMedicationOptions.find(option => option.compareValue === this.initialSuggestCommercialMedication?.compareValue))
				this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.setValue(checkboxValue);
			});
		}
	}

	setPresentationUnits(medicationSctid?: string): void {
		if (this.snomedRelationsFFIsOn && this.commercialPrescriptionFFIsOn && medicationSctid)
			this.presentationUnitsService.getMedicationPresentationUnits(medicationSctid).subscribe(result => {
				this.presentationUnitsOptions = result;

				const presentationUnit = this.prescriptionItemForm.controls.presentationUnit.value;
				if (!this.presentationUnitsOptions.includes(presentationUnit))
					this.prescriptionItemForm.controls.presentationUnit.setValue(undefined);
			})
	}

	changeCheckboxValue(event): void {
		if (!event.checked)
			this.initialSuggestCommercialMedication = undefined;
		this.setSuggestedCommercialMedication(undefined);
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
		this.snomedConcept = prescriptionItem.snomed;

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

		if (prescriptionItem.suggestedCommercialMedication) {
			const commercialMedication: GetCommercialMedicationSnomedDto = {
				commercialPt: prescriptionItem.suggestedCommercialMedication.pt,
				genericMedication: prescriptionItem.snomed
			}
			this.setCommercialMedicationConcept(commercialMedication);
		}

		if (prescriptionItem.commercialMedicationPrescription) {
			this.prescriptionItemForm.controls.medicationPackQuantity.setValue(prescriptionItem.commercialMedicationPrescription.medicationPackQuantity);
			this.prescriptionItemForm.controls.presentationUnit.setValue(prescriptionItem.commercialMedicationPrescription.presentationUnitQuantity);
		}
	}

	private formConfiguration() {
		this.prescriptionItemForm = this.formBuilder.group({
			snomed: [null, Validators.required],
			unitDose: [null],
			dayDose: [null],
			healthProblem: [null, Validators.required],
			interval: [this.DEFAULT_RADIO_OPTION],
			pharmacoSearchType: [this.DEFAULT_RADIO_OPTION],
			intervalHours: [null],
			administrationTime: [null],
			administrationTimeDays: [null],
			observations: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			studyCategory: [null],
			quantity: [null],
			unit: [null],
			isSuggestCommercialMedicationChecked: [false],
			suggestedCommercialMedication: [null],
			presentationUnit: [null],
			medicationPackQuantity: [null]
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

		if (this.snomedRelationsFFIsOn && this.commercialPrescriptionFFIsOn) {
			this.prescriptionItemForm.controls.presentationUnit.setValidators([Validators.required]);
			this.prescriptionItemForm.controls.medicationPackQuantity.setValue(this.MIN_VALUE);
			this.prescriptionItemForm.controls.medicationPackQuantity.setValidators([Validators.min(this.MIN_VALUE), Validators.pattern(NUMBER_PATTERN), Validators.required]);
		}
	}

	isValidForm() {
		return ! this.prescriptionItemForm.valid || this.snomedConcept === undefined;
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
	commercialMedicationPrescription?: CommercialMedicationPrescriptionDto;
    suggestedCommercialMedication?: SnomedDto;
}
