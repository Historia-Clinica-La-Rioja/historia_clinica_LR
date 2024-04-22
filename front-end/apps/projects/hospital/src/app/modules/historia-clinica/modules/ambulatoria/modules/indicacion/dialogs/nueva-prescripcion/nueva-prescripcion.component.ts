import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { PatientService } from '@api-rest/services/patient.service';
import {
	APatientDto,
	ApiErrorDto,
	AppFeature,
	BMPersonDto,
	BasicPatientDto,
	ClinicalSpecialtyDto,
	DocumentRequestDto,
	PrescriptionDto,
} from '@api-rest/api-model.d';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import {hasError, NUMBER_PATTERN, scrollIntoError} from '@core/utils/form.utils';
import { NewPrescriptionItem } from '../../../../dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../../services/prescripciones.service';
import { mapToAPatientDto } from '../../utils/prescripcion-mapper';
import { PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';

const POSDATADAS_DEFAULT = 0;
const POSDATADAS_MIN = 1;
const POSDATADAS_MAX = 11;
const MAX_PHONE_PREFIX: number = 10;
const MAX_PHONE_NUMBER: number = 15;

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
	prescriptionForm: FormGroup<PrescriptionForm>;
	isHabilitarRecetaDigitalEnabled: boolean = false;
	
	hasError = hasError;
	submitted: boolean = false;
	showAddMedicationError: boolean = false;
	isFinishPrescripcionLoading = false;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly patientService: PatientService,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<NuevaPrescripcionComponent>,
		private readonly featureFlagService: FeatureFlagService,
		private readonly el: ElementRef,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionData) {
			this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
				.subscribe((result: boolean) => this.isHabilitarRecetaDigitalEnabled = result);
		}

	ngOnInit(): void {
		this.formConfiguration();

		this.patientService.getPatientBasicData(Number(this.data.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});
		
	}

	private formConfiguration() {
		this.prescriptionForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			withoutRecipe: [false],
			evolucion: [],
			clinicalSpecialty: [null, [Validators.required]],
			prolongedTreatment: [false],
			posdatadas: [{value: POSDATADAS_DEFAULT, disabled: true}, [Validators.min(POSDATADAS_MIN), Validators.max(POSDATADAS_MAX)]],
			archived: [false],
			patientData: this.setPatientDataGroup(),
		});
	}

	private setPatientDataGroup(): FormGroup | null {
		return (this.isHabilitarRecetaDigitalEnabled)
			? this.formBuilder.group({
				phonePrefix: [null, [Validators.required, Validators.maxLength(MAX_PHONE_PREFIX), Validators.pattern(NUMBER_PATTERN)]],
				country: [{value: null, disabled: true}, [Validators.required]],
				phoneNumber: [null, [Validators.required, Validators.maxLength(MAX_PHONE_NUMBER), Validators.pattern(NUMBER_PATTERN)]],
				province: [null, Validators.required],
				locality: [null, Validators.required],
				city: [null, Validators.required],
				street: [null, Validators.required],
				streetNumber: [null, Validators.required]
			})
			: null;
	}

	setPerson(person: BMPersonDto) {
		this.person = person;
	}

	closeModal(newPrescription?: NewPrescription): void {
		this.dialogRef.close(newPrescription);
	}

	back(stepper: MatStepper) {
		stepper.previous();
	}

	updateMedicationErrorState(showAddMedicationError: boolean) {
		this.showAddMedicationError = showAddMedicationError;
	}

	updatePrescriptionItemState(prescriptionItems: NewPrescriptionItem[]) {
		this.prescriptionItems = prescriptionItems;
	}

	private scrollToBottom() {
		this.dialogScroll.nativeElement.scrollTop = this.dialogScroll.nativeElement.scrollHeight;
	}

	confirmPrescription(): void {
		this.isFinishPrescripcionLoading = true;
		if (this.prescriptionForm.invalid || this.prescriptionItems.length == 0) {
			(this.prescriptionForm.invalid)
				? scrollIntoError(this.prescriptionForm, this.el)
				: this.scrollToBottom()
			return this.prescriptionForm.markAllAsTouched();
		}

		this.submitted = true;
		this.showAddMedicationError = false;
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

}

export interface PrescriptionForm {
	patientMedicalCoverage: FormControl<PatientMedicalCoverage>,
	withoutRecipe: FormControl<boolean>,
	evolucion: FormControl<[]>,
	clinicalSpecialty: FormControl<ClinicalSpecialtyDto>,
	prolongedTreatment: FormControl<boolean>,
	posdatadas: FormControl<number>,
	archived: FormControl<boolean>,
	patientData: FormControl<PatientData>,
}

export interface Prescription {
	patientMedicalCoverage: PatientMedicalCoverage,
	withoutRecipe: boolean,
	evolucion: [],
	clinicalSpecialty: ClinicalSpecialtyDto,
	prolongedTreatment: boolean,
	posdatadas: number,
	archived: boolean,
	patientData: PatientData,
}

export interface PatientData {
	phonePrefix: string,
	country: number,
	phoneNumber: string,
	province: number,
	locality: number,
	city: number,
	street: string,
	streetNumber: string
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
