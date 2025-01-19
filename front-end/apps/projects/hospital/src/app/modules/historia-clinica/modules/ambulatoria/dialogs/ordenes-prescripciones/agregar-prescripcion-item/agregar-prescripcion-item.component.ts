import { AfterContentChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppFeature, CommercialMedicationPrescriptionDto, ERole, GetCommercialMedicationSnomedDto, HCEHealthConditionDto, QuantityDto, SnomedDto, SnomedECL } from '@api-rest/api-model.d';
import { CommercialMedicationService } from '@api-rest/services/commercial-medication.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { PresentationUnitsService } from '@api-rest/services/presentation-units.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { hasError, NUMBER_PATTERN } from '@core/utils/form.utils';
import { intervalValidation } from "@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/utils/ordenesyprescrip.utils";
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnomedFinancedAuditRequired } from '@historia-clinica/modules/ambulatoria/components/generic-financed-pharmaco-search/generic-financed-pharmaco-search.component';

@Component({
  selector: 'app-agregar-prescripcion-item',
  templateUrl: './agregar-prescripcion-item.component.html',
  styleUrls: ['./agregar-prescripcion-item.component.scss']
})
export class AgregarPrescripcionItemComponent implements OnInit, AfterViewInit, AfterContentChecked {

	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
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
	isEnabledFinancedPharmaco = false;
	markFormAsTouched = false;

	private hasPrescriptorRole: boolean;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	@ViewChild('intervalHoursInput') intervalHoursInput: ElementRef;
	@ViewChild('administrationTimeDaysInput') administrationTimeDaysInput: ElementRef;

	private MIN_INPUT_LENGTH = 1;

	auditRequiredInput: string[]

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly requestMasterDataService: RequestMasterDataService,
		public dialogRef: MatDialogRef<AgregarPrescripcionItemComponent>,
		private changeDetector: ChangeDetectorRef,
		private readonly featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		private readonly commercialMedicationService: CommercialMedicationService,
		private readonly presentationUnitsService: PresentationUnitsService,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionItemData) {
			this.featureFlagService.isActive(AppFeature.HABILITAR_FINANCIACION_DE_MEDICAMENTOS).subscribe(isOn => this.isEnabledFinancedPharmaco = isOn);
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
				this.markFormAsTouched = true;
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
			quantity: {
				value: this.prescriptionItemForm.controls.quantity.value,
				unit: this.prescriptionItemForm.controls.unit.value
			},
			isDailyInterval: showDosage ? this.prescriptionItemForm.controls.interval.value === this.DEFAULT_RADIO_OPTION : null,
			intervalHours: showDosage ? this.prescriptionItemForm.controls.interval.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.intervalHours.value : null : null,
			observations: this.prescriptionItemForm.controls.observations.value,
			commercialMedicationPrescription: commercialMedicationPrescription,
			suggestedCommercialMedication: this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.value ? this.prescriptionItemForm.controls.suggestedCommercialMedication.value : null
		};
		this.dialogRef.close(newItem);
	}

	setConcept(selectedConcept: SnomedDto, commercialPt?: string): void {
		if (selectedConcept) {
			this.snomedConcept = selectedConcept;
			this.prescriptionItemForm.controls.snomed.setValue(selectedConcept);
			this.prescriptionItemForm.controls.snomed.disable();
			this.setPresentationUnits(selectedConcept?.sctid);
			this.setSuggestedCommercialMedicationOptions(commercialPt);
		}
		else {
			this.snomedConcept = undefined
			this.resetCommercialMedication();
		}
	}

	setConceptFinancedPharmaco(selectedConcept: SnomedFinancedAuditRequired, commercialPt?: string): void {
		this.auditRequiredInput = selectedConcept && selectedConcept.auditRequiredText
		this.setConcept(selectedConcept?.snomed,commercialPt)
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

	private setItemData(prescriptionItem: NewPrescriptionItem): void {
		this.prescriptionItemForm.controls.healthProblem.setValue(prescriptionItem.healthProblem?.sctId);
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
		
		this.snomedConcept = prescriptionItem.snomed;
		this.prescriptionItemForm.controls.snomed.setValue(this.snomedConcept);
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

		this.prescriptionItemForm.controls.administrationTime.setValue(this.DEFAULT_RADIO_OPTION);

		if (this.data.showDosage) {
			this.prescriptionItemForm.controls.interval.setValidators([Validators.required]);
		}

		if (this.data.showStudyCategory) {
			this.prescriptionItemForm.controls.studyCategory.setValidators([Validators.required]);
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
	hasSelectedCoverage?: boolean;
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
	unitDose?: number;
	dayDose?: number;
	quantity?: QuantityDto;
	commercialMedicationPrescription?: CommercialMedicationPrescriptionDto;
    suggestedCommercialMedication?: SnomedDto;
	frequencyType?: string
}
