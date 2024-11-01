import { ChangeDetectorRef, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AppFeature, CommercialMedicationPrescriptionDto, CreateOutpatientDto, ERole, GetCommercialMedicationSnomedDto, HCEHealthConditionDto, OutpatientProblemDto, QuantityDto, ServiceRequestCategoryDto, SharedSnomedDto, SnomedDto } from '@api-rest/api-model';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { CommercialMedicationService } from '@api-rest/services/commercial-medication.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { PresentationUnitsService } from '@api-rest/services/presentation-units.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { hasError, NUMBER_PATTERN, scrollIntoError } from '@core/utils/form.utils';
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { intervalValidation } from '../ordenes-prescripciones/utils/ordenesyprescrip.utils';
import { pharmaceuticalForm } from '../../constants/prescripciones-masterdata';
import { SnomedFinancedAuditRequired } from '../../components/generic-financed-pharmaco-search/generic-financed-pharmaco-search.component';

@Component({
	selector: 'app-add-digital-prescription-item',
	templateUrl: './add-digital-prescription-item.component.html',
	styleUrls: ['./add-digital-prescription-item.component.scss']
})
export class AddDigitalPrescriptionItemComponent implements OnInit {

	prescriptionItemForm: FormGroup<PrescriptionItemForm>;
	hasError = hasError;

	PRESCRIPTOR: ERole = ERole.PRESCRIPTOR;
	hasPrescriptorRole = false;

	healthProblems: HCEHealthConditionDto[] = [];
	DEFAULT_RADIO_OPTION = 1;
	OTHER_RADIO_OPTION = 0;
	snomedConcept: SnomedDto;
	initialSuggestCommercialMedication: TypeaheadOption<SnomedDto>;
	suggestedCommercialMedicationOptions: TypeaheadOption<SnomedDto>[];
	presentationUnitsOptions: number[];
	pharmaceuticalForm: string[] = pharmaceuticalForm;
	enableExtendedFields = false;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	severityTypes: any[];
	markFormAsTouched = false;
	studyCategoryOptions = [];
	MAX_VALUE = 99;
	MIN_VALUE = 1;
	MAX_QUANTITY: number = this.MAX_VALUE * this.MAX_VALUE;
	TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	auditRequiredInput: string[];

	HABILITAR_RELACIONES_SNOMED = false
	HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS = false;
	HABILITAR_FINANCIACION_DE_MEDICAMENTOS = false;
	HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO = false;
	HABILITAR_REPORTE_EPIDEMIOLOGICO = false;

	@ViewChild('administrationTimeDaysInput') administrationTimeDaysInput: ElementRef;

	constructor(@Inject(MAT_DIALOG_DATA) public data,
				private readonly permission: PermissionsService,
				private readonly hceGeneralState: HceGeneralStateService,
				private readonly featureFlags: FeatureFlagService,
				private readonly presentationUnits: PresentationUnitsService,
				private readonly commercialMedication: CommercialMedicationService,
				private readonly snackBar: SnackBarService,
				private readonly snvsMasterData: SnvsMasterDataService,
				private readonly dialog: MatDialog,
				private readonly outpatientConsultation: OutpatientConsultationService,
				private readonly internacionMasterData: InternacionMasterDataService,
				private readonly requestMasterData: RequestMasterDataService,
				private readonly dialogRef: MatDialogRef<AddDigitalPrescriptionItemComponent>,
				private changeDetector: ChangeDetectorRef,
				private readonly el: ElementRef,
				private readonly snomed: SnomedService) {
					this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(new FormBuilder, this.snomed, this.snackBar, this.snvsMasterData, this.dialog);
					this.setSeverityTypes();
					this.setStudyCatergories();
					this.setFeatureFlags();
				}

	ngOnInit(): void {
		this.setPrescriptionItemForm();
		this.setHasPrescriptorRole();
		this.setProblems();
		this.pharmacoSearchTypeValueChanges();

		if (this.data.item) this.setItemData(this.data.item);
	}

	ngAfterContentChecked(): void {
		this.changeDetector.detectChanges();
	}

	addNewProblem = () => {
		this.dialog.open(NewConsultationAddProblemFormComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.ambulatoryConsultationProblemsService,
				severityTypes: this.severityTypes,
				epidemiologicalReportFF: this.HABILITAR_REPORTE_EPIDEMIOLOGICO,
				searchConceptsLocallyFF: this.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS,
				isFromRecetaDigital: true
			},
		}).afterClosed().subscribe((data: AmbulatoryConsultationProblem[]) => this.addedNewProblem(data))
	}

	setCommercialMedicationConcept = (selectedConcept) => {
		this.setConcept(selectedConcept.genericMedication, selectedConcept.commercialPt);
	}

	setConceptFinancedPharmaco = (selectedConcept: SnomedFinancedAuditRequired, commercialPt?: string) => {
		this.auditRequiredInput = selectedConcept && selectedConcept.auditRequiredText
		this.setConcept(selectedConcept?.snomed,commercialPt)
	}

	clearSnomedGenericConcept = () => {
		this.snomedConcept = null;
		this.clearCommercialMedication();
	}

	setConcept = (selectedConcept: SnomedDto, commercialPt?: string) => {
		this.snomedConcept = selectedConcept;
		this.prescriptionItemForm.controls.snomed.setValue(this.snomedConcept);

		if (selectedConcept) {
			this.setPresentationUnits(selectedConcept?.sctid);
			this.setSuggestedCommercialMedicationOptions(commercialPt);

			if (this.pharmaceuticalForm.some(value => selectedConcept.pt.includes(value))) {
				this.enableUnitDoseAndDayDose();
				this.prescriptionItemForm.controls.unit.setValue(this.pharmaceuticalForm.filter(value => selectedConcept.pt.includes(value))[0]);
			} else {
				this.disableUnitDoseAndDayDose();
			}
		}

		this.clearCommercialMedication();
	}

	changeCheckboxValue = (checked: boolean) => {
		if (!checked) this.initialSuggestCommercialMedication = null;
		this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.setValue(checked);
		this.prescriptionItemForm.controls.suggestedCommercialMedication.reset();
		this.setSuggestedCommercialMedication(null);
	}

	clearCommercialMedication = () => {
		this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.reset();
		this.prescriptionItemForm.controls.suggestedCommercialMedication.reset();
		this.initialSuggestCommercialMedication = null;
		this.suggestedCommercialMedicationOptions = [];
		this.presentationUnitsOptions = [];
	}

	setSuggestedCommercialMedication = (snomed: SnomedDto) => {
		const conceptSctid = snomed ? snomed.sctid : this.snomedConcept?.sctid;
		this.setPresentationUnits(conceptSctid);

		this.prescriptionItemForm.controls.suggestedCommercialMedication.setValue(snomed);
		this.initialSuggestCommercialMedication = this.suggestedCommercialMedicationOptions.find(option => option.compareValue === snomed?.pt);

	}

	setPresentationUnits = (medicationSctid?: string) => {
		if (!this.HABILITAR_RELACIONES_SNOMED || !medicationSctid) return;
		this.presentationUnits.getMedicationPresentationUnits(medicationSctid).subscribe({
			next: (result: number[]) => {
				this.presentationUnitsOptions = result;
				const presentationUnit = this.prescriptionItemForm.controls.presentationUnit.value;
				if (!this.presentationUnitsOptions.includes(presentationUnit))
					this.prescriptionItemForm.controls.presentationUnit.reset();
			}
		});
	}

	addPrescriptionItem() {
		if (!this.isAddPrescriptionValid()) return;

		const {item, showDosage, showStudyCategory} = this.data;
		const commercialMedicationPrescription = !(this.HABILITAR_RELACIONES_SNOMED && this.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO) ? null :
			{
				medicationPackQuantity: this.prescriptionItemForm.controls.medicationPackQuantity.value,
				presentationUnitQuantity: this.prescriptionItemForm.controls.presentationUnit.value
			};
		const newItem: NewPrescriptionItem = {
			id: item ? item.id : null,
			snomed: this.snomedConcept,
			healthProblem: {
				id: this.healthProblems.find(hpo => hpo.snomed.sctid === this.prescriptionItemForm.controls.healthProblem.value).id,
				description: this.healthProblems.find(hpo => hpo.snomed.sctid === this.prescriptionItemForm.controls.healthProblem.value).snomed.pt,
				sctId: this.prescriptionItemForm.controls.healthProblem.value
			},
			administrationTimeDays: showDosage ? this.prescriptionItemForm.controls.administrationTime.value !== this.DEFAULT_RADIO_OPTION ? this.prescriptionItemForm.controls.administrationTimeDays.value : null : null,
			isChronicAdministrationTime: showDosage ? this.prescriptionItemForm.controls.administrationTime.value === this.DEFAULT_RADIO_OPTION : null,
			studyCategory: {
				id: showStudyCategory ? this.prescriptionItemForm.controls.studyCategory.value : null,
				description: showStudyCategory ? this.studyCategoryOptions.find(sc => sc.id === this.prescriptionItemForm.controls.studyCategory.value).description : null
			},
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

	setQuantityMultiplication = () => {
		if (this.pharmaceuticalForm.some(value => this.snomedConcept?.pt.includes(value))) {
			const dayDose = this.prescriptionItemForm.controls.dayDose.value;
			const administrationTimeDays = this.prescriptionItemForm.controls.administrationTimeDays.value;
			if (dayDose && administrationTimeDays)
				this.prescriptionItemForm.controls.quantity.setValue(dayDose * Number(administrationTimeDays))
		}
	}

	get isSuggestCommercialMedicationChecked(): boolean {
		return this.prescriptionItemForm.controls.isSuggestCommercialMedicationChecked.value;
	}

	get healthProblem(): string {
		return this.prescriptionItemForm.controls.healthProblem.value;
	}

	private isAddPrescriptionValid = (): boolean => {
		if (!this.prescriptionItemForm.valid
			|| !this.snomedConcept
			|| this.snomedConcept.pt === ''
			|| intervalValidation(this.prescriptionItemForm, 'administrationTimeDays', 'administrationTime'))
			{
				scrollIntoError(this.prescriptionItemForm, this.el);
				this.markFormAsTouched = true;
				this.prescriptionItemForm.markAllAsTouched();
				return false;
			}
		return true;
	}

	private setStudyCatergories = () => {
		this.requestMasterData.categories().subscribe({
			next: (result: ServiceRequestCategoryDto[]) => this.studyCategoryOptions = result
		});
	}
	private setSeverityTypes = () => {
		this.internacionMasterData.getHealthSeverity().subscribe({
			next: (healthConditionSeverities) => {
				this.severityTypes = healthConditionSeverities;
				this.ambulatoryConsultationProblemsService.setSeverityTypes(healthConditionSeverities);
			}
		})
	}

	private addedNewProblem = (data: AmbulatoryConsultationProblem[]) => {
		if (!data?.length) return;

		const problem: AmbulatoryConsultationProblem = data[data.length - 1];
		const createOutpatientDto: CreateOutpatientDto = this.createOutpatientDto(problem);
		this.outpatientConsultation.createOutpatientConsultation(createOutpatientDto, this.data.patientId).subscribe({
			next: (result: boolean) => {
				if (!result) return;
				this.prescriptionItemForm.controls.healthProblem.setValue(problem.snomed.sctid);
				this.setProblems();
			}
		})
	}

	private createOutpatientDto = (problem: AmbulatoryConsultationProblem): CreateOutpatientDto => {
		const outpatientProblemDto: OutpatientProblemDto[] = [{
			chronic: problem.cronico,
			severity: problem.codigoSeveridad,
			snomed: problem.snomed,
			startDate: problem.fechaInicio ? toApiFormat(problem.fechaInicio) : null,
			endDate: problem.fechaFin ? toApiFormat(problem.fechaFin) : null
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

	private setItemData = (prescriptionItem: NewPrescriptionItem) => {
		if (this.pharmaceuticalForm.some(value => prescriptionItem.snomed.pt?.includes(value))) {
			this.enableUnitDoseAndDayDose();
			this.prescriptionItemForm.controls.unit.setValue(this.pharmaceuticalForm.filter(value => prescriptionItem.snomed.pt.includes(value))[0]);
		} else {
			this.disableUnitDoseAndDayDose();
		}
		this.prescriptionItemForm.controls.unitDose.setValue(prescriptionItem.unitDose);
		this.prescriptionItemForm.controls.dayDose.setValue(prescriptionItem.dayDose);
		this.prescriptionItemForm.controls.observations.setValue(prescriptionItem.observations);
		this.prescriptionItemForm.controls.quantity.setValue(prescriptionItem.quantity?.value);

		this.prescriptionItemForm.controls.healthProblem.setValue(prescriptionItem.healthProblem?.sctId);

		if (prescriptionItem.studyCategory?.id)
			this.prescriptionItemForm.controls.studyCategory.setValue(prescriptionItem.studyCategory.id);

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

	private enableUnitDoseAndDayDose = () => {
		this.prescriptionItemForm.controls.unitDose.setValidators([Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]);
		this.prescriptionItemForm.controls.dayDose.setValidators([Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]);
		this.enableExtendedFields = true;
	}

	private disableUnitDoseAndDayDose = () => {
		this.prescriptionItemForm.controls.unitDose.clearValidators();
		this.prescriptionItemForm.controls.unitDose.setValue(null);
		this.prescriptionItemForm.controls.dayDose.clearValidators();
		this.prescriptionItemForm.controls.dayDose.setValue(null);
		this.enableExtendedFields = false;
	}

	private setSuggestedCommercialMedicationOptions = (commercialPt?: string) => {
		if (!(this.HABILITAR_RELACIONES_SNOMED && this.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) return;

		this.commercialMedication.getSuggestedCommercialMedicationSnomedListByGeneric(this.snomedConcept?.sctid).subscribe({
			next: (result: SharedSnomedDto[]) => {
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
			}
		});
	}

	private pharmacoSearchTypeValueChanges = () => {
		this.prescriptionItemForm.controls.pharmacoSearchType.valueChanges.subscribe(_ => {
			this.prescriptionItemForm.controls.snomed.reset();
		});
	}

	private setFeatureFlags = () => {
		this.featureFlags.isActive(AppFeature.HABILITAR_RELACIONES_SNOMED).subscribe((value: boolean) => this.HABILITAR_RELACIONES_SNOMED = value);
		this.featureFlags.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe((value: boolean) => this.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS = value);
		this.featureFlags.isActive(AppFeature.HABILITAR_FINANCIACION_DE_MEDICAMENTOS).subscribe((value: boolean) => this.HABILITAR_FINANCIACION_DE_MEDICAMENTOS = value);
		this.featureFlags.isActive(AppFeature.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO).subscribe((value: boolean) => this.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO = value);
		this.featureFlags.isActive(AppFeature.HABILITAR_REPORTE_EPIDEMIOLOGICO).subscribe((value: boolean) => this.HABILITAR_REPORTE_EPIDEMIOLOGICO = value);
	}

	private setProblems = () => {
		this.hasPrescriptorRole ? this.setProblemsByRole() : this.setProblemsWithoutRole();
	}

	private setPrescriptionItemForm = () => {
		this.prescriptionItemForm = new FormGroup<PrescriptionItemForm>({
			snomed: new FormControl(null, Validators.required),
			quantityPerTime: new FormControl(null),
			frequency: new FormControl(null),
			totalQuantity: new FormControl(null),
			healthProblem: new FormControl(null, Validators.required),
			pharmacoSearchType: new FormControl(this.DEFAULT_RADIO_OPTION),
			observations: new FormControl(null, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)),
			isSuggestCommercialMedicationChecked: new FormControl(false),
			suggestedCommercialMedication: new FormControl(null),
			presentationUnit: new FormControl(null),
			dayDose: new FormControl(null),
			unitDose: new FormControl(null),
			unit: new FormControl(null),
			medicationPackQuantity: new FormControl(null),
			studyCategory: new FormControl(null),
			administrationTime: new FormControl(this.DEFAULT_RADIO_OPTION),
			administrationTimeDays: new FormControl(null, [Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]),
			quantity: new FormControl(null, [Validators.required, Validators.pattern(NUMBER_PATTERN), Validators.max(this.MAX_QUANTITY), Validators.min(this.MIN_VALUE)]),
		});
		this.setInitialValidators();
	}

	private setInitialValidators = () => {
		if (this.data.showStudyCategory)
			this.prescriptionItemForm.controls.studyCategory.setValidators([Validators.required]);

		if (this.HABILITAR_RELACIONES_SNOMED && this.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO) {
			this.prescriptionItemForm.controls.presentationUnit.setValidators([Validators.required]);
			this.prescriptionItemForm.controls.medicationPackQuantity.setValue(this.MIN_VALUE);
			this.prescriptionItemForm.controls.medicationPackQuantity.setValidators([Validators.min(this.MIN_VALUE), Validators.pattern(NUMBER_PATTERN), Validators.required]);
		}
	}

	private setHasPrescriptorRole = () => {
		this.permission.contextAssignments$().subscribe((userRoles: ERole[]) => this.hasPrescriptorRole = anyMatch<ERole>(userRoles, [ERole.PRESCRIPTOR]));
	}

	private setProblemsByRole = () => {
		this.hceGeneralState.getPatientProblemsByRole(this.data.patientId).subscribe({
			next: ((result: HCEHealthConditionDto[]) => this.healthProblems = result)
		});
	}

	private setProblemsWithoutRole = () => {
		this.hceGeneralState.getPatientProblems(this.data.patientId).subscribe({
			next: ((result: HCEHealthConditionDto[]) => this.healthProblems = result)
		});
	}

}

interface PrescriptionItemForm {
	snomed: FormControl<SnomedDto>,
	quantityPerTime: FormControl<number>,
	frequency: FormControl<number>,
	totalQuantity: FormControl<QuantityDto>,
	healthProblem: FormControl<string>,
	pharmacoSearchType: FormControl<number>,
	observations: FormControl<string>,
	isSuggestCommercialMedicationChecked: FormControl<boolean>
	suggestedCommercialMedication: FormControl<SnomedDto>
	presentationUnit: FormControl<number>
	unitDose: FormControl<number>,
	dayDose: FormControl<number>,
	unit: FormControl<string>,
	medicationPackQuantity: FormControl<number>,
	studyCategory: FormControl<string>,
	administrationTime: FormControl<number>
	administrationTimeDays: FormControl<string>
	quantity: FormControl<number>,
}

interface NewPrescriptionItem {
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
	isChronicAdministrationTime?: boolean;
	observations: string;
	administrationTimeDays?: string;
	unitDose: number;
	dayDose: number;
	quantity: QuantityDto;
	commercialMedicationPrescription?: CommercialMedicationPrescriptionDto;
    suggestedCommercialMedication?: SnomedDto;
}
