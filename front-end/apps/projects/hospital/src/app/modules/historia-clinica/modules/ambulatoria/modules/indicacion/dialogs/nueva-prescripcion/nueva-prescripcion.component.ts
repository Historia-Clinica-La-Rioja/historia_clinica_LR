import { PrescriptionForm, StatePrescripcionService } from './../../services/state-prescripcion.service';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
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
	DocumentRequestDto,
	NewDosageDto,
	PrescriptionDto,
} from '@api-rest/api-model.d';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import {hasError, scrollIntoError} from '@core/utils/form.utils';
import { NewPrescriptionItem } from '../../../../dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../../services/prescripciones.service';
import { mapToAPatientDto } from '../../utils/prescripcion-mapper';
import { finalize } from 'rxjs';
import { getElementAtPosition } from '@core/utils/array.utils';

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

	prescriptionForm: FormGroup<PrescriptionForm>;
	prescriptionItems: NewPrescriptionItem[];
	isHabilitarRecetaDigitalEnabled: boolean = false;

	hasError = hasError;
	submitted: boolean = false;
	showAddMedicationError: boolean = false;
	isFinishPrescripcionLoading = false;
	isHabilitarRecetaDigitalActualizada = false;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly patientService: PatientService,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<NuevaPrescripcionComponent>,
		private readonly featureFlagService: FeatureFlagService,
		private readonly el: ElementRef,
		private statePrescripcionService: StatePrescripcionService,
		@Inject(MAT_DIALOG_DATA) public prescriptionData: NewPrescriptionData) {
			this.setFeatureFlags();
		}

	ngOnInit(): void {
		this.prescriptionForm = this.statePrescripcionService.getForm();
		this.patientService.getPatientBasicData(Number(this.prescriptionData.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});
	}

	setPerson(person: BMPersonDto) {
		this.person = person;
	}

	closeModal(newPrescription?: NewPrescription): void {
		this.statePrescripcionService.resetForm();
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
					dosage: this.buildDosage(pi),
					prescriptionLineNumber: ++prescriptionLineNumberAux,
					commercialMedicationPrescription: pi.commercialMedicationPrescription,
				    suggestedCommercialMedication: pi.suggestedCommercialMedication
				};
			}),
			repetitions: this.prescriptionForm.controls.posdatadas.value,
			isPostDated: this.prescriptionForm.controls.prolongedTreatment.value,
			clinicalSpecialtyId: this.prescriptionForm.controls.clinicalSpecialty.value.id,
			isArchived: this.prescriptionForm.controls.archived.value ? this.prescriptionForm.controls.archived.value : false,
		};

		this.savePrescription(newPrescription);
		if (this.isHabilitarRecetaDigitalEnabled) {
			const patientDto: APatientDto = mapToAPatientDto(this.patientData, this.person, this.prescriptionForm);
			this.patientService.editPatient(patientDto, this.prescriptionData.patientId).subscribe();
		}
	}

	savePrescription(prescriptionDto: PrescriptionDto) {
		if (!prescriptionDto) return;

		this.prescripcionesService.createPrescription(this.prescriptionData.prescriptionType, prescriptionDto, this.prescriptionData.patientId)
		.pipe(finalize(() => this.isFinishPrescripcionLoading = false))
		.subscribe({
			next: (prescriptionRequestResponse: DocumentRequestDto[] | number[]) => {
				this.closeModal({prescriptionDto, prescriptionRequestResponse, identificationNumber: this.person?.identificationNumber});
				this.statePrescripcionService.resetForm();
			},
			error: (err: ApiErrorDto) => {
				this.submitted = false;
				const message = getElementAtPosition<string>(err.errors, 0);
				this.snackBarService.showError(message);
			}
		});
	}

	isMedication(): boolean {
		return this.prescriptionData.prescriptionType === PrescriptionTypes.MEDICATION && ! this.isHabilitarRecetaDigitalEnabled;
	}

	clear(control: AbstractControl) {
		control.reset();
	}

	private buildDosage = (pi: NewPrescriptionItem) => {
		if (this.isHabilitarRecetaDigitalActualizada) 
			return this.buildNewDosage(pi)
		return this.buildOldDosage(pi)
	}

	private buildNewDosage = (pi: NewPrescriptionItem): NewDosageDto => {
		return {
			chronic: pi.isChronicAdministrationTime,
			diary: pi.isDailyInterval,
			duration: Number(pi.administrationTimeDays),
			frequency: pi.dayDose || Number(pi.intervalHours),
			dosesByUnit: pi.unitDose,
			quantity: pi.quantity
		}
	}

	private buildOldDosage = (pi: NewPrescriptionItem): NewDosageDto => {
		return {
			chronic: pi.isChronicAdministrationTime,
			diary: pi.isDailyInterval,
			duration: Number(pi.administrationTimeDays),
			frequency: Number(pi.intervalHours),
			dosesByDay: pi.dayDose,
			dosesByUnit: pi.unitDose,
			quantity: pi.quantity
		}
	}

	private setFeatureFlags = () => {
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL).subscribe((result: boolean) => this.isHabilitarRecetaDigitalEnabled = result);
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL_ACTUALIZADA).subscribe((result: boolean) => this.isHabilitarRecetaDigitalActualizada = result);
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
