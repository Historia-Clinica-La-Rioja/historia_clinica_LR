import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatRadioChange } from '@angular/material/radio';
import { BasicPatientDto, PatientMedicalCoverageDto, ServiceRequestCategoryDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { MapperService } from '@core/services/mapper.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { STUDY_STATUS_ENUM } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { COMPLETE_NOW, CreateOrderService } from '@historia-clinica/services/create-order.service';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { map } from "rxjs/operators";

@Component({
	selector: 'app-add-study',
	templateUrl: './add-study.component.html',
	styleUrls: ['./add-study.component.scss']
})

export class AddStudyComponent implements OnInit {
	studyCategoryOptions = [];
	study_status = STUDY_STATUS_ENUM;
	completeNow = COMPLETE_NOW;
	radioControl : FormGroup<RadioControl>;
	isLinear = false;
	size = Size.SMALL;
	patient: PatientSummary;
	private patientData: BasicPatientDto;
	patientMedicalCoverages: PatientMedicalCoverage[];
	form: UntypedFormGroup;
	healthProblemOptions = [];
	readonly ecl = SnomedECL.PROCEDURE;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];

	constructor(
		public dialogRef: MatDialogRef<AddStudyComponent>,
		@Inject(MAT_DIALOG_DATA) public readonly data: CreateOrder,
		private requestMasterDataService: RequestMasterDataService,
		private readonly patientService: PatientService,
		private readonly patientNameService: PatientNameService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: UntypedFormBuilder,

	) {
		this.radioControl = new FormGroup<RadioControl>({
			controlButton: new FormControl<STUDY_STATUS_ENUM>(this.study_status.REGISTERED)});

		this.patientService.getPatientBasicData<BasicPatientDto>(this.data.patientId).subscribe(
			patient => {
				this.patientService.getPatientPhoto(patient.id).subscribe(photo => {
					this.patient = this.mapperService.toPatientSummary(patient,photo, this.patientNameService);
				})
			});
	 }

	ngOnInit() {
		this.form = this.formBuilder.group({
			patientMedicalCoverage: [null],
			healthProblem: [null, Validators.required],
			observations: [null, Validators.required]
		});

		this.setMedicalCoverages();

		this.patientService.getPatientBasicData(Number(this.data.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});

		this.healthProblemOptions = this.data.problems;

		this.requestMasterDataService.categoriesWithoutDiagnosticImaging().subscribe((categories: ServiceRequestCategoryDto[]) => {
			this.studyCategoryOptions = categories;
		});
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.data.patientId)
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
				genderId: this.patientData.person.gender.id,
				identificationNumber: this.patientData.person.identificationNumber,
				identificationTypeId: this.patientData.person.identificationTypeId,
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

	close() {
		this.data.createOrderService.resetForm();
		this.dialogRef.close()
	}

	setCreationStatus($event: MatRadioChange) {
		this.data.createOrderService.setCreationStatus($event.value);
	}

	addOrder() {
		this.data.createOrderService.setFileNames(this.selectedFilesShow);
		this.data.createOrderService.setObservation(this.form.controls.observations.value);
		this.data.createOrderService.addToList();
		this.close();
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

	setProblem(healthProblem: SnomedDto) {
		this.data.createOrderService.setProblem(healthProblem);
	}

	onSelectFileFormData($event): void {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	removeSelectedFile(index): void {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}
}

export interface CreateOrder {
	patientId: number,
	createOrderService: CreateOrderService,
	problems: SnomedDto[]
}

interface RadioControl {
	controlButton: FormControl<STUDY_STATUS_ENUM | null>;
}
