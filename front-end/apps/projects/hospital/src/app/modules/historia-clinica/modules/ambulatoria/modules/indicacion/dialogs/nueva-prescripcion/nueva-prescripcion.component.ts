import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { map } from 'rxjs/operators';

import { MapperService } from '@core/services/mapper.service';

import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import { ApiErrorDto, AppFeature, BasicPatientDto, ClinicalSpecialtyDto, PatientMedicalCoverageDto, PrescriptionDto } from '@api-rest/api-model.d';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';

import { AgregarPrescripcionItemComponent, NewPrescriptionItem } from '../../../../dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../../services/prescripciones.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-nueva-prescripcion',
	templateUrl: './nueva-prescripcion.component.html',
	styleUrls: ['./nueva-prescripcion.component.scss']
})
export class NuevaPrescripcionComponent implements OnInit {

	prescriptionItems: NewPrescriptionItem[];
	patientMedicalCoverages: PatientMedicalCoverage[];
	prescriptionForm: FormGroup;
	specialties: ClinicalSpecialtyDto[];
	itemCount = 0;
	private patientData: BasicPatientDto;
	isHabilitarRecetaDigitalEnabled: boolean = false;
	POSDATADAS_DEFAULT = 1;
	POSDATADAS_MIN = 0;
	POSDATADAS_MAX = 11;
	hasError = hasError;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly patientService: PatientService,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<NuevaPrescripcionComponent>,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly featureFlagService: FeatureFlagService,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionData) {
			this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
				.subscribe((result: boolean) => this.isHabilitarRecetaDigitalEnabled = result);
		}

	ngOnInit(): void {
		this.setProfessionalSpecialties();

		this.prescriptionForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			withoutRecipe: [false],
			evolucion: [],
			clinicalSpecialty: [null, [Validators.required]],
			prolongedTreatment: [null],
			posdatadas: [{value: this.POSDATADAS_DEFAULT, disabled: true}, [Validators.min(this.POSDATADAS_MIN), Validators.max(this.POSDATADAS_MAX)]],
		});
		this.prescriptionItems = this.data.prescriptionItemList ? this.data.prescriptionItemList : [];
		this.setMedicalCoverages();
		this.patientService.getPatientBasicData(Number(this.data.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});

		if (this.isHabilitarRecetaDigitalEnabled)
			this.openPrescriptionItemDialog();
	}

	setProlongedTreatment(isOn: boolean) {
		this.prescriptionForm.controls.prolongedTreatment.setValue(isOn);
		const posdatadas = this.prescriptionForm.controls.posdatadas;
		if (isOn) {
			posdatadas.enable()
			posdatadas.addValidators(Validators.required);
		} else
			this.disablePosdatadas(posdatadas)
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
				} else {
					this.editPrescriptionItem(prescriptionItem);
				}
			}
		});
	}

	confirmPrescription(): void {
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
						dosesByUnit: pi.unitDose
					},
					prescriptionLineNumber: ++prescriptionLineNumberAux,
				};
			}),
			repetitions: this.prescriptionForm.controls.posdatadas.value,
			isPostDated: this.prescriptionForm.controls.posdatadas.value > this.POSDATADAS_MIN,
			clinicalSpecialtyId: this.prescriptionForm.controls.clinicalSpecialty.value.id,
		};
		this.savePrescription(newPrescription);

	}

	savePrescription(prescriptionDto: PrescriptionDto) {
		if (prescriptionDto) {
			this.prescripcionesService.createPrescription(this.data.prescriptionType, prescriptionDto, this.data.patientId)
			.subscribe(prescriptionRequestResponse => {
					this.closeModal({prescriptionDto, prescriptionRequestResponse});
				},
					(err: ApiErrorDto) => {
						this.snackBarService.showError(err.errors[0]);
					});
		}
	}


	deletePrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		this.prescriptionItems.splice(this.prescriptionItems.findIndex(item => item.id === prescriptionItem.id), 1);
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

	private editPrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		const editPrescriptionItem = this.prescriptionItems.find(pi => pi.id === prescriptionItem.id);

		editPrescriptionItem.snomed = prescriptionItem.snomed;
		editPrescriptionItem.healthProblem = prescriptionItem.healthProblem;
		editPrescriptionItem.unitDose = prescriptionItem.unitDose;
		editPrescriptionItem.dayDose = prescriptionItem.dayDose;
		editPrescriptionItem.administrationTimeDays = prescriptionItem.administrationTimeDays;
		editPrescriptionItem.isChronicAdministrationTime = prescriptionItem.isChronicAdministrationTime;
		editPrescriptionItem.intervalHours = prescriptionItem.intervalHours;
		editPrescriptionItem.isDailyInterval = prescriptionItem.isDailyInterval;
		editPrescriptionItem.studyCategory = prescriptionItem.studyCategory;
		editPrescriptionItem.observations = prescriptionItem.observations;
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.data.patientId))
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patientData.person?.gender.id,
				identificationNumber: this.patientData.person?.identificationNumber,
				identificationTypeId: this.patientData.person?.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this.patientData.id
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.data.patientId), patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_SUCCESS');
						},
						_ => this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_ERROR')
					);
				}
			}
		);
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}

export class NewPrescriptionData {
	patientId: number;
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
	prescriptionRequestResponse: number | number[];
}
