import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormGroup, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import {
	APatientDto,
	ApiErrorDto,
	AppFeature,
	BMPersonDto,
	BasicPatientDto,
	ClinicalSpecialtyDto,
	DocumentRequestDto,
	PrescriptionDto,
	SnomedDto
} from '@api-rest/api-model.d';
import { PatientService } from '@api-rest/services/patient.service';

import { AgregarPrescripcionItemComponent, NewPrescriptionItem } from '../../../../dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../../services/prescripciones.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import {hasError, NUMBER_PATTERN, scrollIntoError} from '@core/utils/form.utils';
import { PharmacosFrequentComponent } from '../pharmacos-frequent/pharmacos-frequent.component';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { MatStepper } from '@angular/material/stepper';
import { mapToAPatientDto, mapToNewPrescriptionItem } from '../../utils/prescripcion-mapper';

@Component({
	selector: 'app-nueva-prescripcion',
	templateUrl: './nueva-prescripcion.component.html',
	styleUrls: ['./nueva-prescripcion.component.scss']
})
export class NuevaPrescripcionComponent implements OnInit {

	@ViewChild('dialog') private dialogScroll: ElementRef;

	indexStep = Steps;
	initialIndex = this.indexStep.PATIENT;
	editableStepModality = true;
	patientData: BasicPatientDto;
	person: BMPersonDto;

	prescriptionItems: NewPrescriptionItem[];
	prescriptionForm: UntypedFormGroup;
	specialties: ClinicalSpecialtyDto[];
	itemCount = 0;
	isHabilitarRecetaDigitalEnabled: boolean = false;
	POSDATADAS_DEFAULT = 0;
	POSDATADAS_MIN = 1;
	POSDATADAS_MAX = 11;
	hasError = hasError;
	maxPhonePrefix: number = 10;
	maxPhoneNumber: number = 15;
	submitted: boolean = false;
	showAddMedicationError: boolean = false;
	isAddMedicationLoading = false;
	isFinishPrescripcionLoading = false;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly patientService: PatientService,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<NuevaPrescripcionComponent>,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly medicationRequestService: MedicationRequestService,
		private readonly el: ElementRef,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionData) {
			this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
				.subscribe((result: boolean) => this.isHabilitarRecetaDigitalEnabled = result);
		}

	ngOnInit(): void {
		this.setProfessionalSpecialties();
		this.formConfiguration();

		this.patientService.getPatientBasicData(Number(this.data.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});

		this.prescriptionItems = this.data.prescriptionItemList ? this.data.prescriptionItemList : [];
		
	}

	private formConfiguration() {
		this.prescriptionForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			withoutRecipe: [false],
			evolucion: [],
			clinicalSpecialty: [null, [Validators.required]],
			prolongedTreatment: [false],
			posdatadas: [{value: this.POSDATADAS_DEFAULT, disabled: true}, [Validators.min(this.POSDATADAS_MIN), Validators.max(this.POSDATADAS_MAX)]],
			archived: [false],
			patientData: this.setPatientDataGroup(),
		});
	}

	private setPatientDataGroup(): FormGroup | null {
		return (this.isHabilitarRecetaDigitalEnabled)
			? this.formBuilder.group({
				phonePrefix: [null, [Validators.required, Validators.maxLength(this.maxPhonePrefix), Validators.pattern(NUMBER_PATTERN)]],
				country: [{value: null, disabled: true}, [Validators.required]],
				phoneNumber: [null, [Validators.required, Validators.maxLength(this.maxPhoneNumber), Validators.pattern(NUMBER_PATTERN)]],
				province: [null, Validators.required],
				locality: [null, Validators.required],
				city: [null, Validators.required],
				street: [null, Validators.required],
				streetNumber: [null, Validators.required]
			})
			: null;
	}

	setProlongedTreatment(isOn: boolean) {
		this.prescriptionForm.controls.prolongedTreatment.setValue(isOn);
		const posdatadas = this.prescriptionForm.controls.posdatadas;
		if (isOn) {
			posdatadas.enable()
			posdatadas.addValidators(Validators.required);
			this.prescriptionForm.controls.posdatadas.setValue(1);
		} else {
			this.prescriptionForm.controls.posdatadas.setValue(0);
			this.disablePosdatadas(posdatadas)
		}
	}

	disablePosdatadas(posdatadas: AbstractControl) {
		posdatadas.disable();
		posdatadas.setValue(this.POSDATADAS_DEFAULT);
	}

	setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties);
		});
	}

	setPerson(person: BMPersonDto) {
		this.person = person;
	}

	setSpecialtyFields(specialtyArray) {
		this.specialties = specialtyArray;
		const clinicalSpecialty = this.prescriptionForm.get('clinicalSpecialty');
		clinicalSpecialty.setValue(specialtyArray[0]);
		if (this.specialties.length === 1)
			clinicalSpecialty.disable();
		this.prescriptionForm.controls['clinicalSpecialty'].markAsTouched();
	}

	closeModal(newPrescription?: NewPrescription): void {
		this.dialogRef.close(newPrescription);
	}

	back(stepper: MatStepper) {
		stepper.previous();
	}

	openPharmacosFrequestDialog() {
		this.isAddMedicationLoading = true;
		this.medicationRequestService.mostFrequentPharmacosPreinscription(this.data.patientId).subscribe((pharmacos: SnomedDto[]) => {
			this.isAddMedicationLoading = false;
			this.dialog.open(PharmacosFrequentComponent, {
				width: '50%',
				data: { pharmacos }
			}).afterClosed().subscribe(result => {
				if (!result || !result.openFormPharmaco) return;
				if (!result.pharmaco && result.openFormPharmaco) return this.openPrescriptionItemDialog();

				this.openPrescriptionItemDialog(mapToNewPrescriptionItem(result.pharmaco));
			});
		})
	}

	openPrescriptionItemDialog(item?: NewPrescriptionItem): void {
		const newPrescriptionItemDialog = this.dialog.open(AgregarPrescripcionItemComponent,
		{
			data: {
				patientId: this.data.patientId,
				titleLabel: this.data.addPrescriptionItemDialogData.titleLabel,
				searchSnomedLabel: this.data.addPrescriptionItemDialogData.searchSnomedLabel,
				showDosage: this.data.addPrescriptionItemDialogData.showDosage,
				showStudyCategory: this.data.addPrescriptionItemDialogData.showStudyCategory,
				eclTerm: this.data.addPrescriptionItemDialogData.eclTerm,
				item,
			},
			width: '35%',
		});

		newPrescriptionItemDialog.afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => {
			if (prescriptionItem) {
				if (!prescriptionItem.id) {
					prescriptionItem.id = ++this.itemCount;
					this.prescriptionItems.push(prescriptionItem);
					this.setShowAddMedicationError();
				} else {
					this.editPrescriptionItem(prescriptionItem);
				}
			}
		});
	}

	private setShowAddMedicationError() {
		this.showAddMedicationError = this.prescriptionItems.length == 0;
	}

	private scrollToBottom() {
		this.dialogScroll.nativeElement.scrollTop = this.dialogScroll.nativeElement.scrollHeight;
	}

	confirmPrescription(): void {
		this.isFinishPrescripcionLoading = true;
		this.showAddMedicationError = false;
		if (this.prescriptionForm.invalid || this.prescriptionItems.length == 0) {
			this.setShowAddMedicationError();
			(this.prescriptionForm.invalid)
				? scrollIntoError(this.prescriptionForm, this.el)
				: this.scrollToBottom()
			return this.prescriptionForm.markAllAsTouched();
		}

		this.submitted = true;
		let prescriptionLineNumberAux = 0;
		const newPrescription: PrescriptionDto = {
			hasRecipe: this.isMedication ? !this.prescriptionForm.controls.withoutRecipe.value : true,
			medicalCoverageId: this.prescriptionForm.controls.patientMedicalCoverage.value?.id,
			items: this.prescriptionItems.map(pi => {
				return {
					healthConditionId: pi.healthProblem.id,
					observations: pi.observations,
					snomed: pi.snomed,
					categoryId: pi.studyCategory?.id,
					dosage: {
						chronic: pi.isChronicAdministrationTime,
						diary: pi.isDailyInterval,
						duration: Number(pi.administrationTimeDays),
						frequency: Number(pi.intervalHours),
						dosesByDay: pi.dayDose,
						dosesByUnit: pi.unitDose,
						quantity: pi.quantity
					},
					prescriptionLineNumber: ++prescriptionLineNumberAux,
				};
			}),
			repetitions: this.prescriptionForm.controls.posdatadas.value,
			isPostDated: this.prescriptionForm.controls.prolongedTreatment.value,
			clinicalSpecialtyId: this.prescriptionForm.controls.clinicalSpecialty.value.id,
			isArchived: this.prescriptionForm.controls.archived.value,
		};
		this.savePrescription(newPrescription);
		if (this.isHabilitarRecetaDigitalEnabled) {
			const patientDto: APatientDto = mapToAPatientDto(this.patientData, this.person, this.prescriptionForm);
			this.patientService.editPatient(patientDto, this.data.patientId).subscribe();
		}
	}

	savePrescription(prescriptionDto: PrescriptionDto) {
		if (prescriptionDto) {
			this.prescripcionesService.createPrescription(this.data.prescriptionType, prescriptionDto, this.data.patientId)
			.subscribe(prescriptionRequestResponse => {
				this.isFinishPrescripcionLoading = false;
				this.closeModal({prescriptionDto, prescriptionRequestResponse, identificationNumber: this.person?.identificationNumber});
			},
			(err: ApiErrorDto) => {
				this.snackBarService.showError(err.errors[0]);
				this.submitted = false;
			});
		}
	}

	deletePrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		this.prescriptionItems.splice(this.prescriptionItems.findIndex(item => item.id === prescriptionItem.id), 1);
		this.setShowAddMedicationError();
	}

	getDosage(prescriptionItem: NewPrescriptionItem): string {
		const intervalText = prescriptionItem.isDailyInterval ? 'Diario' :
			prescriptionItem.intervalHours ? `Cada ${prescriptionItem.intervalHours} hs` : null;

		const administrationTimeText = prescriptionItem.isChronicAdministrationTime ? 'Habitual'
			: prescriptionItem.administrationTimeDays ? `Durante ${prescriptionItem.administrationTimeDays} días` : null;

		return intervalText && administrationTimeText ? (intervalText + ' - ' + administrationTimeText)
			: intervalText ? intervalText : administrationTimeText ? administrationTimeText : '';
	}

	isMedication(): boolean {
		return this.data.prescriptionType === PrescriptionTypes.MEDICATION && ! this.isHabilitarRecetaDigitalEnabled;
	}

	isDailyMedication(prescriptionItem: NewPrescriptionItem): boolean {
		return (prescriptionItem.intervalHours !== null || prescriptionItem.administrationTimeDays !== null);
	}

	clear(control: AbstractControl) {
		control.reset();
	}

	private editPrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		const editPrescriptionItem = this.prescriptionItems.find(pi => pi.id === prescriptionItem.id);

		editPrescriptionItem.snomed = prescriptionItem.snomed;
		editPrescriptionItem.healthProblem = prescriptionItem.healthProblem;
		editPrescriptionItem.unitDose = prescriptionItem.unitDose;
		editPrescriptionItem.dayDose = prescriptionItem.dayDose;
		editPrescriptionItem.quantity = prescriptionItem.quantity;
		editPrescriptionItem.administrationTimeDays = prescriptionItem.administrationTimeDays;
		editPrescriptionItem.isChronicAdministrationTime = prescriptionItem.isChronicAdministrationTime;
		editPrescriptionItem.intervalHours = prescriptionItem.intervalHours;
		editPrescriptionItem.isDailyInterval = prescriptionItem.isDailyInterval;
		editPrescriptionItem.studyCategory = prescriptionItem.studyCategory;
		editPrescriptionItem.observations = prescriptionItem.observations;
	}

}

export class NewPrescriptionData {
	patientId: number;
	personId: number;
	titleLabel: string;
	addLabel: string;
	prescriptionType: PrescriptionTypes;
	prescriptionItemList: NewPrescriptionItem[];
	addPrescriptionItemDialogData: {
		titleLabel: string;
		searchSnomedLabel: string;
		showDosage: boolean;
		showStudyCategory: boolean;
		eclTerm: string;
	};
}

export class NewPrescription {
	prescriptionDto: PrescriptionDto;
	prescriptionRequestResponse: DocumentRequestDto[] | number[];
	identificationNumber: string;
}

enum Steps {
	PATIENT = 0,
	RECIPE = 1,
	MEDICATION = 2
}
