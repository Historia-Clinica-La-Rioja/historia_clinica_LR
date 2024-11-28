import { ChangeDetectorRef, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
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
import { anyMatch, getElementAtPosition } from '@core/utils/array.utils';
import { hasError, NUMBER_PATTERN, scrollIntoError } from '@core/utils/form.utils';
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { intervalValidation } from '../ordenes-prescripciones/utils/ordenesyprescrip.utils';
import { SnomedFinancedAuditRequired } from '../../components/generic-financed-pharmaco-search/generic-financed-pharmaco-search.component';
import { AugmentativeMedicationPresentation, getBasicPresentation, getPresentationGroup, PresentationGroup } from '../../constants/medication-presentation';
import { combineLatest, startWith, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { DigitalPrescriptionMapperService } from '../../services/digital-prescription-mapper.service';

const DAY_HS = 24;

@Component({
	selector: 'app-add-digital-prescription-item',
	templateUrl: './add-digital-prescription-item.component.html',
	styleUrls: ['./add-digital-prescription-item.component.scss']
})
export class AddDigitalPrescriptionItemComponent implements OnInit, OnDestroy {

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
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	severityTypes: any[];
	markFormAsTouched = false;
	studyCategoryOptions = [];
	MAX_VALUE = 99;
	MIN_VALUE = 1;
	MAX_QUANTITY: number = this.MAX_VALUE * this.MAX_VALUE;
	TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	auditRequiredInput: string[];
	medicationPresentations: string[] = [];
	currentPresentation: string;
	showMedicationPackQuantity = false;
	showPresentationQuantity = false;
	quantitySelectorOptions: string[] = [];
	canDoMultiplicationFields = false;
	frequencySelectorOptions: string[] = [];
	quantityFrequencyChanges: Subscription;

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
				private readonly translate: TranslateService,
				private readonly digitalPrescriptionMapper: DigitalPrescriptionMapperService,
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
		this.setFrequencySelectorOptions();

		if (this.data.item) this.setItemData(this.data.item);
	}

	ngOnDestroy(): void {
		this.quantityFrequencyChanges?.unsubscribe();
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
		this.reset();

		if (selectedConcept) {
			this.setPresentationUnits(selectedConcept?.sctid);
			this.setSuggestedCommercialMedicationOptions(commercialPt);
			this.setMedicationPresentations(selectedConcept.sctid);
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
				const presentationUnit = this.prescriptionItemForm.value.presentationUnit;
				if (!this.presentationUnitsOptions.includes(presentationUnit))
					this.prescriptionItemForm.controls.presentationUnit.reset();
			}
		});
	}

	addPrescriptionItem() {
		if (!this.isAddPrescriptionValid()) return;

		const {item, showStudyCategory} = this.data;
		const commercialMedicationPrescription = this.showMedicationPackQuantity || this.showPresentationQuantity ? {
			medicationPackQuantity: this.showMedicationPackQuantity ? this.prescriptionItemForm.value.medicationPackQuantity : 0,
			presentationUnitQuantity: this.showPresentationQuantity ? this.prescriptionItemForm.value.presentationUnit : 0
		}: null;
		const formValues = this.prescriptionItemForm.value;
		const isDailyInterval = formValues.frequencyType === this.getFrequencyDayTranslate();
		const newItem: NewPrescriptionItem = this.digitalPrescriptionMapper.toNewPrescriptionItem(
			item,
			showStudyCategory,
			this.snomedConcept,
			this.healthProblems,
			formValues,
			commercialMedicationPrescription,
			this.studyCategoryOptions,
			isDailyInterval
		);
		this.dialogRef.close(newItem);
	}

	setTotalQuantityAvailability = (value: string[]) => {
		this.prescriptionItemForm.controls.totalQuantity.enable();

		const presentation = getElementAtPosition(value, 0);

		const isPresentationGroupOne = PresentationGroup.GROUP_ONE === getPresentationGroup(presentation);
		const isPresentationGroupTwo = PresentationGroup.GROUP_TWO === getPresentationGroup(presentation);
		const isPresentationGroupThree = PresentationGroup.GROUP_THREE === getPresentationGroup(presentation);
		const isUnitGotas = this.prescriptionItemForm.value.unit === AugmentativeMedicationPresentation.GOTAS;

		if (isPresentationGroupThree || isPresentationGroupTwo && isUnitGotas) {
			this.canDoMultiplicationFields = false;
			this.prescriptionItemForm.controls.totalQuantity.setValue(null);
			return this.prescriptionItemForm.controls.totalQuantity.disable();
		} else if (isPresentationGroupOne || isPresentationGroupTwo) {
			this.canDoMultiplicationFields = true;
			this.detectFieldChanges();
		}
	}

	get isSuggestCommercialMedicationChecked(): boolean {
		return this.prescriptionItemForm.value.isSuggestCommercialMedicationChecked;
	}

	get healthProblem(): string {
		return this.prescriptionItemForm.value.healthProblem;
	}

	private setTotalQuantityMultiplication = (quantity: number, administrationTimeDays: string, frequency: number, frequencyType: string) => {
		if (!this.canDoMultiplicationFields) return;
		if (!frequency || !quantity || !administrationTimeDays) return;

		const multiplication = Math.floor(this.calculateFrequency(frequency, quantity, Number(administrationTimeDays), frequencyType));
		this.prescriptionItemForm.controls.totalQuantity.setValue(multiplication);
	}

	private calculateFrequency = (frequency: number, quantity: number, administrationTimeDays: number, frequencyType: string): number => {
		if (frequencyType === this.getFrequencyDayTranslate())
			return (quantity * administrationTimeDays) / frequency;
		return (DAY_HS/frequency) * quantity * administrationTimeDays
	}

	private reset = () => {
		this.canDoMultiplicationFields = false;
		this.prescriptionItemForm.controls.totalQuantity.setValidators(this.getTotalQuantityValidators());
		this.quantitySelectorOptions = [];
		this.showMedicationPackQuantity = false;
		this.showPresentationQuantity = false;
		this.prescriptionItemForm.controls.totalQuantity.setValue(null);
		this.prescriptionItemForm.controls.totalQuantity.enable();
	}

	private detectFieldChanges = () => {
		this.quantityFrequencyChanges = combineLatest([
			this.prescriptionItemForm.controls.quantity.valueChanges.pipe(
				startWith(this.prescriptionItemForm.controls.quantity.value)
			),
			this.prescriptionItemForm.controls.administrationTimeDays.valueChanges.pipe(
				startWith(this.prescriptionItemForm.controls.administrationTimeDays.value)
			),
			this.prescriptionItemForm.controls.frequency.valueChanges.pipe(
				startWith(this.prescriptionItemForm.controls.frequency.value)
			),
			this.prescriptionItemForm.controls.frequencyType.valueChanges.pipe(
				startWith(this.prescriptionItemForm.controls.frequencyType.value)
			)
		]).subscribe(([quantity, administrationTimeDays, frequency, frequencyType]) => this.setTotalQuantityMultiplication(quantity, administrationTimeDays, frequency, frequencyType));
	};

	private enableTotalQuantity = (value: string[]) => {
		const presentation = getElementAtPosition(value, 0);
		if (PresentationGroup.GROUP_ONE === getPresentationGroup(presentation)) {
			this.canDoMultiplicationFields = true;
			this.detectFieldChanges();
		} else {
			this.prescriptionItemForm.controls.totalQuantity.enable();
			this.prescriptionItemForm.controls.totalQuantity.clearValidators();
		}
	}

	private getTotalQuantityValidators = (): ValidatorFn[] => {
		return [Validators.required, Validators.pattern(NUMBER_PATTERN), Validators.max(this.MAX_QUANTITY), Validators.min(this.MIN_VALUE)];
	}

	private setMedicationPresentations = (sctid: string) => {
		(this.HABILITAR_RELACIONES_SNOMED)
			? this.setMedicationPresentationByGenericSctid(sctid)
			: this.setBasicMedicationPresentation();

	}

	private setMedicationPresentationByGenericSctid = (sctid: string) => {
		this.presentationUnits.getMedicationPresentationByGenericSctid(sctid).subscribe({
			next: (value: string[]) => {
				this.medicationPresentations = value;
				this.currentPresentation = getElementAtPosition(this.medicationPresentations, 0);
				this.setShowMedicationPackQuantity(value);
				this.setShowPresentationQuantity();
				this.setQuantitySelector(value);
				this.enableTotalQuantity(value);
				this.setTotalQuantityAvailability(value);
			}
		});
	}

	private setBasicMedicationPresentation = () => {
		const presentation = [getBasicPresentation(this.snomedConcept.pt)];
		if (presentation) {
			this.currentPresentation = getElementAtPosition(presentation, 0);
			this.setQuantitySelector(presentation);
			this.enableTotalQuantity(presentation);
			this.setTotalQuantityAvailability(presentation);
		}
	}

	private setQuantitySelector = (value: string[]) => {
		let presentation = getElementAtPosition(value, 1) || getElementAtPosition(value, 0);
		if (value.includes(AugmentativeMedicationPresentation.GOTAS))
			presentation = getElementAtPosition(value, 0) || getElementAtPosition(value, 1);

		if (this.data.item && value.includes(this.data.item.quantity?.unit))
			presentation = this.data.item.quantity?.unit;

		this.quantitySelectorOptions = value;
		this.prescriptionItemForm.controls.unit.setValue(presentation);
	}

	private setShowMedicationPackQuantity = (value: string[]) => {
		const presentation = getElementAtPosition(value, 0);
		this.showMedicationPackQuantity =
			this.canShowPackQuantityAndPresentationQuantity()
			|| PresentationGroup.GROUP_ONE !== getPresentationGroup(presentation);
	}

	private setShowPresentationQuantity = () => {
		this.showPresentationQuantity = this.canShowPackQuantityAndPresentationQuantity()
	}

	private canShowPackQuantityAndPresentationQuantity = (): boolean => {
		return (this.HABILITAR_RELACIONES_SNOMED && (this.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO || this.data.hasSelectedCoverage));
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
		this.prescriptionItemForm.controls.quantity.setValue(prescriptionItem.unitDose);
		this.prescriptionItemForm.controls.frequency.setValue(prescriptionItem.dayDose);
		this.prescriptionItemForm.controls.observations.setValue(prescriptionItem.observations);
		this.prescriptionItemForm.controls.administrationTimeDays.setValue(prescriptionItem.administrationTimeDays);
		this.prescriptionItemForm.controls.totalQuantity.setValue(prescriptionItem.quantity?.value);
		this.prescriptionItemForm.controls.healthProblem.setValue(prescriptionItem.healthProblem?.sctId);
		this.prescriptionItemForm.controls.unit.setValue(prescriptionItem.quantity?.unit);
		this.prescriptionItemForm.controls.frequencyType.setValue(prescriptionItem.frequencyType || this.getFrequencyHsTranslate());

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
			healthProblem: new FormControl(null, Validators.required),
			pharmacoSearchType: new FormControl(this.DEFAULT_RADIO_OPTION),
			observations: new FormControl(null, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)),
			isSuggestCommercialMedicationChecked: new FormControl(false),
			suggestedCommercialMedication: new FormControl(null),
			presentationUnit: new FormControl(null),
			frequency: new FormControl(null, [Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]),
			quantity: new FormControl(null, [Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]),
			unit: new FormControl(null),
			medicationPackQuantity: new FormControl(null, Validators.pattern(NUMBER_PATTERN)),
			studyCategory: new FormControl(null),
			administrationTime: new FormControl(this.DEFAULT_RADIO_OPTION),
			administrationTimeDays: new FormControl(null, [Validators.required, Validators.max(this.MAX_VALUE), Validators.min(this.MIN_VALUE)]),
			totalQuantity: new FormControl(null, this.getTotalQuantityValidators()),
			frequencyType: new FormControl(this.getFrequencyHsTranslate())
		});
		this.setInitialValidators();
	}

	private setFrequencySelectorOptions = () => {
		this.frequencySelectorOptions = [this.getFrequencyHsTranslate(), this.getFrequencyDayTranslate()];
	}

	private getFrequencyDayTranslate = (): string => {
		return this.translate.instant('ambulatoria.paciente.ordenes_prescripciones.add_digital_prescription_item_dialog.frequency_type.DAY');
	}

	private getFrequencyHsTranslate = (): string => {
		return this.translate.instant('ambulatoria.paciente.ordenes_prescripciones.add_digital_prescription_item_dialog.frequency_type.HS');
	}

	private setInitialValidators = () => {
		if (this.data.showStudyCategory)
			this.prescriptionItemForm.controls.studyCategory.setValidators([Validators.required]);

		if (this.HABILITAR_RELACIONES_SNOMED && this.HABILITAR_PRESCRIPCION_COMERCIAL_EN_DESARROLLO) {
			this.prescriptionItemForm.controls.presentationUnit.setValidators([Validators.required]);
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
	healthProblem: FormControl<string>,
	pharmacoSearchType: FormControl<number>,
	observations: FormControl<string>,
	isSuggestCommercialMedicationChecked: FormControl<boolean>
	suggestedCommercialMedication: FormControl<SnomedDto>
	presentationUnit: FormControl<number>
	quantity: FormControl<number>,
	frequency: FormControl<number>,
	unit: FormControl<string>,
	medicationPackQuantity: FormControl<number>,
	studyCategory: FormControl<string>,
	administrationTime: FormControl<number>
	administrationTimeDays: FormControl<string>
	totalQuantity: FormControl<number>,
	frequencyType: FormControl<string>
}

export interface NewPrescriptionItem {
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
	observations: string;
	administrationTimeDays?: string;
	unitDose: number;
	dayDose: number;
	quantity: QuantityDto;
	commercialMedicationPrescription?: CommercialMedicationPrescriptionDto;
    suggestedCommercialMedication?: SnomedDto;
	frequencyType: string
}
